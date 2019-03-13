package poussecafe.myboundedcontext;

import poussecafe.discovery.BoundedContextConfigurer;

public class MyBoundedContext {

    private MyBoundedContext() {

    }

    public static BoundedContextConfigurer configure() {
        return new BoundedContextConfigurer.Builder()
                .packagePrefix("poussecafe.myboundedcontext")
                .build();
    }
}
