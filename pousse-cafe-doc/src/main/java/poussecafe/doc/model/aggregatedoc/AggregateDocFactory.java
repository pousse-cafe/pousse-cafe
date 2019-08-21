package poussecafe.doc.model.aggregatedoc;

import javax.lang.model.element.TypeElement;
import poussecafe.discovery.MessageListener;
import poussecafe.doc.ClassDocPredicates;
import poussecafe.doc.commands.CreateAggregateDoc;
import poussecafe.doc.model.ModuleComponentDoc;
import poussecafe.doc.model.ClassDocRepository;
import poussecafe.doc.model.ComponentDocFactory;
import poussecafe.doc.model.entitydoc.EntityDocFactory;
import poussecafe.doc.model.moduledoc.ModuleDocId;
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
        TypeElement aggregateClassDoc = classDocRepository.getClassDoc(className);
        if(!isAggregateDoc(aggregateClassDoc)) {
            throw new DomainException("Class " + aggregateClassDoc.getQualifiedName() + " is not an aggregate root");
        }

        AggregateDocId id = AggregateDocId.ofClassName(aggregateClassDoc.getQualifiedName().toString());
        AggregateDoc aggregateDoc = newAggregateWithId(id);

        String name = name(aggregateClassDoc);
        ModuleDocId moduleDocId = command.moduleId().value();
        aggregateDoc.attributes().moduleComponentDoc().value(new ModuleComponentDoc.Builder()
                .moduleDocId(moduleDocId)
                .componentDoc(componentDocFactory.buildDoc(name, aggregateClassDoc))
                .build());

        aggregateDoc.attributes().idClassName().value(entityDocFactory.idClassName(aggregateClassDoc));

        return aggregateDoc;
    }

    private ClassDocRepository classDocRepository;

    private ComponentDocFactory componentDocFactory;

    private EntityDocFactory entityDocFactory;

    public static String name(TypeElement classDoc) {
        return classDoc.getSimpleName().toString();
    }

    public boolean isAggregateDoc(TypeElement classDoc) {
        return classDocPredicates.documentsWithSuperclass(classDoc, AggregateRoot.class);
    }

    private ClassDocPredicates classDocPredicates;
}
