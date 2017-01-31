package gov.hhs.onc.sdcct.validate.impl;

import gov.hhs.onc.sdcct.data.metadata.ResourceMetadata;
import gov.hhs.onc.sdcct.transform.content.path.ContentPath;
import gov.hhs.onc.sdcct.transform.content.path.ContentPathBuilder;
import gov.hhs.onc.sdcct.transform.saxon.impl.SdcctSaxonConfiguration;
import gov.hhs.onc.sdcct.transform.location.SdcctLocator;
import gov.hhs.onc.sdcct.transform.location.impl.SdcctLocation;
import gov.hhs.onc.sdcct.validate.SdcctValidator;
import gov.hhs.onc.sdcct.validate.ValidationException;
import gov.hhs.onc.sdcct.validate.ValidationLocation;
import gov.hhs.onc.sdcct.validate.ValidationResult;
import gov.hhs.onc.sdcct.validate.ValidationType;
import gov.hhs.onc.sdcct.xml.XmlStreamAccessor;
import gov.hhs.onc.sdcct.xml.impl.SdcctXmlOutputFactory;
import gov.hhs.onc.sdcct.xml.saxon.impl.XdmDocument;
import gov.hhs.onc.sdcct.xml.jaxb.metadata.JaxbContextMetadata;
import javax.annotation.Nullable;
import net.sf.saxon.om.NodeInfo;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractSdcctValidator implements SdcctValidator {
    @Autowired
    protected SdcctXmlOutputFactory xmlOutFactory;

    @Autowired
    protected ContentPathBuilder contentPathBuilder;

    @Autowired
    protected SdcctSaxonConfiguration config;

    protected ValidationType type;
    protected boolean validateStream;
    protected boolean validateDoc;

    protected AbstractSdcctValidator(ValidationType type, boolean validateStream, boolean validateDoc) {
        this.type = type;
        this.validateStream = validateStream;
        this.validateDoc = validateDoc;
    }

    @Override
    public XdmDocument validateDocument(ValidationResult result, ResourceMetadata<?> resourceMetadata, XdmDocument doc) throws ValidationException {
        return doc;
    }

    @Nullable
    @Override
    public <T extends XmlStreamAccessor> T validateStream(ValidationResult result, JaxbContextMetadata jaxbContextMetadata, T streamAccessor)
        throws ValidationException {
        return null;
    }

    protected static ValidationLocation buildLocation(ContentPathBuilder contentPathBuilder, NodeInfo nodeInfo) throws Exception {
        SdcctLocation loc = new SdcctLocation(nodeInfo);
        loc.setContentPath(contentPathBuilder.build(null, false, nodeInfo));

        return buildLocation(null, loc);
    }

    protected static ValidationLocation buildLocation(@Nullable SdcctLocator locator, SdcctLocation loc) {
        ValidationLocation validationLoc = new ValidationLocationImpl().setLineNumber(loc.getLineNumber()).setColumnNumber(loc.getColumnNumber());

        if (!loc.hasContentPath() && (locator != null)) {
            locator.buildLocation(loc);
        }

        if (loc.hasContentPath()) {
            ContentPath locContentPath = loc.getContentPath();

            // noinspection ConstantConditions
            validationLoc.setFluentPathExpression(locContentPath.getFluentPathExpression()).setJsonPointerExpression(locContentPath.getJsonPointerExpression())
                .setXpathExpression(locContentPath.getXpathExpression());
        }

        return validationLoc;
    }

    @Override
    public ValidationType getType() {
        return this.type;
    }

    @Override
    public boolean getValidateDocument() {
        return this.validateDoc;
    }

    @Override
    public boolean getValidateStream() {
        return this.validateStream;
    }
}
