package gov.hhs.onc.sdcct.context.impl;

import java.text.MessageFormat;
import java.util.Locale;
import javax.annotation.Nullable;
import org.springframework.context.support.AbstractMessageSource;
import org.springframework.core.env.PropertyResolver;

public class PropertyResolvingMessageSource extends AbstractMessageSource {
    private PropertyResolver propResolver;

    public PropertyResolvingMessageSource(PropertyResolver propResolver) {
        this.propResolver = propResolver;

        this.setAlwaysUseMessageFormat(true);
    }

    @Nullable
    @Override
    protected MessageFormat resolveCode(String code, Locale locale) {
        String msg = this.propResolver.resolveRequiredPlaceholders(this.propResolver.getRequiredProperty(code));

        return ((msg != null) ? this.createMessageFormat(msg, Locale.ROOT) : null);
    }
}
