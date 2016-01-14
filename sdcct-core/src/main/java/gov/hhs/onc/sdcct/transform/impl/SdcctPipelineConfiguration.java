package gov.hhs.onc.sdcct.transform.impl;

import net.sf.saxon.event.PipelineConfiguration;

public class SdcctPipelineConfiguration extends PipelineConfiguration {
    public SdcctPipelineConfiguration(SdcctConfiguration config) {
        super(config);
    }

    @Override
    public SdcctConfiguration getConfiguration() {
        return ((SdcctConfiguration) super.getConfiguration());
    }
}
