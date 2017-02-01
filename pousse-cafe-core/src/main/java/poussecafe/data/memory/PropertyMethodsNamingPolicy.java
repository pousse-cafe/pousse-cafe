package poussecafe.data.memory;

import java.util.HashSet;
import java.util.Set;

import static java.util.Arrays.asList;

class PropertyMethodsNamingPolicy {

    private static final Set<String> BLACKLISTED_PROPERTIES = new HashSet<>(
            asList("class", "proxyClass", "invocationHandler"));

    public boolean isSetterOrGetter(String methodName) {
        return isSetter(methodName) || isGetter(methodName);
    }

    public boolean isSetter(String methodName) {
        return isSetterOrGetter(methodName, "set");
    }

    private boolean isSetterOrGetter(String methodName,
            String prefix) {
        return !isBlackListed(methodName) && (methodName.length() > prefix.length() && methodName.startsWith(prefix));
    }

    public boolean isGetter(String methodName) {
        return isSetterOrGetter(methodName, "get");
    }

    public String extractPropertyName(String methodName) {
        return methodName.substring(3, 4).toLowerCase() + methodName.substring(4);
    }

    private boolean isBlackListed(String propertyName) {
        return BLACKLISTED_PROPERTIES.contains(propertyName);
    }
}
