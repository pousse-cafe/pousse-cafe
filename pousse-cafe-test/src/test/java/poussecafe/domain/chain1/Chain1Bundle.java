package poussecafe.domain.chain1;

import poussecafe.discovery.BundleConfigurer;

public class Chain1Bundle {

    public static BundleConfigurer configure() {
        return new BundleConfigurer.Builder()
                .moduleBasePackage("poussecafe.domain.chain1")
                .build();
    }
}
