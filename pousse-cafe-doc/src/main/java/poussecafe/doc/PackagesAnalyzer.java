package poussecafe.doc;

import com.sun.javadoc.PackageDoc;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public class PackagesAnalyzer {

    public static class Builder {

        private PackagesAnalyzer analyzer = new PackagesAnalyzer();

        public Builder rootDocWrapper(RootDocWrapper rootDocWrapper) {
            analyzer.rootDocWrapper = rootDocWrapper;
            return this;
        }

        public Builder packageDocConsumer(Consumer<PackageDoc> classDocConsumer) {
            Objects.requireNonNull(classDocConsumer);
            analyzer.packageDocConsumers.add(classDocConsumer);
            return this;
        }

        public PackagesAnalyzer build() {
            Objects.requireNonNull(analyzer.rootDocWrapper);
            return analyzer;
        }
    }

    private PackagesAnalyzer() {

    }

    private RootDocWrapper rootDocWrapper;

    private List<Consumer<PackageDoc>> packageDocConsumers = new ArrayList<>();

    public void analyzeCode() {
        PackageDoc[] classes = rootDocWrapper.rootDoc().specifiedPackages();
        for (PackageDoc classDoc : classes) {
            if (!AnnotationsResolver.isIgnored(classDoc)) {
                processPackageDoc(classDoc);
            }
        }
    }

    private void processPackageDoc(PackageDoc classDoc) {
        for(Consumer<PackageDoc> consumer : packageDocConsumers) {
            consumer.accept(classDoc);
        }
    }
}
