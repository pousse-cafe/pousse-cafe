package poussecafe.doc.model.aggregatedoc;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import jdk.javadoc.doclet.DocletEnvironment;
import poussecafe.discovery.MessageListener;
import poussecafe.doc.ClassDocPredicates;
import poussecafe.doc.commands.CreateAggregateDoc;
import poussecafe.doc.model.ClassDocRepository;
import poussecafe.doc.model.ComponentDocFactory;
import poussecafe.doc.model.ModuleComponentDoc;
import poussecafe.doc.model.entitydoc.EntityDocFactory;
import poussecafe.doc.model.moduledoc.ModuleDocId;
import poussecafe.domain.AggregateRoot;
import poussecafe.domain.DomainException;
import poussecafe.domain.Factory;
import poussecafe.domain.Repository;

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

        String name = id.name();
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

    public boolean isAggregateDoc(TypeElement classDoc) {
        return classDocPredicates.documentsWithSuperclass(classDoc, AggregateRoot.class);
    }

    private ClassDocPredicates classDocPredicates;

    public boolean isFactoryDoc(TypeElement classDoc) {
        return classDocPredicates.documentsWithSuperclass(classDoc, Factory.class);
    }

    public TypeElement aggregateTypeElementOfFactory(TypeElement factoryTypeElement) {
        if(!isFactoryDoc(factoryTypeElement)) {
            throw new IllegalArgumentException();
        }
        DeclaredType superclass = (DeclaredType) factoryTypeElement.getSuperclass();
        return (TypeElement) docletEnvironment.getTypeUtils().asElement(superclass.getTypeArguments().get(FACTORY_AGGREGATE_TYPE_INDEX));
    }

    private DocletEnvironment docletEnvironment;

    private static final int FACTORY_AGGREGATE_TYPE_INDEX = 1;

    public boolean isRepositoryDoc(TypeElement classDoc) {
        return classDocPredicates.documentsWithSuperclass(classDoc, Repository.class);
    }

    public TypeElement aggregateTypeElementOfRepository(TypeElement repositoryTypeElement) {
        if(!isRepositoryDoc(repositoryTypeElement)) {
            throw new IllegalArgumentException();
        }
        DeclaredType superclass = (DeclaredType) repositoryTypeElement.getSuperclass();
        return (TypeElement) docletEnvironment.getTypeUtils().asElement(superclass.getTypeArguments().get(REPOSITORY_AGGREGATE_TYPE_INDEX));
    }

    private static final int REPOSITORY_AGGREGATE_TYPE_INDEX = 0;
}
