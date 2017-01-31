package gov.hhs.onc.sdcct.transform.saxon.impl;

import net.sf.saxon.event.PipelineConfiguration;
import net.sf.saxon.lib.ParseOptions;

public class SdcctPipelineConfiguration extends PipelineConfiguration {
    public SdcctPipelineConfiguration(SdcctSaxonConfiguration config) {
        super(config);

        this.setParseOptions(new ParseOptions(config.getParseOptions()));
    }

    @Override
    public SdcctSaxonConfiguration getConfiguration() {
        return ((SdcctSaxonConfiguration) super.getConfiguration());
    }
}
