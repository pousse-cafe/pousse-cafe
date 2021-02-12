package poussecafe.discovery;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Set;
import org.reflections.Reflections;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

public class ReflectionsWrapper {

    public ReflectionsWrapper(Collection<String> basePackages) {
        ConfigurationBuilder reflectionsConfigurationBuilder = new ConfigurationBuilder();
        FilterBuilder filter = new FilterBuilder();
        ClassLoader[] classLoaders = null;
        for(String basePackage : basePackages) {
            validOrThrow(basePackage);
            String packageRootPrefix = basePackage + ".";
            filter.includePackage(packageRootPrefix);
            reflectionsConfigurationBuilder.addUrls(ClasspathHelper.forPackage(packageRootPrefix, classLoaders));
        }
        filter.exclude(".*\\.java");

        reflectionsConfigurationBuilder.filterInputsBy(filter);
        reflectionsConfigurationBuilder.setExpandSuperTypes(false);
        reflections = new Reflections(reflectionsConfigurationBuilder);
    }

    private void validOrThrow(String basePackage) {
        if(!basePackage.matches(VALID_PACKAGE_NAME_REGEX)) {
            throw new IllegalArgumentException("Invalid package name " + basePackage);
        }
    }

    private static final String VALID_PACKAGE_NAME_REGEX = "^[a-zA-Z][a-zA-Z0-9_]*(\\.[a-zA-Z][a-zA-Z0-9_]*)*$";

    private Reflections reflections;

    public Set<Class<?>> getTypesAnnotatedWith(Class<? extends Annotation> annotationClass) {
        return reflections.getTypesAnnotatedWith(annotationClass);
    }

    public <T> Set<Class<? extends T>> getSubTypesOf(Class<T> type) {
        return reflections.getSubTypesOf(type);
    }
}
