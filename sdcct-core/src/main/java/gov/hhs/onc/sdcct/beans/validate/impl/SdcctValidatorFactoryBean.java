package gov.hhs.onc.sdcct.beans.validate.impl;

import javax.annotation.Nullable;
import javax.validation.Configuration;
import javax.validation.ParameterNameProvider;
import javax.validation.Validator;
import javax.validation.executable.ExecutableValidator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

public class SdcctValidatorFactoryBean extends LocalValidatorFactoryBean {
    private Configuration<?> config;
    private ParameterNameProvider paramNameProv;

    @Nullable
    @Override
    public ExecutableValidator forExecutables() {
        Validator validator = this.getValidator();

        return ((validator instanceof ExecutableValidator) ? ((ExecutableValidator) validator) : null);
    }

    @Override
    protected void postProcessConfiguration(Configuration<?> config) {
        this.config = config;

        if (this.paramNameProv == null) {
            this.paramNameProv = this.config.getDefaultParameterNameProvider();
        }
    }

    public Configuration<?> getConfiguration() {
        return this.config;
    }

    @Override
    public ParameterNameProvider getParameterNameProvider() {
        return this.paramNameProv;
    }

    public void setParameterNameProvider(ParameterNameProvider paramNameProv) {
        this.paramNameProv = paramNameProv;
    }
}
