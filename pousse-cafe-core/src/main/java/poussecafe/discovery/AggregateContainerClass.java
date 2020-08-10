package poussecafe.discovery;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import poussecafe.domain.AggregateRoot;
import poussecafe.domain.Factory;
import poussecafe.domain.Repository;

import static java.util.stream.Collectors.toList;

@SuppressWarnings("rawtypes")
public class AggregateContainerClass {

    public AggregateContainerClass(Class<?> containerClass) {
        this.containerClass = containerClass;
    }

    private Class<?> containerClass;

    public Class aggregateRootClass() {
        return innerClassAssignableFrom(AggregateRoot.class);
    }

    public Class innerClassAssignableFrom(Class parentClass) {
        @SuppressWarnings("unchecked")
        List<Class> aggregateRootClasses = innerClasses()
                .filter(parentClass::isAssignableFrom)
                .collect(toList());
        if(aggregateRootClasses.size() != 1) {
            throw new IllegalStateException("Aggregate container class does not declare a single aggregate root");
        } else {
            return aggregateRootClasses.get(0);
        }
    }

    private Stream<Class> innerClasses() {
        return Arrays.stream(containerClass.getDeclaredClasses());
    }

    public Class<?> factoryClass() {
        return innerClassAssignableFrom(Factory.class);
    }

    public Class<?> repositoryClass() {
        return innerClassAssignableFrom(Repository.class);
    }
}
