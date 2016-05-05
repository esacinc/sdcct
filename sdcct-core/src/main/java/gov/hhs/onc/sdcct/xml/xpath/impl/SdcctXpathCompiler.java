package gov.hhs.onc.sdcct.xml.xpath.impl;

import gov.hhs.onc.sdcct.transform.impl.SdcctProcessor;
import java.util.Map;
import javax.annotation.Nullable;
import net.sf.saxon.functions.FunctionLibraryList;
import net.sf.saxon.functions.IntegratedFunctionLibrary;
import net.sf.saxon.s9api.QName;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.XPathCompiler;
import net.sf.saxon.s9api.XdmItem;
import net.sf.saxon.s9api.XdmNode;
import net.sf.saxon.s9api.XdmValue;
import net.sf.saxon.sxpath.IndependentContext;
import net.sf.saxon.sxpath.XPathEvaluator;
import net.sf.saxon.trans.XPathException;

public class SdcctXpathCompiler extends XPathCompiler {
    public SdcctXpathCompiler(SdcctProcessor proc) {
        super(proc);
    }

    @Override
    public XdmValue evaluate(String exprStr, @Nullable XdmItem contextItem) throws SaxonApiException {
        return this.compile(exprStr).load(new DynamicXpathOptions().setContextNode(((XdmNode) contextItem))).evaluate();
    }

    @Nullable
    @Override
    public XdmItem evaluateSingle(String exprStr, @Nullable XdmItem contextItem) throws SaxonApiException {
        return this.compile(exprStr).load(new DynamicXpathOptions().setContextNode(((XdmNode) contextItem))).evaluateSingle();
    }

    @Override
    public SdcctXpathExecutable compile(String exprStr) throws SaxonApiException {
        return this.compile(exprStr, null);
    }

    public SdcctXpathExecutable compile(String exprStr, @Nullable StaticXpathOptions staticOpts) throws SaxonApiException {
        SdcctProcessor proc = this.getProcessor();

        IndependentContext staticContext = new IndependentContext(this.getUnderlyingStaticContext());
        staticContext.setAllowUndeclaredVariables(true);

        if (staticOpts != null) {
            if (staticOpts.hasNamespaces()) {
                staticOpts.getNamespaces().forEach(staticContext::declareNamespace);
            }

            if (staticOpts.hasFunctions()) {
                IntegratedFunctionLibrary staticFuncLib = new IntegratedFunctionLibrary();

                staticOpts.getFunctions().forEach(staticFuncLib::registerFunction);

                ((FunctionLibraryList) staticContext.getFunctionLibrary()).addFunctionLibrary(staticFuncLib);
            }

            if (staticOpts.hasVariables()) {
                Map<QName, XdmValue> staticVars = staticOpts.getVariables();

                for (QName staticVarQname : staticVars.keySet()) {
                    staticContext.declareVariable(staticVarQname.getStructuredQName());
                }
            }
        }

        XPathEvaluator evaluator = new XPathEvaluator(proc.getUnderlyingConfiguration());
        evaluator.setStaticContext(staticContext);

        try {
            return new SdcctXpathExecutable(proc, staticOpts, staticContext, evaluator.createExpression(exprStr));
        } catch (XPathException e) {
            throw new SaxonApiException(e);
        }
    }

    @Override
    public SdcctProcessor getProcessor() {
        return ((SdcctProcessor) super.getProcessor());
    }

    @Override
    public IndependentContext getUnderlyingStaticContext() {
        return ((IndependentContext) super.getUnderlyingStaticContext());
    }
}
