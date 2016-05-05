package gov.hhs.onc.sdcct.xml.xpath.impl;

import gov.hhs.onc.sdcct.xml.utils.SdcctXmlUtils;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import javax.xml.transform.URIResolver;
import net.sf.saxon.om.Item;
import net.sf.saxon.om.SequenceIterator;
import net.sf.saxon.om.StructuredQName;
import net.sf.saxon.s9api.QName;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.SaxonApiUncheckedException;
import net.sf.saxon.s9api.XPathSelector;
import net.sf.saxon.s9api.XdmAtomicValue;
import net.sf.saxon.s9api.XdmItem;
import net.sf.saxon.s9api.XdmNode;
import net.sf.saxon.s9api.XdmSequenceIterator;
import net.sf.saxon.s9api.XdmValue;
import net.sf.saxon.sxpath.XPathDynamicContext;
import net.sf.saxon.sxpath.XPathExpression;
import net.sf.saxon.sxpath.XPathVariable;
import net.sf.saxon.trans.XPathException;
import net.sf.saxon.value.SequenceExtent;
import org.apache.commons.collections4.IterableUtils;

public class SdcctXpathSelector extends XPathSelector {
    public static class SdcctXdmSequenceIterator extends XdmSequenceIterator {
        private SequenceIterator seqIterator;

        public SdcctXdmSequenceIterator(SequenceIterator seqIterator) {
            super(seqIterator);

            this.seqIterator = seqIterator;
        }

        public SequenceIterator getSequenceIterator() {
            return this.seqIterator;
        }
    }

    private XPathExpression expr;
    private StaticXpathOptions staticOpts;
    private DynamicXpathOptions dynamicOpts;
    private XPathDynamicContext dynamicContext;
    private Map<StructuredQName, XPathVariable> declaredVars;

    public SdcctXpathSelector(XPathExpression expr, StaticXpathOptions staticOpts, DynamicXpathOptions dynamicOpts, XPathDynamicContext dynamicContext,
        Map<StructuredQName, XPathVariable> declaredVars) {
        super(expr, declaredVars);

        this.expr = expr;
        this.staticOpts = staticOpts;
        this.dynamicOpts = dynamicOpts;
        this.dynamicContext = dynamicContext;
        this.declaredVars = declaredVars;
    }

    @Override
    public boolean effectiveBooleanValue() throws SaxonApiException {
        try {
            return this.expr.effectiveBooleanValue(this.dynamicContext);
        } catch (XPathException e) {
            throw new SaxonApiException(e);
        }
    }

    public List<XdmNode> evaluateNodes() throws SaxonApiException {
        return SdcctXmlUtils.streamNodes(this.iterator()).collect(Collectors.toList());
    }

    public List<String> evaluateStrings() throws SaxonApiException {
        return SdcctXmlUtils.streamStrings(this.iterator()).collect(Collectors.toList());
    }

    public List<XdmAtomicValue> evaluateAtomicValues() throws SaxonApiException {
        return SdcctXmlUtils.streamAtomicValues(this.iterator()).collect(Collectors.toList());
    }

    @Nullable
    public XdmNode evaluateNode() throws SaxonApiException {
        return ((XdmNode) this.evaluateSingle());
    }

    @Nullable
    public String evaluateString() throws SaxonApiException {
        return Optional.ofNullable(this.evaluateAtomicValue()).map(XdmItem::getStringValue).orElse(null);
    }

    @Nullable
    public XdmAtomicValue evaluateAtomicValue() throws SaxonApiException {
        return ((XdmAtomicValue) this.evaluateSingle());
    }

    public List<XdmItem> evaluateItems() throws SaxonApiException {
        return IterableUtils.toList(this);
    }

    @Override
    public SdcctXdmSequenceIterator iterator() {
        try {
            return new SdcctXdmSequenceIterator(this.expr.iterate(this.dynamicContext));
        } catch (XPathException e) {
            throw new SaxonApiUncheckedException(e);
        }
    }

    @Override
    public XdmValue evaluate() throws SaxonApiException {
        try {
            return XdmValue.wrap(SequenceExtent.makeSequenceExtent(this.expr.iterate(this.dynamicContext)));
        } catch (XPathException e) {
            throw new SaxonApiException(e);
        }
    }

    @Nullable
    @Override
    public XdmItem evaluateSingle() throws SaxonApiException {
        try {
            return Optional.ofNullable(this.expr.evaluateSingle(this.dynamicContext)).map(value -> ((XdmItem) XdmItem.wrap(value))).orElse(null);
        } catch (XPathException e) {
            throw new SaxonApiException(e);
        }
    }

    public boolean hasContextItem() {
        return (this.getContextItem() != null);
    }

    @Nullable
    @Override
    public XdmItem getContextItem() {
        return Optional.ofNullable(this.dynamicContext.getContextItem()).map(contextItem -> ((XdmItem) XdmItem.wrap(contextItem))).orElse(null);
    }

    @Override
    public void setContextItem(@Nullable XdmItem contextItem) throws SaxonApiException {
        try {
            this.dynamicContext.setContextItem(((Item) Optional.ofNullable(contextItem).map(XdmItem::getUnderlyingValue).orElse(null)));
        } catch (XPathException e) {
            throw new SaxonApiException(e);
        }
    }

    @Nullable
    @Override
    public URIResolver getURIResolver() {
        return this.dynamicContext.getURIResolver();
    }

    @Override
    public void setURIResolver(@Nullable URIResolver uriResolver) {
        this.dynamicContext.setURIResolver(uriResolver);
    }

    @Override
    public void setVariable(QName qname, XdmValue value) throws SaxonApiException {
        try {
            this.dynamicContext.setVariable(this.declaredVars.get(qname.getStructuredQName()), value.getUnderlyingValue());
        } catch (XPathException e) {
            throw new SaxonApiException(e);
        }
    }

    public DynamicXpathOptions getDynamicOptions() {
        return this.dynamicOpts;
    }

    public StaticXpathOptions getStaticOptions() {
        return this.staticOpts;
    }

    @Override
    public XPathDynamicContext getUnderlyingXPathContext() {
        return this.dynamicContext;
    }
}
