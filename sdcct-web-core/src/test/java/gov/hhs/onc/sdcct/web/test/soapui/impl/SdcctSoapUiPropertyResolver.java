package gov.hhs.onc.sdcct.web.test.soapui.impl;

import com.eviware.soapui.model.propertyexpansion.PropertyExpansion;
import com.eviware.soapui.model.propertyexpansion.PropertyExpansionContext;
import com.eviware.soapui.model.propertyexpansion.resolvers.PropertyResolver;
import gov.hhs.onc.sdcct.beans.factory.impl.EmbeddedPlaceholderResolver;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Nullable;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("soapUiPropResolver")
public class SdcctSoapUiPropertyResolver implements PropertyResolver {
    private final static String SPRING_REF_PROP_NAME_PREFIX = PropertyExpansion.SCOPE_PREFIX + "Spring" + PropertyExpansion.PROPERTY_SEPARATOR;

    private final static Pattern PROP_PROP_VALUE_PATTERN = Pattern.compile("(\\$)\\(([\\w\\-\\.]+)\\)");
    private final static Pattern EXPR_PROP_VALUE_PATTERN = Pattern.compile("(#)\\(([^$]+)\\)");

    private final static String PLACEHOLDER_PROP_VALUE_REPLACE_PATTERN = "$1{$2}";

    @Autowired
    private EmbeddedPlaceholderResolver embeddedPlaceholderResolver;

    @Nullable
    @Override
    public String resolveProperty(PropertyExpansionContext context, String propName, boolean globalOverride) {
        if (!StringUtils.startsWith(propName, SPRING_REF_PROP_NAME_PREFIX)) {
            return null;
        }

        String propValue = StringUtils.removeStart(propName, SPRING_REF_PROP_NAME_PREFIX);
        Matcher propValueMatcher = PROP_PROP_VALUE_PATTERN.matcher(propValue);

        if (propValueMatcher.matches()) {
            propValue = propValueMatcher.replaceAll(PLACEHOLDER_PROP_VALUE_REPLACE_PATTERN);
        }

        if ((propValueMatcher = EXPR_PROP_VALUE_PATTERN.matcher(propValue)).matches()) {
            propValue = propValueMatcher.replaceAll(PLACEHOLDER_PROP_VALUE_REPLACE_PATTERN);
        }

        return embeddedPlaceholderResolver.resolvePlaceholders(propValue);
    }
}
