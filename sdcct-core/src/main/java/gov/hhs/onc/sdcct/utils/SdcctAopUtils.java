package gov.hhs.onc.sdcct.utils;

import java.lang.reflect.Method;
import java.util.function.Predicate;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang3.builder.Builder;
import org.springframework.aop.Advisor;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.MethodMatcher;
import org.springframework.aop.Pointcut;
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;
import org.springframework.aop.support.ComposablePointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.target.SingletonTargetSource;

public final class SdcctAopUtils {
    public static class SdcctSingletonTargetSource extends SingletonTargetSource {
        private final static long serialVersionUID = 0L;

        private Class<?> targetClass;

        public SdcctSingletonTargetSource(Object target, Class<?> targetClass) {
            super(target);

            this.targetClass = targetClass;
        }

        @Override
        public Class<?> getTargetClass() {
            return this.targetClass;
        }
    }

    @FunctionalInterface
    public static interface SdcctMethodInterceptor extends MethodInterceptor {
        @Nullable
        @Override
        public default Object invoke(MethodInvocation invocation) throws Throwable {
            Method method = invocation.getMethod();

            return this.invoke(invocation, method, method.getName(), invocation.getArguments(), invocation.getThis());
        }

        @Nullable
        public Object invoke(MethodInvocation invocation, Method method, String methodName, Object[] args, @Nullable Object target) throws Throwable;
    }

    @FunctionalInterface
    public static interface SdcctStaticMethodMatcher extends MethodMatcher {
        @Override
        default boolean matches(Method method, Class<?> targetClass, Object ... args) {
            return false;
        }

        @Override
        default boolean isRuntime() {
            return false;
        }
    }

    public static class SdcctProxyBuilder<T> implements Builder<T> {
        private AspectJProxyFactory factory = new AspectJProxyFactory();

        public SdcctProxyBuilder(Class<T> targetClass, Object target) {
            this.factory.setTargetSource(new SdcctSingletonTargetSource(target, targetClass));

            if (!targetClass.isInterface()) {
                this.factory.setProxyTargetClass(true);
            }
        }

        @Override
        public T build() {
            return this.factory.getProxy();
        }

        public SdcctProxyBuilder<T> addMethodAdvice(MethodMatcher methodMatcher, SdcctMethodInterceptor advice) {
            return this.addMethodAdvice(null, methodMatcher, advice);
        }

        public SdcctProxyBuilder<T> addMethodAdvice(@Nullable ClassFilter classFilter, @Nullable MethodMatcher methodMatcher, SdcctMethodInterceptor advice) {
            return this.addMethodAdvice(new ComposablePointcut(((classFilter != null) ? classFilter : ClassFilter.TRUE), ((methodMatcher != null)
                ? methodMatcher : MethodMatcher.TRUE)), advice);
        }

        public SdcctProxyBuilder<T> addMethodAdvice(Pointcut pointcut, SdcctMethodInterceptor advice) {
            return this.addAdvisors(new DefaultPointcutAdvisor(pointcut, advice));
        }

        public SdcctProxyBuilder<T> addAdvisors(Advisor ... advisors) {
            this.factory.addAdvisors(advisors);

            return this;
        }

        public AspectJProxyFactory getFactory() {
            return this.factory;
        }
    }

    private SdcctAopUtils() {
    }

    public static SdcctStaticMethodMatcher matchMethodNames(String ... methodNames) {
        return (method, targetClass) -> {
            String methodName = method.getName();

            return Stream.of(methodNames).anyMatch(methodName::equals);
        };
    }

    public static SdcctStaticMethodMatcher matchMethods(Method ... methods) {
        return (method, targetClass) -> Stream.of(methods).anyMatch(method::equals);
    }

    public static SdcctStaticMethodMatcher matchMethods(Predicate<Method> predicate) {
        return (method, targetClass) -> predicate.test(method);
    }
}
