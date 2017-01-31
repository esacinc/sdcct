package gov.hhs.onc.sdcct.xml.xslt.saxon.impl;

import gov.hhs.onc.sdcct.transform.saxon.impl.SdcctSaxonController;
import gov.hhs.onc.sdcct.transform.saxon.impl.SdcctSaxonProcessor;
import gov.hhs.onc.sdcct.xml.xslt.DynamicXsltOptions;
import gov.hhs.onc.sdcct.xml.xslt.StaticXsltOptions;
import java.util.Map;
import javax.annotation.Nullable;
import net.sf.saxon.PreparedStylesheet;
import net.sf.saxon.s9api.QName;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.SaxonApiUncheckedException;
import net.sf.saxon.s9api.XdmValue;
import net.sf.saxon.s9api.XsltExecutable;
import net.sf.saxon.trans.CompilerInfo;

public class SdcctXsltExecutable extends XsltExecutable {
    private CompilerInfo compilerInfo;
    private StaticXsltOptions staticOpts;

    public SdcctXsltExecutable(SdcctSaxonProcessor proc, CompilerInfo compilerInfo, @Nullable StaticXsltOptions staticOpts, PreparedStylesheet preparedStylesheet) {
        super(proc, preparedStylesheet);

        this.compilerInfo = compilerInfo;
        this.staticOpts = staticOpts;
    }

    @Override
    public SdcctXsltTransformer load() {
        return this.load(null);
    }

    public SdcctXsltTransformer load(@Nullable DynamicXsltOptions dynamicOpts) {
        SdcctSaxonProcessor proc = this.getProcessor();
        PreparedStylesheet preparedStylesheet = this.getUnderlyingCompiledStylesheet();

        SdcctSaxonController controller = new SdcctSaxonController(proc.getUnderlyingConfiguration(), preparedStylesheet);
        controller.setMessageReceiverClassName(this.compilerInfo.getMessageReceiverClassName());
        controller.setOutputURIResolver(this.compilerInfo.getOutputURIResolver());
        controller.setRecoveryPolicy(this.compilerInfo.getRecoveryPolicy());

        SdcctXsltTransformer transformer =
            new SdcctXsltTransformer(this.getProcessor(), this.staticOpts, dynamicOpts, controller, preparedStylesheet.getCompileTimeParams());

        if (dynamicOpts != null) {
            if (dynamicOpts.hasSource()) {
                try {
                    transformer.setSource(dynamicOpts.getSource());
                } catch (SaxonApiException e) {
                    throw new SaxonApiUncheckedException(e);
                }
            }

            if (dynamicOpts.hasDestination()) {
                transformer.setDestination(dynamicOpts.getDestination());
            }

            if (dynamicOpts.hasVariables()) {
                Map<QName, XdmValue> dynamicVars = dynamicOpts.getVariables();

                for (QName dynamicVarQname : dynamicVars.keySet()) {
                    transformer.setParameter(dynamicVarQname, dynamicVars.get(dynamicVarQname));
                }
            }
        }

        return transformer;
    }

    @Override
    public SdcctSaxonProcessor getProcessor() {
        return ((SdcctSaxonProcessor) super.getProcessor());
    }
}
