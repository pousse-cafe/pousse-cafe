package poussecafe.domain.chain;

import poussecafe.discovery.BundleConfigurer;

public class ChainBundle {

    public static BundleConfigurer configure() {
        return new BundleConfigurer.Builder()
                .moduleBasePackage("poussecafe.domain.chain")
                .build();
    }
}
