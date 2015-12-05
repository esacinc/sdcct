package gov.hhs.onc.sdcct.context.impl;

import gov.hhs.onc.sdcct.context.ApplicationInitializer;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.function.Supplier;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.reflect.ConstructorUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContextException;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.core.io.support.SpringFactoriesLoader;

public abstract class AbstractApplicationInitializerRunListener<T extends ApplicationInitializer> extends AbstractSdcctApplicationRunListener {
    protected Class<T> initClass;

    protected AbstractApplicationInitializerRunListener(Class<T> initClass, SpringApplication app, String[] args) {
        super(app, args);

        this.initClass = initClass;
    }

    protected T buildInitializer(Supplier<T> defaultSupplier, Object ... args) {
        List<String> initClassNames = SpringFactoriesLoader.loadFactoryNames(initClass, Thread.currentThread().getContextClassLoader());

        if (initClassNames.isEmpty()) {
            return defaultSupplier.get();
        }

        List<Class<?>> initClasses = ClassUtils.convertClassNamesToClasses(initClassNames);

        initClasses.sort(AnnotationAwareOrderComparator.INSTANCE);

        Class<?> primaryInitClass = initClasses.get(0);

        try {
            return this.initClass.cast(ConstructorUtils.invokeConstructor(primaryInitClass, args));
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException e) {
            throw new ApplicationContextException(String.format("Unable to instantiate application initializer (class=%s).", primaryInitClass.getName()), e);
        }
    }
}
