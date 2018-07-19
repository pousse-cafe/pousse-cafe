package poussecafe.doc;

import com.sun.javadoc.ClassDoc;
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
        ClassDoc[] classes = rootDocWrapper.rootDoc().classes();
        for (ClassDoc classDoc : classes) {
            if (!AnnotationsResolver.isIgnored(classDoc) && BoundedContextDocFactory.isBoundedContextDoc(classDoc)) {
                rootDocWrapper.debug("Adding bounded context with package " + classDoc.name());
                boundedContextDocCreation.addBoundedContextDoc(classDoc);
            }
        }
    }

    private BoundedContextDocCreation boundedContextDocCreation;
}
