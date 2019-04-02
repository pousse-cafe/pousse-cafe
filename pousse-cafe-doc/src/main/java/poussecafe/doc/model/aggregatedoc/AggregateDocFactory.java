package poussecafe.doc.model.aggregatedoc;

import com.sun.javadoc.ClassDoc;
import poussecafe.discovery.MessageListener;
import poussecafe.doc.ClassDocPredicates;
import poussecafe.doc.commands.CreateAggregateDoc;
import poussecafe.doc.model.BoundedContextComponentDoc;
import poussecafe.doc.model.ClassDocRepository;
import poussecafe.doc.model.ComponentDocFactory;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDocId;
import poussecafe.domain.AggregateRoot;
import poussecafe.domain.DomainException;
import poussecafe.domain.Factory;

public class AggregateDocFactory extends Factory<AggregateDocId, AggregateDoc, AggregateDoc.Attributes> {

    /**
     * @process AggregateDocCreation
     */
    @MessageListener
    public AggregateDoc newAggregateDoc(CreateAggregateDoc command) {
        String className = command.className().value();
        ClassDoc aggregateClassDoc = classDocRepository.getClassDoc(className);
        if(!isAggregateDoc(aggregateClassDoc)) {
            throw new DomainException("Class " + aggregateClassDoc.name() + " is not an aggregate root");
        }

        AggregateDocId id = AggregateDocId.ofClassName(aggregateClassDoc.qualifiedTypeName());
        AggregateDoc aggregateDoc = newAggregateWithId(id);

        String name = name(aggregateClassDoc);
        BoundedContextDocId boundedContextDocId = command.boundedContextId().value();
        aggregateDoc.attributes().boundedContextComponentDoc().value(new BoundedContextComponentDoc.Builder()
                .boundedContextDocId(boundedContextDocId)
                .componentDoc(componentDocFactory.buildDoc(name, aggregateClassDoc))
                .build());

        aggregateDoc.attributes().idClassName().value(idClassName(aggregateClassDoc));

        return aggregateDoc;
    }

    private ClassDocRepository classDocRepository;

    private ComponentDocFactory componentDocFactory;

    private String idClassName(ClassDoc aggregateClassDoc) {
        return aggregateClassDoc.superclassType().asParameterizedType().typeArguments()[KEY_TYPE_INDEX].qualifiedTypeName();
    }

    private static final int KEY_TYPE_INDEX = 0;

    public static String name(ClassDoc classDoc) {
        return classDoc.simpleTypeName();
    }

    public static boolean isAggregateDoc(ClassDoc classDoc) {
        return ClassDocPredicates.documentsWithSuperclass(classDoc, AggregateRoot.class);
    }
}
