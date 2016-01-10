package gov.hhs.onc.sdcct.json.impl;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.module.SimpleSerializers;
import java.util.Arrays;

public abstract class AbstractSdcctJsonModule extends Module {
    protected String name;
    protected Version version;
    protected JsonSerializer<?>[] serializers;

    protected AbstractSdcctJsonModule(String name, Version version, JsonSerializer<?> ... serializers) {
        this.name = name;
        this.version = version;
        this.serializers = serializers;
    }

    @Override
    public void setupModule(SetupContext setupContext) {
        if (this.serializers.length > 0) {
            setupContext.addSerializers(new SimpleSerializers(Arrays.asList(this.serializers)));
        }
    }

    @Override
    public String getModuleName() {
        return this.name;
    }

    @Override
    public Version version() {
        return this.version;
    }
}
