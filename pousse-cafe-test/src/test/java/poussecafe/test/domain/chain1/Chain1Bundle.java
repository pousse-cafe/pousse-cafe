package poussecafe.test.domain.chain1;

import poussecafe.discovery.BundleConfigurer;

public class Chain1Bundle {

    public static BundleConfigurer configure() {
        return new BundleConfigurer.Builder()
                .module(Chain1.class)
                .build();
    }
}
