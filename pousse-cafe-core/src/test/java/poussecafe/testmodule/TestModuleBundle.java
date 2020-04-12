package poussecafe.testmodule;

import poussecafe.discovery.BundleConfigurer;

public class TestModuleBundle {

    public static BundleConfigurer configurer() {
        return new BundleConfigurer.Builder()
                .module(TestModule.class)
                .build();
    }
}
