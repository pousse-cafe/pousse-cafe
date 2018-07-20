package poussecafe.doc;

import com.sun.javadoc.ClassDoc;
import java.util.List;
import java.util.function.Consumer;
import poussecafe.doc.model.AggregateDocLocator;
import poussecafe.doc.model.aggregatedoc.AggregateDoc;
import poussecafe.doc.model.aggregatedoc.AggregateDocFactory;
import poussecafe.doc.model.entitydoc.EntityDocFactory;
import poussecafe.doc.process.EntityDocCreation;

import static poussecafe.check.Checks.checkThatValue;

public class EntityDocCreator implements Consumer<ClassDoc> {

    public EntityDocCreator(RootDocWrapper rootDocWrapper) {
        checkThatValue(rootDocWrapper).notNull();
        this.rootDocWrapper = rootDocWrapper;
    }

    private RootDocWrapper rootDocWrapper;

    @Override
    public void accept(ClassDoc classDoc) {
        if (AggregateDocFactory.isAggregateDoc(classDoc)) {
            AggregateDoc aggregateDoc = aggregateDocLocator.locateAggregateDoc(classDoc);
            if(aggregateDoc == null) {
                return;
            }

            for(ClassDoc entityClassDoc : findRelatedEntities(classDoc)) {
                rootDocWrapper.debug("Adding entity with class " + entityClassDoc.name() + " to aggregate " + aggregateDoc.name());
                entityDocCreation.addEntityDoc(aggregateDoc.getKey(), entityClassDoc);
            }
        }
    }

    private List<ClassDoc> findRelatedEntities(ClassDoc classDoc) {
        return new CodeCrawler.Builder()
                .rootClassDoc(classDoc)
                .basePackage(rootDocWrapper.basePackage())
                .matcher(candidateClassDoc -> EntityDocFactory.isEntityDoc(candidateClassDoc))
                .maxMatchedDepth(1)
                .buildAndCrawlCode();
    }

    private AggregateDocLocator aggregateDocLocator;

    private EntityDocCreation entityDocCreation;
}
