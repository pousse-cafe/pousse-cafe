package poussecafe.environment;

import poussecafe.domain.AggregateRoot;
import poussecafe.domain.Module;

@SuppressWarnings("rawtypes")
public class NamingConvention {

    public static String simpleAggregateName(Class<? extends AggregateRoot> aggregateRootClass) {
        return aggregateRootClass.getSimpleName();
    }

    public static String moduleName(Class<? extends Module> moduleClass) {
        return moduleClass.getSimpleName();
    }

    public static String qualifiedAggregateName(Class<? extends Module> moduleClass,
            Class<? extends AggregateRoot> aggregateRootClass) {
        return moduleName(moduleClass) + "." + simpleAggregateName(aggregateRootClass);
    }

    private NamingConvention() {

    }
}
