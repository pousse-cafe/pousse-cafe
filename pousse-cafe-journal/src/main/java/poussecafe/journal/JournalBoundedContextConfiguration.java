package poussecafe.journal;

import poussecafe.context.BoundedContextConfigurer;

public class JournalBoundedContextConfiguration {

    private JournalBoundedContextConfiguration() {

    }

    public static BoundedContextConfigurer configure() {
        return new BoundedContextConfigurer.Builder()
                .packagePrefix("poussecafe.journal")
                .build();
    }
}
