package poussecafe.simple;

import poussecafe.discovery.BoundedContextConfigurer;

public class MyBoundedContextConfiguration {

    private MyBoundedContextConfiguration() {

    }

    public static BoundedContextConfigurer configure() {
        return new BoundedContextConfigurer.Builder()
                .packagePrefix("poussecafe.simple")
                .build();
    }
}
