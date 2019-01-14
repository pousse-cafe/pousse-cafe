package poussecafe.support;

import poussecafe.context.BoundedContextConfigurer;

public class PousseCafeSupport {

    private PousseCafeSupport() {

    }

    public static BoundedContextConfigurer configure() {
        return new BoundedContextConfigurer.Builder()
                .packagePrefix("poussecafe.support")
                .build();
    }
}
