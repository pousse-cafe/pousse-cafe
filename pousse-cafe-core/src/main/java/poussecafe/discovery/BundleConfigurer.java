package poussecafe.discovery;

import java.util.HashSet;
import java.util.Set;
import poussecafe.domain.Module;
import poussecafe.environment.Bundle;
import poussecafe.environment.BundleDefinition;
import poussecafe.messaging.Messaging;
import poussecafe.messaging.internal.InternalMessaging;
import poussecafe.runtime.MessagingAndStorage;
import poussecafe.storage.Storage;
import poussecafe.storage.internal.InternalStorage;

public class BundleConfigurer {

    public static class Builder {

        private BundleConfigurer configurer = new BundleConfigurer();

        /**
         * @deprecated use module method instead.
         */
        @Deprecated(since = "0.17")
        public Builder moduleBasePackage(String packageName) {
            basePackages.add(packageName);
            return this;
        }

        private Set<String> basePackages = new HashSet<>();

        public Builder module(Class<? extends Module> moduleClass) {
            if(configurer.moduleClasses.add(moduleClass)) {
                basePackages.add(moduleClass.getPackageName());
            }
            return this;
        }

        public Builder basePackage(String basePackage) {
            basePackageOf(basePackage, DefaultModule.class);
            return this;
        }

        public Builder basePackageOf(String basePackage, Class<? extends Module> module) {
            basePackages.add(basePackage);
            configurer.moduleClasses.add(module);
            return this;
        }

        public Builder basePackageClass(Class<?> basePackageClass) {
            basePackageClassOf(basePackageClass, DefaultModule.class);
            return this;
        }

        public Builder basePackageClassOf(Class<?> basePackageClass, Class<? extends Module> module) {
            basePackageOf(basePackageClass.getPackageName(), module);
            return this;
        }

        public BundleConfigurer build() {
            if(basePackages.isEmpty()) {
                module(DefaultModule.class);
            }
            configurer.classPathExplorer = new ClassPathExplorer(basePackages);
            return configurer;
        }
    }

    private BundleConfigurer() {

    }

    private ClassPathExplorer classPathExplorer;

    public BundleDefinition define() {
        BundleDefinition.Builder builder = new BundleDefinition.Builder();
        for(Class<? extends Module> moduleClass : moduleClasses) {
            builder.withAggregateDefinitions(classPathExplorer.discoverAggregates(moduleClass));
            builder.withDomainProcesses(classPathExplorer.discoverDomainProcesses(moduleClass));
            builder.withServices(classPathExplorer.discoverServices(moduleClass));
            builder.withMessages(classPathExplorer.discoverMessages(moduleClass));
            builder.withMessageListeners(classPathExplorer.discoverListeners(moduleClass));
        }
        return builder.build();
    }

    private Set<Class<? extends Module>> moduleClasses = new HashSet<>();

    public Bundle.Builder defineAndImplementDefault() {
        return defineThenImplement()
                .messaging(InternalMessaging.instance())
                .storage(InternalStorage.instance());
    }

    public BundleWithoutImplementation defineThenImplement() {
        Bundle.Builder builder = new Bundle.Builder();
        builder.definition(define());
        for(Class<? extends Module> moduleClass : moduleClasses) {
            builder.serviceImplementations(classPathExplorer.discoverServiceImplementations(moduleClass));
        }
        return new BundleWithoutImplementation(builder);
    }

    public class BundleWithoutImplementation {

        private BundleWithoutImplementation(Bundle.Builder builder) {
            this.builder = builder;
        }

        private Bundle.Builder builder;

        public Bundle.Builder messagingAndStorage(MessagingAndStorage messagingAndStorage) {
            return messaging(messagingAndStorage.messaging())
                    .storage(messagingAndStorage.storage());
        }

        public BundleWithMessaging messaging(Messaging messaging) {
            builder.messagingImplementations(new MessagingImplementationDiscovery.Builder()
                    .classPathExplorer(classPathExplorer)
                    .messaging(messaging)
                    .build()
                    .discover());
            return new BundleWithMessaging(builder);
        }
    }

    public class BundleWithMessaging {

        private BundleWithMessaging(Bundle.Builder builder) {
            this.builder = builder;
        }

        private Bundle.Builder builder;

        public Bundle.Builder storage(Storage storage) {
            builder.storageImplementations(new StorageImplementationDiscovery.Builder()
                    .classPathExplorer(classPathExplorer)
                    .storage(storage)
                    .build()
                    .discover());
            return builder;
        }
    }
}
