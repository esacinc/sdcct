package gov.hhs.onc.sdcct.validate.schematron.impl;

import gov.hhs.onc.sdcct.data.metadata.ResourceMetadata;
import gov.hhs.onc.sdcct.schematron.svrl.AttributeValueNamespace;
import gov.hhs.onc.sdcct.schematron.svrl.FailedAssertion;
import gov.hhs.onc.sdcct.schematron.svrl.FiredRule;
import gov.hhs.onc.sdcct.schematron.svrl.impl.OutputImpl;
import gov.hhs.onc.sdcct.transform.impl.ByteArrayResult;
import gov.hhs.onc.sdcct.validate.ValidationException;
import gov.hhs.onc.sdcct.validate.ValidationIssueSeverity;
import gov.hhs.onc.sdcct.validate.ValidationIssues;
import gov.hhs.onc.sdcct.validate.ValidationResult;
import gov.hhs.onc.sdcct.validate.ValidationSource;
import gov.hhs.onc.sdcct.validate.ValidationType;
import gov.hhs.onc.sdcct.validate.impl.AbstractSdcctValidator;
import gov.hhs.onc.sdcct.validate.impl.ValidationIssueImpl;
import gov.hhs.onc.sdcct.validate.impl.ValidationSourceImpl;
import gov.hhs.onc.sdcct.validate.schematron.SchematronValidator;
import gov.hhs.onc.sdcct.validate.schematron.SdcctSchematron;
import gov.hhs.onc.sdcct.xml.saxon.impl.XdmDocument;
import gov.hhs.onc.sdcct.xml.impl.XmlCodec;
import gov.hhs.onc.sdcct.xml.xpath.impl.DynamicXpathOptionsImpl;
import gov.hhs.onc.sdcct.xml.xpath.saxon.impl.SdcctXpathCompiler;
import gov.hhs.onc.sdcct.xml.xpath.impl.StaticXpathOptionsImpl;
import gov.hhs.onc.sdcct.xml.xslt.saxon.impl.SdcctXsltTransformer;
import java.util.Map;
import java.util.TreeMap;
import javax.xml.XMLConstants;
import net.sf.saxon.stax.XMLStreamWriterDestination;
import net.sf.saxon.tree.linked.DocumentImpl;
import net.sf.saxon.tree.linked.ElementImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component("validatorSchematron")
@Order((Ordered.HIGHEST_PRECEDENCE + 1))
public class SchematronValidatorImpl extends AbstractSdcctValidator implements SchematronValidator {
    @Autowired
    private XmlCodec xmlCodec;

    @Autowired
    private SdcctXpathCompiler xpathCompiler;

    public SchematronValidatorImpl() {
        super(ValidationType.SCHEMATRON, false, true);
    }

    @Override
    public XdmDocument validateDocument(ValidationResult result, ResourceMetadata<?> resourceMetadata, XdmDocument doc) throws ValidationException {
        if (!resourceMetadata.hasSchematrons()) {
            return super.validateDocument(result, resourceMetadata, doc);
        }

        ValidationIssues issues = result.getIssues();
        DocumentImpl docInfo = doc.getUnderlyingNode();
        ElementImpl docElemInfo = doc.getDocumentElement();
        String docPublicId = docInfo.getPublicId(), docSysId = docInfo.getSystemId(), nsUri = docElemInfo.getURI(), id, name, contextXpathExpr = null;
        ValidationSource src;
        Map<String, String> namespaces = new TreeMap<>();
        AttributeValueNamespace attrValueNs;
        FailedAssertion failedAssertion;

        // noinspection ConstantConditions
        for (SdcctSchematron schematron : resourceMetadata.getSchematrons()) {
            // noinspection ConstantConditions
            src = new ValidationSourceImpl().setId((id = schematron.getId())).setName((name = schematron.getName()))
                .setUri(schematron.getDocument().getDocumentUri().getUri());

            namespaces.clear();
            namespaces.putAll(schematron.getSchemaNamespaces());
            namespaces.put(XMLConstants.DEFAULT_NS_PREFIX, nsUri);

            try {
                ByteArrayResult schemaResult = new ByteArrayResult(docPublicId, docSysId);

                SdcctXsltTransformer schemaXsltTransformer = schematron.getSchemaXsltExecutable().load();
                schemaXsltTransformer.setSource(docInfo);
                schemaXsltTransformer.setDestination(new XMLStreamWriterDestination(this.xmlOutFactory.createXMLStreamWriter(schemaResult)));

                schemaXsltTransformer.transform();

                for (Object outputContentItem : this.xmlCodec.decode(schemaResult.getBytes(), OutputImpl.class, null).getContent()) {
                    if (outputContentItem instanceof AttributeValueNamespace) {
                        namespaces.put((attrValueNs = ((AttributeValueNamespace) outputContentItem)).getPrefix(), attrValueNs.getUri());
                    } else if (outputContentItem instanceof FiredRule) {
                        contextXpathExpr = ((FiredRule) outputContentItem).getContext();
                    } else if (outputContentItem instanceof FailedAssertion) {
                        // noinspection ConstantConditions
                        issues.addIssues(new ValidationIssueImpl().setType(ValidationType.SCHEMATRON).setSeverity(ValidationIssueSeverity.ERROR)
                            .setLocation(buildLocation(this.contentPathBuilder, this.xpathCompiler
                                .compile((failedAssertion = ((FailedAssertion) outputContentItem)).getLocation(),
                                    new StaticXpathOptionsImpl().setNamespaces(namespaces))
                                .load(new DynamicXpathOptionsImpl().setContextItem(docInfo)).evaluateNode().getUnderlyingNode()))
                            .setSource(src).setContextXpathExpression(contextXpathExpr).setTestXpathExpression(failedAssertion.getTest())
                            .setMessage(failedAssertion.getText()));
                    }
                }
            } catch (Exception e) {
                issues.getIssues().clear();

                throw new ValidationException(
                    String.format("Unable to validate XML document element (nsPrefix=%s, nsUri=%s, localName=%s) using Schematron (id=%s, name=%s).",
                        docElemInfo.getPrefix(), docElemInfo.getURI(), docElemInfo.getLocalPart(), id, name),
                    e, result);
            }
        }

        return super.validateDocument(result, resourceMetadata, doc);
    }
}
