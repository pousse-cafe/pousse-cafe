package poussecafe.property;

public class ConfigurableProtectedProperty<T> {

    ConfigurableProtectedProperty(Object container, Property<T> property) {
        this.container = container;
        this.property = property;
        policy = new PropertyAccessPolicy();
    }

    private Object container;

    private PropertyAccessPolicy policy;

    private Property<T> property;

    public ConfigurableProtectedProperty<T> allowPackageWrite(boolean allowPackageWrite) {
        if(allowPackageWrite) {
            addWriteRule(new PackageAccessRule(container.getClass().getPackage()));
        }
        return this;
    }

    private ConfigurableProtectedProperty<T> addWriteRule(AllowRule accessRule) {
        policy.addWriteRule(accessRule);
        return this;
    }

    public ConfigurableProtectedProperty<T> allowClassWrite(Class<?> allowedClass) {
        policy.addWriteRule(new ClassAccessRule(allowedClass));
        return this;
    }

    public ProtectedProperty<T> build() {
        return new ProtectedProperty<>(policy, property);
    }
}
