package gov.hhs.onc.sdcct.context.impl;

import gov.hhs.onc.sdcct.context.MetadataInitializer;
import gov.hhs.onc.sdcct.context.SdcctPropertyNames;
import java.io.File;
import java.util.Optional;
import javax.annotation.Nullable;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContextException;
import org.springframework.core.env.ConfigurableEnvironment;

public abstract class AbstractMetadataInitializer extends AbstractApplicationInitializer implements MetadataInitializer {
    protected final static String DEFAULT_DATA_DIR_RELATIVE_PATH = "var";

    protected String name;

    protected AbstractMetadataInitializer(SdcctApplication app, String name) {
        super(app);

        this.name = name;
    }

    @Override
    public void initialize(ConfigurableEnvironment env) {
        this.app.setName(this.name);
        this.app.setHomeDirectory(this.buildHomeDirectory(env));
        this.app.setDataDirectory(this.buildDataDirectory(env));
    }

    protected File buildDataDirectory(ConfigurableEnvironment env) {
        String dataDirPath = this.buildDataDirectoryPath(env);

        if (StringUtils.isBlank(dataDirPath)) {
            throw new ApplicationContextException("Unable to determine data directory path.");
        }

        File dataDir = new File(dataDirPath);

        dataDirPath = dataDir.getPath();

        if (!dataDir.exists()) {
            if (!dataDir.mkdirs()) {
                throw new ApplicationContextException(String.format("Unable to create data directory (path=%s).", dataDirPath));
            }
        } else if (!dataDir.isDirectory()) {
            throw new ApplicationContextException(String.format("Data directory path (%s) is not a directory.", dataDirPath));
        }

        return dataDir;
    }

    protected String buildDataDirectoryPath(ConfigurableEnvironment env) {
        return env.getProperty(SdcctPropertyNames.DATA_DIR, new File(this.app.getHomeDirectory(), DEFAULT_DATA_DIR_RELATIVE_PATH).getPath());
    }

    protected File buildHomeDirectory(ConfigurableEnvironment env) {
        String homeDirPath = this.buildHomeDirectoryPath(env);

        if (StringUtils.isBlank(homeDirPath)) {
            throw new ApplicationContextException("Unable to determine application home directory path.");
        }

        return this.buildHomeDirectory(new File(homeDirPath));
    }

    protected File buildHomeDirectory(File homeDir) {
        if (!homeDir.exists()) {
            throw new ApplicationContextException(String.format("Application home directory path (%s) does not exist.", homeDir.getPath()));
        } else if (!homeDir.isDirectory()) {
            throw new ApplicationContextException(String.format("Application home directory path (%s) is not a directory.", homeDir.getPath()));
        }

        return homeDir;
    }

    @Nullable
    protected String buildHomeDirectoryPath(ConfigurableEnvironment env) {
        return Optional.ofNullable(env.getProperty(SdcctPropertyNames.APP_HOME_DIR)).orElse(env.getProperty(SdcctPropertyNames.USER_DIR));
    }
}
