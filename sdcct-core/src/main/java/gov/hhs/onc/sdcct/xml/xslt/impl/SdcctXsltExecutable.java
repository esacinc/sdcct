package gov.hhs.onc.sdcct.xml.xslt.impl;

import gov.hhs.onc.sdcct.transform.impl.SdcctController;
import gov.hhs.onc.sdcct.transform.impl.SdcctProcessor;
import gov.hhs.onc.sdcct.xml.xslt.DynamicXsltOptions;
import gov.hhs.onc.sdcct.xml.xslt.StaticXsltOptions;
import java.util.Map;
import javax.annotation.Nullable;
import net.sf.saxon.PreparedStylesheet;
import net.sf.saxon.s9api.QName;
import net.sf.saxon.s9api.XdmValue;
import net.sf.saxon.s9api.XsltExecutable;
import net.sf.saxon.trans.CompilerInfo;

public class SdcctXsltExecutable extends XsltExecutable {
    private CompilerInfo compilerInfo;
    private StaticXsltOptions staticOpts;

    public SdcctXsltExecutable(SdcctProcessor proc, CompilerInfo compilerInfo, @Nullable StaticXsltOptions staticOpts, PreparedStylesheet preparedStylesheet) {
        super(proc, preparedStylesheet);

        this.compilerInfo = compilerInfo;
        this.staticOpts = staticOpts;
    }

    @Override
    public SdcctXsltTransformer load() {
        return this.load(null);
    }

    public SdcctXsltTransformer load(@Nullable DynamicXsltOptions dynamicOpts) {
        SdcctProcessor proc = this.getProcessor();
        PreparedStylesheet preparedStylesheet = this.getUnderlyingCompiledStylesheet();

        SdcctController controller = new SdcctController(proc.getUnderlyingConfiguration(), preparedStylesheet);
        controller.setMessageReceiverClassName(this.compilerInfo.getMessageReceiverClassName());
        controller.setOutputURIResolver(this.compilerInfo.getOutputURIResolver());
        controller.setRecoveryPolicy(this.compilerInfo.getRecoveryPolicy());

        SdcctXsltTransformer transformer =
            new SdcctXsltTransformer(this.getProcessor(), this.staticOpts, dynamicOpts, controller, preparedStylesheet.getCompileTimeParams());

        if ((dynamicOpts != null) && (dynamicOpts.hasVariables())) {
            Map<QName, XdmValue> dynamicVars = dynamicOpts.getVariables();

            for (QName dynamicVarQname : dynamicVars.keySet()) {
                transformer.setParameter(dynamicVarQname, dynamicVars.get(dynamicVarQname));
            }
        }

        return transformer;
    }

    @Override
    public SdcctProcessor getProcessor() {
        return ((SdcctProcessor) super.getProcessor());
    }
}
