package poussecafe.runtime;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import poussecafe.environment.Bundle;

public class Bundles {

    public static class Builder {

        private Bundles bundles = new Bundles();

        public Builder withBundle(Bundle bundle) {
            bundles.bundlesList.add(bundle);
            return this;
        }

        public Builder withList(List<Bundle> bundlesList) {
            bundles.bundlesList.addAll(bundlesList);
            return this;
        }

        public Bundles build() {
            return bundles;
        }
    }

    private Bundles() {

    }

    private List<Bundle> bundlesList = new ArrayList<>();

    public void forEach(Consumer<Bundle> consumer) {
        bundlesList.forEach(consumer);
    }
}
