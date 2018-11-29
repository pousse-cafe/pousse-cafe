package poussecafe.simplemetaapp;

import poussecafe.context.BoundedContextConfigurer;

public class MyBoundedContextConfiguration {

    private MyBoundedContextConfiguration() {

    }

    public static BoundedContextConfigurer configure() {
        return new BoundedContextConfigurer.Builder()
                .packagePrefix("poussecafe.simplemetaapp")
                .build();
    }
}
