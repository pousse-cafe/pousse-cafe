package poussecafe.domain.chain3;

import poussecafe.discovery.BundleConfigurer;

public class Chain3Bundle {

    public static BundleConfigurer configure() {
        return new BundleConfigurer.Builder()
                .moduleBasePackage("poussecafe.domain.chain3")
                .build();
    }
}
