package gov.hhs.onc.sdcct.context;

import gov.hhs.onc.sdcct.context.impl.SdcctApplication;
import org.springframework.core.env.ConfigurableEnvironment;

public interface ApplicationInitializer {
    public void initialize(ConfigurableEnvironment env);

    public SdcctApplication getApplication();
}
