package poussecafe.shop;

import poussecafe.discovery.BundleConfigurer;

public class Shop {

    private Shop() {

    }

    public static BundleConfigurer configure() {
        return new BundleConfigurer.Builder()
                .moduleBasePackage("poussecafe.shop")
                .build();
    }
}
