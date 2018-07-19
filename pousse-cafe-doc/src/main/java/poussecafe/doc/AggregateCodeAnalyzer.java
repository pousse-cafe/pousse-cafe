package poussecafe.doc;

import com.sun.javadoc.ClassDoc;
import poussecafe.doc.model.aggregatedoc.AggregateDocFactory;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDoc;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDocRepository;
import poussecafe.doc.process.AggregateDocCreation;

public class AggregateCodeAnalyzer {

    public static class Builder {

        private AggregateCodeAnalyzer analyzer = new AggregateCodeAnalyzer();

        public Builder rootDocWrapper(RootDocWrapper rootDocWrapper) {
            analyzer.rootDocWrapper = rootDocWrapper;
            return this;
        }

        public AggregateCodeAnalyzer build() {
            return analyzer;
        }
    }

    private AggregateCodeAnalyzer() {

    }

    private RootDocWrapper rootDocWrapper;

    public void analyzeCode() {
        ClassDoc[] classDocs = rootDocWrapper.rootDoc().classes();
        for (ClassDoc classDoc : classDocs) {
            if (!AnnotationsResolver.isIgnored(classDoc) && AggregateDocFactory.isAggregateDoc(classDoc)) {
                BoundedContextDoc boundedContextDoc = boundedContextDocRepository.findByPackageNamePrefixing(classDoc.qualifiedName());
                if(boundedContextDoc != null) {
                    rootDocWrapper.debug("Adding aggregate with class " + classDoc.name());
                    aggregateDocCreation.addAggregateDoc(classDoc);
                } else {
                    rootDocWrapper.debug("Skipping aggregate with class " + classDoc.name() + ", no documented bounded context");
                }
            }
        }
    }

    private BoundedContextDocRepository boundedContextDocRepository;

    private AggregateDocCreation aggregateDocCreation;
}
