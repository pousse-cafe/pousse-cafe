package poussecafe.doc;

import com.sun.javadoc.ClassDoc;
import java.util.function.Consumer;
import poussecafe.doc.model.aggregatedoc.AggregateDocFactory;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDoc;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDocRepository;
import poussecafe.doc.process.AggregateDocCreation;

import static poussecafe.check.Checks.checkThatValue;

public class AggregateDocCreator implements Consumer<ClassDoc> {

    public AggregateDocCreator(RootDocWrapper rootDocWrapper) {
        checkThatValue(rootDocWrapper).notNull();
        this.rootDocWrapper = rootDocWrapper;
    }

    private RootDocWrapper rootDocWrapper;

    @Override
    public void accept(ClassDoc classDoc) {
        if (AggregateDocFactory.isAggregateDoc(classDoc)) {
            BoundedContextDoc boundedContextDoc = boundedContextDocRepository
                    .findByPackageNamePrefixing(classDoc.qualifiedName());
            if (boundedContextDoc != null) {
                rootDocWrapper.debug("Adding aggregate with class " + classDoc.name());
                aggregateDocCreation.addAggregateDoc(classDoc);
            } else {
                rootDocWrapper
                        .debug("Skipping aggregate with class " + classDoc.name() + ", no documented bounded context");
            }
        }
    }

    private BoundedContextDocRepository boundedContextDocRepository;

    private AggregateDocCreation aggregateDocCreation;
}
