package gov.hhs.onc.sdcct.beans.factory.impl;

import java.util.Objects;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanExpressionContext;
import org.springframework.beans.factory.config.BeanExpressionResolver;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.config.PlaceholderConfigurerSupport;
import org.springframework.stereotype.Component;

@Component("embeddedPlaceholderResolver")
public class EmbeddedPlaceholderResolver implements BeanFactoryAware, InitializingBean {
    private ConfigurableBeanFactory beanFactory;
    private BeanExpressionResolver beanExprResolver;
    private BeanExpressionContext beanExprContext;

    public String resolvePlaceholders(String str) {
        return this.resolvePlaceholders(str, false);
    }

    public String resolvePlaceholders(String str, boolean asPropName) {
        if (asPropName) {
            String strResolving = PlaceholderConfigurerSupport.DEFAULT_PLACEHOLDER_PREFIX + str + PlaceholderConfigurerSupport.DEFAULT_PLACEHOLDER_SUFFIX,
                strResolved = this.beanFactory.resolveEmbeddedValue(strResolving);

            if (!strResolved.equals(strResolving)) {
                str = strResolved;
            }
        } else {
            str = this.beanFactory.resolveEmbeddedValue(str);
        }

        return Objects.toString(this.beanExprResolver.evaluate(str, this.beanExprContext), null);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.beanExprResolver = this.beanFactory.getBeanExpressionResolver();
        this.beanExprContext = new BeanExpressionContext(this.beanFactory, null);
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = ((ConfigurableBeanFactory) beanFactory);
    }
}
