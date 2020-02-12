package poussecafe.doc;

import poussecafe.discovery.BundleConfigurer;

public class PousseCafeDocBundle {

    private PousseCafeDocBundle() {

    }

    public static BundleConfigurer configure() {
        return new BundleConfigurer.Builder()
                .module(PousseCafeDoc.class)
                .build();
    }
}
