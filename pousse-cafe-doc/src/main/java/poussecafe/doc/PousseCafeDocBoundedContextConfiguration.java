package poussecafe.doc;

import poussecafe.context.BoundedContextConfigurer;

public class PousseCafeDocBoundedContextConfiguration {

    private PousseCafeDocBoundedContextConfiguration() {

    }

    public static BoundedContextConfigurer configure() {
        return new BoundedContextConfigurer.Builder()
                .packagePrefix("poussecafe.doc")
                .build();
    }
}
