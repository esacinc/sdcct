package gov.hhs.onc.sdcct.utils;

import java.lang.reflect.Method;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.aop.MethodMatcher;

public final class SdcctMethodUtils {
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

    private SdcctMethodUtils() {
    }

    public static SdcctStaticMethodMatcher matchName(String ... names) {
        final Set<String> nameSet = Stream.of(names).collect(Collectors.toSet());

        return matchName(nameSet::contains);
    }

    public static SdcctStaticMethodMatcher matchName(Predicate<String> predicate) {
        return (method, targetClass) -> predicate.test(method.getName());
    }

    public static SdcctStaticMethodMatcher matchReturnType(Class<?> returnClass, boolean assignable) {
        final Predicate<Class<?>> predicate = (assignable ? returnClass::isAssignableFrom : returnClass::equals);

        return (method, targetClass) -> predicate.test(method.getReturnType());
    }

    public static SdcctStaticMethodMatcher match(Method ... methods) {
        final Set<Method> methodSet = Stream.of(methods).collect(Collectors.toSet());

        return match(methodSet::contains);
    }

    public static SdcctStaticMethodMatcher match(Predicate<Method> predicate) {
        return (method, targetClass) -> predicate.test(method);
    }
}
