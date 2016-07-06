package gov.hhs.onc.sdcct.xml.xslt.impl;

import gov.hhs.onc.sdcct.transform.impl.SdcctController;
import gov.hhs.onc.sdcct.transform.impl.SdcctProcessor;
import gov.hhs.onc.sdcct.xml.xslt.DynamicXsltOptions;
import gov.hhs.onc.sdcct.xml.xslt.StaticXsltOptions;
import javax.annotation.Nullable;
import net.sf.saxon.expr.instruct.GlobalParameterSet;
import net.sf.saxon.s9api.XsltTransformer;

public class SdcctXsltTransformer extends XsltTransformer {
    private StaticXsltOptions staticOpts;
    private DynamicXsltOptions dynamicOpts;

    public SdcctXsltTransformer(SdcctProcessor proc, @Nullable StaticXsltOptions staticOpts, @Nullable DynamicXsltOptions dynamicOpts,
        SdcctController controller, GlobalParameterSet staticParams) {
        super(proc, controller, staticParams);

        this.staticOpts = staticOpts;
        this.dynamicOpts = dynamicOpts;
    }

    @Override
    public SdcctController getUnderlyingController() {
        return ((SdcctController) super.getUnderlyingController());
    }
}
