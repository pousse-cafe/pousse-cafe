package poussecafe.property;

public class ProtectedProperty<T> {

    ProtectedProperty(PropertyAccessPolicy policy, Property<T> property) {
        this.policy = policy;
        this.property = property;
    }

    private PropertyAccessPolicy policy;

    private Property<T> property;

    public Property<T> let(Object accessor) {
        return new Property<T>() {
            @Override
            public T get() {
                return property.get();
            }

            @Override
            public void set(T value) {
                if(policy.allowsWriteFor(accessor)) {
                    property.set(value);
                } else {
                    throw new AccessPolicyException();
                }
            }
        };
    }
}
