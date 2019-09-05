package poussecafe.domain.chain2;

import poussecafe.discovery.BundleConfigurer;

public class Chain2Bundle {

    public static BundleConfigurer configure() {
        return new BundleConfigurer.Builder()
                .moduleBasePackage("poussecafe.domain.chain2")
                .build();
    }
}
