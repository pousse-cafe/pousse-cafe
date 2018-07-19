package poussecafe.doc;

import com.sun.javadoc.PackageDoc;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDocFactory;
import poussecafe.doc.process.BoundedContextDocCreation;

public class BoundedContextCodeAnalyzer {

    public static class Builder {

        private BoundedContextCodeAnalyzer analyzer = new BoundedContextCodeAnalyzer();

        public Builder rootDocWrapper(RootDocWrapper rootDocWrapper) {
            analyzer.rootDocWrapper = rootDocWrapper;
            return this;
        }

        public BoundedContextCodeAnalyzer build() {
            return analyzer;
        }
    }

    private BoundedContextCodeAnalyzer() {

    }

    private RootDocWrapper rootDocWrapper;

    public void analyzeCode() {
        PackageDoc[] packages = rootDocWrapper.rootDoc().specifiedPackages();
        for (PackageDoc packageDoc : packages) {
            if (BoundedContextDocFactory.isBoundedContextDoc(packageDoc)) {
                rootDocWrapper.debug("Adding bounded context with package " + packageDoc.name());
                boundedContextDocCreation.addBoundedContextDoc(packageDoc);
            }
        }
    }

    private BoundedContextDocCreation boundedContextDocCreation;
}
