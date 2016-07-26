package gov.hhs.onc.sdcct.validate.schematron.impl;

import gov.hhs.onc.sdcct.api.IssueLevel;
import gov.hhs.onc.sdcct.data.metadata.ResourceMetadata;
import gov.hhs.onc.sdcct.schematron.svrl.AttributeValueNamespace;
import gov.hhs.onc.sdcct.schematron.svrl.FailedAssertion;
import gov.hhs.onc.sdcct.schematron.svrl.FiredRule;
import gov.hhs.onc.sdcct.schematron.svrl.impl.OutputImpl;
import gov.hhs.onc.sdcct.transform.impl.ByteArrayResult;
import gov.hhs.onc.sdcct.transform.impl.SdcctLocation;
import gov.hhs.onc.sdcct.validate.ValidationException;
import gov.hhs.onc.sdcct.validate.ValidationIssue;
import gov.hhs.onc.sdcct.validate.ValidationSource;
import gov.hhs.onc.sdcct.validate.ValidationType;
import gov.hhs.onc.sdcct.validate.impl.AbstractSdcctValidator;
import gov.hhs.onc.sdcct.validate.impl.ValidationIssueImpl;
import gov.hhs.onc.sdcct.validate.impl.ValidationLocationImpl;
import gov.hhs.onc.sdcct.validate.impl.ValidationSourceImpl;
import gov.hhs.onc.sdcct.validate.schematron.SchematronValidator;
import gov.hhs.onc.sdcct.validate.schematron.SdcctSchematron;
import gov.hhs.onc.sdcct.xml.impl.XmlCodec;
import gov.hhs.onc.sdcct.xml.jaxb.metadata.JaxbComplexTypeMetadata;
import gov.hhs.onc.sdcct.xml.xpath.impl.DynamicXpathOptionsImpl;
import gov.hhs.onc.sdcct.xml.xpath.impl.SdcctXpathCompiler;
import gov.hhs.onc.sdcct.xml.xpath.impl.StaticXpathOptionsImpl;
import gov.hhs.onc.sdcct.xml.xslt.impl.SdcctXsltTransformer;
import java.net.URI;
import java.util.List;
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
        super(ValidationType.SCHEMATRON);
    }

    @Override
    protected List<ValidationIssue> validateInternal(DocumentImpl docInfo, ElementImpl docElemInfo, JaxbComplexTypeMetadata<?> jaxbTypeMetadata,
        Class<?> beanClass, ResourceMetadata<?> resourceMetadata, List<ValidationIssue> issues) throws ValidationException {
        if (!resourceMetadata.hasSchematrons()) {
            return issues;
        }

        String docPublicId = docInfo.getPublicId(), docSysId = docInfo.getSystemId(), nsUri = docElemInfo.getURI(), id, name, contextXpathExpr = null;
        ValidationSource src;
        Map<String, String> namespaces = new TreeMap<>();
        AttributeValueNamespace attrValueNs;
        FailedAssertion failedAssertion;
        ValidationIssue issue;

        // noinspection ConstantConditions
        for (SdcctSchematron schematron : resourceMetadata.getSchematrons()) {
            // noinspection ConstantConditions
            src = new ValidationSourceImpl((id = schematron.getId()), (name = schematron.getName()),
                URI.create(schematron.getDocument().getDocumentUri().getUri()));

            namespaces.clear();
            namespaces.putAll(schematron.getSchemaNamespaces());
            namespaces.put(XMLConstants.DEFAULT_NS_PREFIX, nsUri);

            try {
                SdcctXsltTransformer xsltTransformer = schematron.getSchemaXsltExecutable().load();
                xsltTransformer.setSource(docInfo);

                ByteArrayResult result = new ByteArrayResult(docPublicId, docSysId);

                xsltTransformer.setDestination(new XMLStreamWriterDestination(this.xmlOutFactory.createXMLStreamWriter(result)));

                xsltTransformer.transform();

                for (Object outputContentItem : this.xmlCodec.decode(result.getBytes(), OutputImpl.class, null).getContent()) {
                    if (outputContentItem instanceof AttributeValueNamespace) {
                        namespaces.put((attrValueNs = ((AttributeValueNamespace) outputContentItem)).getPrefix(), attrValueNs.getUri());
                    } else if (outputContentItem instanceof FiredRule) {
                        contextXpathExpr = ((FiredRule) outputContentItem).getContext();
                    } else if (outputContentItem instanceof FailedAssertion) {
                        // noinspection ConstantConditions
                        (issue = new ValidationIssueImpl(ValidationType.SCHEMATRON, IssueLevel.ERROR,
                            (failedAssertion = ((FailedAssertion) outputContentItem)).getText(),
                            new ValidationLocationImpl(new SdcctLocation(
                                this.xpathCompiler.compile(failedAssertion.getLocation(), new StaticXpathOptionsImpl().setNamespaces(namespaces))
                                    .load(new DynamicXpathOptionsImpl().setContextItem(docInfo)).evaluateNode().getUnderlyingNode())),
                            src)).setContextXpathExpression(contextXpathExpr);
                        issue.setTestXpathExpression(failedAssertion.getTest());
                        issues.add(issue);
                    }
                }
            } catch (Exception e) {
                issues.clear();

                throw new ValidationException(
                    String.format("Unable to validate XML document element (nsPrefix=%s, nsUri=%s, localName=%s) using Schematron (id=%s, name=%s).",
                        docElemInfo.getPrefix(), docElemInfo.getURI(), docElemInfo.getLocalPart(), id, name),
                    e, issues);
            }
        }

        return issues;
    }
}
