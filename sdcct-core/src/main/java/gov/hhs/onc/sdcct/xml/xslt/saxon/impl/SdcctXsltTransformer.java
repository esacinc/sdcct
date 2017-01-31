package gov.hhs.onc.sdcct.xml.xslt.saxon.impl;

import gov.hhs.onc.sdcct.transform.saxon.impl.SdcctSaxonController;
import gov.hhs.onc.sdcct.transform.saxon.impl.SdcctSaxonProcessor;
import gov.hhs.onc.sdcct.xml.xslt.DynamicXsltOptions;
import gov.hhs.onc.sdcct.xml.xslt.StaticXsltOptions;
import javax.annotation.Nullable;
import net.sf.saxon.expr.instruct.GlobalParameterSet;
import net.sf.saxon.s9api.XsltTransformer;

public class SdcctXsltTransformer extends XsltTransformer {
    private StaticXsltOptions staticOpts;
    private DynamicXsltOptions dynamicOpts;

    public SdcctXsltTransformer(SdcctSaxonProcessor proc, @Nullable StaticXsltOptions staticOpts, @Nullable DynamicXsltOptions dynamicOpts,
        SdcctSaxonController controller, GlobalParameterSet staticParams) {
        super(proc, controller, staticParams);

        this.staticOpts = staticOpts;
        this.dynamicOpts = dynamicOpts;
    }

    @Override
    public SdcctSaxonController getUnderlyingController() {
        return ((SdcctSaxonController) super.getUnderlyingController());
    }
}
