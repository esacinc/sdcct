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
import net.sf.saxon.stax.ReceiverToXMLStreamWriter;
import net.sf.saxon.tree.linked.DocumentImpl;
import net.sf.saxon.tree.linked.ElementImpl;
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
    protected List<ValidationIssue> validateInternal(DocumentImpl docInfo, ElementImpl docElemInfo, JaxbComplexTypeMetadata<?> jaxbTypeMetadata,
        Class<?> beanClass, ResourceMetadata<?> resourceMetadata, List<ValidationIssue> issues) throws ValidationException {
        JaxbContextMetadata jaxbContextMetadata = jaxbTypeMetadata.getContext();
        MsvSchema schema = jaxbContextMetadata.getValidationSchema();
        String schemaId = schema.getId(), schemaName = schema.getName();
        Map<String, JaxbSchemaMetadata> jaxbSchemaMetadatas = jaxbContextMetadata.getSchemas();
        Map<String, ValidationSource> schemaSrcs = new LinkedHashMap<>(jaxbSchemaMetadatas.size());

        for (String schemaNsUri : jaxbSchemaMetadatas.keySet()) {
            schemaSrcs.put(schemaNsUri, new ValidationSourceImpl(schemaId, schemaName, URI.create(schemaNsUri)));
        }

        try {
            XMLStreamWriter2 resultWriter = this.xmlOutFactory.createXMLStreamWriter(new StreamResult(NoOpOutputStream.INSTANCE));

            ValidatorXmlReporter reporter = new ValidatorXmlReporter(schemaSrcs, issues);
            resultWriter.setValidationProblemHandler(reporter);
            resultWriter.validateAgainst(schema);

            ReceiverToXMLStreamWriter receiver = new ReceiverToXMLStreamWriter(resultWriter);
            receiver.setPipelineConfiguration(this.config.makePipelineConfiguration());

            Sender.send(docElemInfo, receiver, null);
        } catch (Exception e) {
            issues.clear();

            throw new ValidationException(String.format(
                "Unable to validate XML document element (nsPrefix=%s, nsUri=%s, localName=%s) using XML schemas (nsUris=[%s]).", docElemInfo.getPrefix(),
                docElemInfo.getURI(), docElemInfo.getLocalPart(), StringUtils.join(jaxbContextMetadata.getSchemas().keySet(), ", ")), e, issues);
        }

        return issues;
    }
}
