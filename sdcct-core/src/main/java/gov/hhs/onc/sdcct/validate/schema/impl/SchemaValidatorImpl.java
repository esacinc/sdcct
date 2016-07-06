package gov.hhs.onc.sdcct.validate.schema.impl;

import gov.hhs.onc.sdcct.api.IssueLevel;
import gov.hhs.onc.sdcct.data.metadata.ResourceMetadata;
import gov.hhs.onc.sdcct.net.utils.SdcctIoUtils.NoOpOutputStream;
import gov.hhs.onc.sdcct.transform.impl.SdcctLocation;
import gov.hhs.onc.sdcct.validate.ValidationException;
import gov.hhs.onc.sdcct.validate.ValidationIssue;
import gov.hhs.onc.sdcct.validate.ValidationSource;
import gov.hhs.onc.sdcct.validate.ValidationType;
import gov.hhs.onc.sdcct.validate.impl.AbstractSdcctValidator;
import gov.hhs.onc.sdcct.validate.impl.ValidationIssueImpl;
import gov.hhs.onc.sdcct.validate.impl.ValidationLocationImpl;
import gov.hhs.onc.sdcct.validate.impl.ValidationSourceImpl;
import gov.hhs.onc.sdcct.validate.schema.SchemaValidator;
import gov.hhs.onc.sdcct.xml.impl.AbstractSdcctXmlReporter;
import gov.hhs.onc.sdcct.xml.jaxb.metadata.JaxbComplexTypeMetadata;
import gov.hhs.onc.sdcct.xml.jaxb.metadata.JaxbContextMetadata;
import gov.hhs.onc.sdcct.xml.jaxb.metadata.JaxbSchemaMetadata;
import java.net.URI;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.xml.transform.stream.StreamResult;
import net.sf.saxon.event.Sender;
import net.sf.saxon.om.NodeInfo;
import net.sf.saxon.stax.ReceiverToXMLStreamWriter;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.stax2.XMLStreamWriter2;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component("validatorSchema")
@Order(Ordered.HIGHEST_PRECEDENCE)
public class SchemaValidatorImpl extends AbstractSdcctValidator implements SchemaValidator {
    private static class ValidatorXmlReporter extends AbstractSdcctXmlReporter {
        private Map<String, ValidationSource> schemaSrcs;
        private List<ValidationIssue> issues;

        public ValidatorXmlReporter(Map<String, ValidationSource> schemaSrcs, List<ValidationIssue> issues) {
            this.schemaSrcs = schemaSrcs;
            this.issues = issues;
        }

        @Override
        public void report(IssueLevel level, String msg, SdcctLocation loc) {
            // noinspection ConstantConditions
            this.issues.add(new ValidationIssueImpl(ValidationType.SCHEMA, level, msg, new ValidationLocationImpl(loc),
                (loc.hasElementQname() ? this.schemaSrcs.get(loc.getElementQname().getNamespaceURI()) : null)));
        }
    }

    public SchemaValidatorImpl() {
        super(ValidationType.SCHEMA);
    }

    @Override
    protected List<ValidationIssue> validateInternal(NodeInfo nodeInfo, JaxbComplexTypeMetadata<?> jaxbTypeMetadata, Class<?> beanClass,
        ResourceMetadata<?> resourceMetadata, List<ValidationIssue> issues) throws ValidationException {
        JaxbContextMetadata jaxbContextMetadata = jaxbTypeMetadata.getContext();
        Map<String, JaxbSchemaMetadata> jaxbSchemaMetadatas = jaxbContextMetadata.getSchemas();
        Map<String, ValidationSource> schemaSrcs = new LinkedHashMap<>(jaxbSchemaMetadatas.size());

        for (String schemaNsUri : jaxbSchemaMetadatas.keySet()) {
            // TODO: Build XML schema IDs + names.
            schemaSrcs.put(schemaNsUri, new ValidationSourceImpl(null, null, URI.create(schemaNsUri)));
        }

        try {
            XMLStreamWriter2 resultWriter = this.xmlOutFactory.createXMLStreamWriter(new StreamResult(NoOpOutputStream.INSTANCE));

            ValidatorXmlReporter reporter = new ValidatorXmlReporter(schemaSrcs, issues);
            resultWriter.setValidationProblemHandler(reporter);
            resultWriter.validateAgainst(jaxbContextMetadata.getValidationSchema());

            ReceiverToXMLStreamWriter receiver = new ReceiverToXMLStreamWriter(resultWriter);
            receiver.setPipelineConfiguration(this.config.makePipelineConfiguration());

            Sender.send(nodeInfo, receiver, null);
        } catch (Exception e) {
            issues.clear();

            throw new ValidationException(String.format("Unable to validate XML node (nsPrefix=%s, nsUri=%s, localName=%s) using XML schemas (nsUris=[%s]).",
                nodeInfo.getPrefix(), nodeInfo.getURI(), nodeInfo.getLocalPart(), StringUtils.join(jaxbContextMetadata.getSchemas().keySet(), ", ")), e,
                issues);
        }

        return issues;
    }
}
