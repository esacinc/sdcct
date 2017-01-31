package gov.hhs.onc.sdcct.validate.schema.impl;

import gov.hhs.onc.sdcct.api.SdcctIssueSeverity;
import gov.hhs.onc.sdcct.transform.location.SdcctLocator;
import gov.hhs.onc.sdcct.transform.location.impl.SdcctLocation;
import gov.hhs.onc.sdcct.validate.ValidationException;
import gov.hhs.onc.sdcct.validate.ValidationIssueSeverity;
import gov.hhs.onc.sdcct.validate.ValidationIssues;
import gov.hhs.onc.sdcct.validate.ValidationResult;
import gov.hhs.onc.sdcct.validate.ValidationSource;
import gov.hhs.onc.sdcct.validate.ValidationType;
import gov.hhs.onc.sdcct.validate.impl.AbstractSdcctValidator;
import gov.hhs.onc.sdcct.validate.impl.ValidationIssueImpl;
import gov.hhs.onc.sdcct.validate.impl.ValidationSourceImpl;
import gov.hhs.onc.sdcct.validate.schema.SchemaValidator;
import gov.hhs.onc.sdcct.xml.XmlStreamAccessor;
import gov.hhs.onc.sdcct.xml.impl.AbstractSdcctXmlReporter;
import gov.hhs.onc.sdcct.xml.jaxb.metadata.JaxbContextMetadata;
import gov.hhs.onc.sdcct.xml.jaxb.metadata.JaxbSchemaMetadata;
import gov.hhs.onc.sdcct.xml.validate.impl.MsvXmlSchemaImpl;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.annotation.Nullable;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component("validatorSchema")
@Order(Ordered.HIGHEST_PRECEDENCE)
public class SchemaValidatorImpl extends AbstractSdcctValidator implements SchemaValidator {
    private static class SchemaValidatorXmlReporter extends AbstractSdcctXmlReporter<SchemaValidatorXmlReporter> {
        private SdcctLocator locator;
        private Map<String, ValidationSource> schemaSrcs;
        private ValidationIssues issues;

        public SchemaValidatorXmlReporter(SdcctLocator locator, Map<String, ValidationSource> schemaSrcs, ValidationIssues issues) {
            super();

            this.locator = locator;
            this.schemaSrcs = schemaSrcs;
            this.issues = issues;
        }

        @Override
        public void report(SdcctIssueSeverity severity, String msg, SdcctLocation loc, @Nullable Throwable exception) {
            // noinspection ConstantConditions
            this.issues.addIssues(new ValidationIssueImpl().setType(ValidationType.SCHEMA).setSeverity(ValidationIssueSeverity.valueOf(severity.name()))
                .setLocation(buildLocation(this.locator, loc))
                .setSource((loc.hasElementQname() ? this.schemaSrcs.get(loc.getElementQname().getNamespaceURI()) : null)).setMessage(msg));
        }

        @Override
        protected SchemaValidatorXmlReporter cloneInternal() {
            return this;
        }
    }

    public SchemaValidatorImpl() {
        super(ValidationType.SCHEMA, true, false);
    }

    @Override
    public <T extends XmlStreamAccessor> T validateStream(ValidationResult result, JaxbContextMetadata jaxbContextMetadata, T streamAccessor)
        throws ValidationException {
        ValidationIssues issues = result.getIssues();
        MsvXmlSchemaImpl schema = jaxbContextMetadata.getValidationSchema();
        String schemaId = schema.getId(), schemaName = schema.getName();
        Map<String, JaxbSchemaMetadata> jaxbSchemaMetadatas = jaxbContextMetadata.getSchemas();
        Map<String, ValidationSource> schemaSrcs = new LinkedHashMap<>(jaxbSchemaMetadatas.size());

        for (String schemaNsUri : jaxbSchemaMetadatas.keySet()) {
            schemaSrcs.put(schemaNsUri, new ValidationSourceImpl().setId(schemaId).setName(schemaName).setUri(schemaNsUri));
        }

        streamAccessor.setReporter(new SchemaValidatorXmlReporter(streamAccessor.getLocator(), schemaSrcs, issues));

        try {
            streamAccessor.getValidator().setDelegate(schema.createValidator(streamAccessor.getValidationContext()));
        } catch (Exception e) {
            issues.getIssues().clear();

            throw new ValidationException(
                String.format("Unable to configure stream validation using XML schema(s) (nsUris=[%s]).", StringUtils.join(schemaSrcs.keySet(), ", ")), e,
                result);
        }

        return streamAccessor;
    }
}
