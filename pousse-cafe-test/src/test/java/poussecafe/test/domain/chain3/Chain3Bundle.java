package poussecafe.test.domain.chain3;

import poussecafe.discovery.BundleConfigurer;

public class Chain3Bundle {

    public static BundleConfigurer configure() {
        return new BundleConfigurer.Builder()
                .module(Chain3.class)
                .build();
    }
}
