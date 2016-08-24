package gov.hhs.onc.sdcct.xml.html.impl;

import gov.hhs.onc.sdcct.xml.html.HtmlTranscodeOptions;
import gov.hhs.onc.sdcct.xml.impl.AbstractDynamicXmlTransformOptions;

public class HtmlTranscodeOptionsImpl extends AbstractDynamicXmlTransformOptions<HtmlTranscodeOptions> implements HtmlTranscodeOptions {
    private final static long serialVersionUID = 0L;

    @Override
    protected HtmlTranscodeOptions cloneInternal() {
        return new HtmlTranscodeOptionsImpl();
    }
}
