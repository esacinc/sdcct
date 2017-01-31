package gov.hhs.onc.sdcct.context.impl;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.support.AbstractMessageSource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.PropertySourcesPropertyResolver;

public class PropertySourcesMessageSource extends AbstractMessageSource implements InitializingBean {
    private MutablePropertySources propSrcs = new MutablePropertySources();
    private PropertySourcesPropertyResolver propSourcesPropResolver;

    @Override
    public void afterPropertiesSet() throws Exception {
        this.propSourcesPropResolver = new PropertySourcesPropertyResolver(this.propSrcs);
    }

    @Nullable
    @Override
    protected MessageFormat resolveCode(String code, Locale locale) {
        String msg = this.propSourcesPropResolver.resolveRequiredPlaceholders(this.propSourcesPropResolver.getRequiredProperty(code));

        return ((msg != null) ? this.createMessageFormat(msg, Locale.ROOT) : null);
    }

    public MutablePropertySources getPropertySources() {
        return this.propSrcs;
    }

    public void setPropertySources(PropertySource<?> ... propSrcs) {
        Stream.of(propSrcs).forEach(propSrc -> this.propSrcs.addLast(propSrc));
    }
}
