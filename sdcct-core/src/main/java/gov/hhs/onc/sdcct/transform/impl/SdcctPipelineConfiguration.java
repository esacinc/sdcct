package gov.hhs.onc.sdcct.transform.impl;

import net.sf.saxon.event.PipelineConfiguration;
import net.sf.saxon.lib.ParseOptions;

public class SdcctPipelineConfiguration extends PipelineConfiguration {
    public SdcctPipelineConfiguration(SdcctConfiguration config) {
        super(config);

        this.setParseOptions(new ParseOptions(config.getParseOptions()));
    }

    @Override
    public SdcctConfiguration getConfiguration() {
        return ((SdcctConfiguration) super.getConfiguration());
    }
}
