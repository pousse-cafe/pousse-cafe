package poussecafe.domain;

import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;

public class Component {

    public void setComponentFactory(ComponentFactory componentFactory) {
        checkThat(value(componentFactory).notNull());
        this.componentFactory = componentFactory;
    }

    private ComponentFactory componentFactory;

    protected ComponentFactory componentFactory() {
        return componentFactory;
    }

    public <T> T newComponent(Class<T> primitiveClass) {
        return newComponent(
                new ComponentSpecification.Builder<T>().withComponentClass(primitiveClass).withData(true).build());
    }

    public <T, D> T newComponent(Class<T> primitiveClass,
            D data) {
        return newComponent(new ComponentSpecification.Builder<T>()
                .withComponentClass(primitiveClass)
                .withExistingData(data)
                .build());
    }

    public <T> T newComponent(ComponentSpecification<T> specification) {
        return componentFactory.newComponent(specification);
    }
}
