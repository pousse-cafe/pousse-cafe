package poussecafe.journal;

import poussecafe.context.BoundedContextConfigurer;

public class Journal {

    private Journal() {

    }

    public static BoundedContextConfigurer configure() {
        return new BoundedContextConfigurer.Builder()
                .packagePrefix("poussecafe.journal")
                .packagePrefix("poussecafe.events")
                .packagePrefix("poussecafe.adapters.messaging")
                .build();
    }
}
