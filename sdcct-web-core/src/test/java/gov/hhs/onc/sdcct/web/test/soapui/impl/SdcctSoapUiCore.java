package gov.hhs.onc.sdcct.web.test.soapui.impl;

import com.eviware.soapui.DefaultSoapUICore;
import com.eviware.soapui.SoapUIExtensionClassLoader;
import com.eviware.soapui.model.ModelItem;
import com.eviware.soapui.support.scripting.SoapUIScriptEngine;
import com.eviware.soapui.support.scripting.SoapUIScriptEngineRegistry;
import com.eviware.soapui.support.scripting.groovy.GroovyScriptEngineFactory;
import com.eviware.soapui.support.scripting.groovy.SoapUIGroovyScriptEngine;
import gov.hhs.onc.sdcct.utils.SdcctStreamUtils;
import groovy.lang.Binding;
import java.io.File;
import java.net.URL;
import java.util.Map;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.InitializingBean;

public class SdcctSoapUiCore extends DefaultSoapUICore implements BeanClassLoaderAware, InitializingBean {
    private static class SdcctSoapUiGroovyScriptEngine extends SoapUIGroovyScriptEngine {
        private final static Map<String, Object> DEFAULT_SCRIPT_VARS =
            Stream.of(new ImmutablePair<>("helper", SdcctSoapUiHelper.INSTANCE)).collect(SdcctStreamUtils.toMap());

        public SdcctSoapUiGroovyScriptEngine(ClassLoader parentClassLoader) {
            super(parentClassLoader);
        }

        @Override
        public synchronized void compile() throws Exception {
            super.compile();

            Binding binding = this.getBinding();

            DEFAULT_SCRIPT_VARS.forEach(binding::setVariable);
        }
    }

    private class SdcctSoapUiGroovyScriptEngineFactory extends GroovyScriptEngineFactory {
        @Override
        public SoapUIScriptEngine createScriptEngine(ModelItem modelItem) {
            return new SdcctSoapUiGroovyScriptEngine(SdcctSoapUiCore.this.extClassLoader);
        }
    }

    private ClassLoader beanClassLoader;
    private SoapUIExtensionClassLoader extClassLoader;

    public SdcctSoapUiCore(File rootDir, File settingsFile) {
        super(rootDir.getAbsolutePath());

        this.setSettingsFile(settingsFile.getAbsolutePath());
    }

    @Nullable
    @Override
    public String saveSettings() throws Exception {
        return null;
    }

    @Override
    public synchronized void loadExternalLibraries() {
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.extClassLoader = new SoapUIExtensionClassLoader(new URL[0], this.beanClassLoader);

        this.init(this.getSettingsFile());

        SoapUIScriptEngineRegistry.registerScriptEngine(GroovyScriptEngineFactory.ID, new SdcctSoapUiGroovyScriptEngineFactory());
    }

    @Override
    public void setBeanClassLoader(ClassLoader beanClassLoader) {
        this.beanClassLoader = beanClassLoader;
    }

    @Override
    public SoapUIExtensionClassLoader getExtensionClassLoader() {
        return this.extClassLoader;
    }
}
