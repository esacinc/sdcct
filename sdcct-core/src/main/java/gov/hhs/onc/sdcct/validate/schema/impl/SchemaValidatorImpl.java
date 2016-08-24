package gov.hhs.onc.sdcct.validate.schema.impl;

import gov.hhs.onc.sdcct.api.SdcctIssueSeverity;
import gov.hhs.onc.sdcct.transform.impl.SdcctLocation;
import gov.hhs.onc.sdcct.validate.ValidationException;
import gov.hhs.onc.sdcct.validate.ValidationResult;
import gov.hhs.onc.sdcct.validate.ValidationSource;
import gov.hhs.onc.sdcct.validate.ValidationType;
import gov.hhs.onc.sdcct.validate.impl.AbstractSdcctValidator;
import gov.hhs.onc.sdcct.validate.impl.ValidationIssueImpl;
import gov.hhs.onc.sdcct.validate.impl.ValidationLocationImpl;
import gov.hhs.onc.sdcct.validate.impl.ValidationSourceImpl;
import gov.hhs.onc.sdcct.validate.schema.SchemaValidator;
import gov.hhs.onc.sdcct.xml.impl.AbstractSdcctXmlReporter;
import gov.hhs.onc.sdcct.xml.jaxb.metadata.JaxbContextMetadata;
import gov.hhs.onc.sdcct.xml.jaxb.metadata.JaxbSchemaMetadata;
import java.net.URI;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.annotation.Nullable;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.stax2.validation.Validatable;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component("validatorSchema")
@Order(Ordered.HIGHEST_PRECEDENCE)
public class SchemaValidatorImpl extends AbstractSdcctValidator implements SchemaValidator {
    private static class SchemaValidatorXmlReporter extends AbstractSdcctXmlReporter<SchemaValidatorXmlReporter> {
        private Map<String, ValidationSource> schemaSrcs;
        private ValidationResult result;

        public SchemaValidatorXmlReporter(Map<String, ValidationSource> schemaSrcs, ValidationResult result) {
            super(null);

            this.schemaSrcs = schemaSrcs;
            this.result = result;
        }

        @Override
        public void report(SdcctIssueSeverity severity, String msg, SdcctLocation loc, @Nullable Throwable exception) {
            // noinspection ConstantConditions
            this.result.addIssues(new ValidationIssueImpl(ValidationType.SCHEMA, severity, msg, new ValidationLocationImpl(loc),
                (loc.hasElementQname() ? this.schemaSrcs.get(loc.getElementQname().getNamespaceURI()) : null)));
        }

        @Override
        protected SchemaValidatorXmlReporter cloneInternal() {
            return new SchemaValidatorXmlReporter(this.schemaSrcs, this.result);
        }
    }

    public SchemaValidatorImpl() {
        super(ValidationType.SCHEMA, true, false);
    }

    @Override
    public <T extends Validatable> MsvValidator validateStream(ValidationResult result, JaxbContextMetadata jaxbContextMetadata, T streamAccessor)
        throws ValidationException {
        MsvSchema schema = jaxbContextMetadata.getValidationSchema();
        String schemaId = schema.getId(), schemaName = schema.getName();
        Map<String, JaxbSchemaMetadata> jaxbSchemaMetadatas = jaxbContextMetadata.getSchemas();
        Map<String, ValidationSource> schemaSrcs = new LinkedHashMap<>(jaxbSchemaMetadatas.size());

        for (String schemaNsUri : jaxbSchemaMetadatas.keySet()) {
            schemaSrcs.put(schemaNsUri, new ValidationSourceImpl(schemaId, schemaName, URI.create(schemaNsUri)));
        }

        streamAccessor.setValidationProblemHandler(new SchemaValidatorXmlReporter(schemaSrcs, result));

        MsvValidator msvValidator;

        try {
            msvValidator = ((MsvValidator) streamAccessor.validateAgainst(schema));
        } catch (Exception e) {
            throw new ValidationException(
                String.format("Unable to configure stream validation using XML schema(s) (nsUris=[%s]).", StringUtils.join(schemaSrcs.keySet(), ", ")), e,
                result.setIssues());
        }

        return msvValidator;
    }
}
