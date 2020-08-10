package poussecafe.testmodule2;

import poussecafe.discovery.BundleConfigurer;

public class TestModule2Bundle {

    public static BundleConfigurer configurer() {
        return new BundleConfigurer.Builder()
                .module(TestModule2.class)
                .build();
    }
}
