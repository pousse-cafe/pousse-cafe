package poussecafe.sample;

import poussecafe.discovery.BoundedContextConfigurer;

public class SampleBoundedContextDefinition {

    private SampleBoundedContextDefinition() {

    }

    public static BoundedContextConfigurer configure() {
        return new BoundedContextConfigurer.Builder()
                .packagePrefix("poussecafe.sample")
                .build();
    }
}
