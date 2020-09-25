package poussecafe.test.domain.chain2;

import poussecafe.discovery.BundleConfigurer;

public class Chain2Bundle {

    public static BundleConfigurer configure() {
        return new BundleConfigurer.Builder()
                .module(Chain2.class)
                .build();
    }
}
