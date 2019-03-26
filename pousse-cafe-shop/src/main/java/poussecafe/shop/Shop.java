package poussecafe.shop;

import poussecafe.discovery.BoundedContextConfigurer;

public class Shop {

    private Shop() {

    }

    public static BoundedContextConfigurer configure() {
        return new BoundedContextConfigurer.Builder()
                .packagePrefix("poussecafe.shop")
                .build();
    }
}
