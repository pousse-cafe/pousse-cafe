package poussecafe.journal;

import poussecafe.discovery.BundleConfigurer;

public class Journal {

    private Journal() {

    }

    public static BundleConfigurer configure() {
        return new BundleConfigurer.Builder()
                .moduleBasePackage("poussecafe.journal")
                .moduleBasePackage("poussecafe.support")
                .build();
    }
}
