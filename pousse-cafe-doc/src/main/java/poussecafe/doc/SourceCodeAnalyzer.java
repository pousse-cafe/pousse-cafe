package poussecafe.doc;

import com.sun.javadoc.PackageDoc;
import poussecafe.doc.model.BoundedContextDocFactory;
import poussecafe.doc.process.BoundedContextDocCreation;

public class SourceCodeAnalyzer {

    public static class Builder {

        private SourceCodeAnalyzer analyzer = new SourceCodeAnalyzer();

        public Builder rootDocWrapper(RootDocWrapper rootDocWrapper) {
            analyzer.rootDocWrapper = rootDocWrapper;
            return this;
        }

        public SourceCodeAnalyzer build() {
            return analyzer;
        }
    }

    private SourceCodeAnalyzer() {

    }

    private RootDocWrapper rootDocWrapper;

    public void analyzeCode() {
        String outputPath = rootDocWrapper.outputPath();
        rootDocWrapper.rootDoc().printNotice("Writing output to folder " + outputPath);

        PackageDoc[] packages = rootDocWrapper.rootDoc().specifiedPackages();
        for (PackageDoc packageDoc : packages) {
            if (BoundedContextDocFactory.isBoundedContextDoc(packageDoc)) {
                boundedContextDocCreation.addBoundedContextDoc(packageDoc);
            }
        }
    }

    private BoundedContextDocCreation boundedContextDocCreation;
}
