package poussecafe.mymodule;

import poussecafe.discovery.BundleConfigurer;

public class MyModuleBundle {

    private MyModuleBundle() {

    }

    public static BundleConfigurer configure() {
        return new BundleConfigurer.Builder()
                .module(MyModule.class)
                .build();
    }
}
