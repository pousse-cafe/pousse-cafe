package poussecafe.doc;

import poussecafe.contextconfigurer.BoundedContextConfigurer;

public class PousseCafeDoc {

    private PousseCafeDoc() {

    }

    public static BoundedContextConfigurer configure() {
        return new BoundedContextConfigurer.Builder()
                .packagePrefix("poussecafe.doc")
                .build();
    }
}
