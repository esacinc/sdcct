package gov.hhs.onc.sdcct.utils;

import com.github.sebhoss.warnings.CompilerWarnings;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.lang3.ClassUtils;

public final class SdcctClassUtils {
    public final static String IMPL_PKG_NAME = "impl";

    public final static String ABSTRACT_CLASS_NAME_PREFIX = "Abstract";

    public final static String IMPL_CLASS_NAME_SUFFIX = "Impl";

    public final static Set<Class<?>> BOOLEAN_CLASSES = Stream.of(Boolean.TYPE, Boolean.class).collect(Collectors.toSet());
    public final static Set<String> BOOLEAN_CLASS_NAMES = BOOLEAN_CLASSES.stream().map(Class::getName).collect(Collectors.toSet());

    private SdcctClassUtils() {
    }

    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    public static <T, U extends T> Class<? extends U> buildImplClass(ClassLoader classLoader, Class<U> superClass, Class<? extends T> interfaceClass)
        throws ClassNotFoundException {
        String interfacePkgName = interfaceClass.getPackage().getName(), interfaceClassSimpleName = interfaceClass.getSimpleName(),
            implClassName = buildImplClassName(interfacePkgName, interfaceClassSimpleName, true);

        if (!org.springframework.util.ClassUtils.isPresent(implClassName, classLoader)) {
            implClassName = buildImplClassName(interfacePkgName, interfaceClassSimpleName, false);
        }

        return ((Class<? extends U>) Class.forName(implClassName, true, classLoader));
    }

    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    public static <T> Class<? extends T> buildInterfaceClass(ClassLoader classLoader, Class<T> superInterface, Package pkg, String simpleName)
        throws ClassNotFoundException {
        return ((Class<? extends T>) Class.forName(buildInterfaceClassName(pkg, simpleName), true, classLoader));
    }

    public static String buildImplClassName(String interfacePkgName, String interfaceClassSimpleName, boolean abztract) {
        String pkgName = buildImplPackageName(interfacePkgName);

        return (abztract
            ? buildClassName(pkgName, (ABSTRACT_CLASS_NAME_PREFIX + interfaceClassSimpleName))
            : buildClassName(pkgName, (interfaceClassSimpleName + IMPL_CLASS_NAME_SUFFIX)));
    }

    public static String buildInterfaceClassName(Package pkg, String simpleName) {
        return buildClassName(pkg.getName(), simpleName);
    }

    public static String buildClassName(String pkgName, String simpleName) {
        return (pkgName + ClassUtils.PACKAGE_SEPARATOR + simpleName);
    }

    public static Package buildImplPackage(Package pkg) {
        return Package.getPackage(buildImplPackageName(pkg.getName()));
    }

    public static String buildImplPackageName(String pkgName) {
        return (pkgName + ClassUtils.PACKAGE_SEPARATOR + IMPL_PKG_NAME);
    }
}
