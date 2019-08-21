package poussecafe.mymodule;

import poussecafe.discovery.BundleConfigurer;

public class MyModule {

    private MyModule() {

    }

    public static BundleConfigurer configure() {
        return new BundleConfigurer.Builder()
                .moduleBasePackage("poussecafe.mymodule")
                .build();
    }
}
