package poussecafe.doc;

import java.util.Set;
import poussecafe.context.BoundedContext;
import poussecafe.doc.model.ComponentDocFactory;
import poussecafe.doc.model.UbiquitousLanguageFactory;
import poussecafe.doc.model.aggregatedoc.AggregateDoc;
import poussecafe.doc.model.aggregatedoc.AggregateDocData;
import poussecafe.doc.model.aggregatedoc.AggregateDocFactory;
import poussecafe.doc.model.aggregatedoc.AggregateDocRepository;
import poussecafe.doc.model.aggregatedoc.InternalAggregateDocDataAccess;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDoc;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDocData;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDocFactory;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDocRepository;
import poussecafe.doc.model.boundedcontextdoc.InternalBoundedContextDocDataAccess;
import poussecafe.doc.model.domainprocessdoc.DomainProcessDoc;
import poussecafe.doc.model.domainprocessdoc.DomainProcessDocData;
import poussecafe.doc.model.domainprocessdoc.DomainProcessDocFactory;
import poussecafe.doc.model.domainprocessdoc.DomainProcessDocRepository;
import poussecafe.doc.model.domainprocessdoc.InternalDomainProcessDocDataAccess;
import poussecafe.doc.model.entitydoc.EntityDoc;
import poussecafe.doc.model.entitydoc.EntityDocData;
import poussecafe.doc.model.entitydoc.EntityDocFactory;
import poussecafe.doc.model.entitydoc.EntityDocRepository;
import poussecafe.doc.model.entitydoc.InternalEntityDocDataAccess;
import poussecafe.doc.model.factorydoc.FactoryDoc;
import poussecafe.doc.model.factorydoc.FactoryDocData;
import poussecafe.doc.model.factorydoc.FactoryDocFactory;
import poussecafe.doc.model.factorydoc.FactoryDocRepository;
import poussecafe.doc.model.factorydoc.InternalFactoryDocDataAccess;
import poussecafe.doc.model.relation.InternalRelationDataAccess;
import poussecafe.doc.model.relation.Relation;
import poussecafe.doc.model.relation.RelationData;
import poussecafe.doc.model.relation.RelationFactory;
import poussecafe.doc.model.relation.RelationRepository;
import poussecafe.doc.model.servicedoc.InternalServiceDocDataAccess;
import poussecafe.doc.model.servicedoc.ServiceDoc;
import poussecafe.doc.model.servicedoc.ServiceDocData;
import poussecafe.doc.model.servicedoc.ServiceDocFactory;
import poussecafe.doc.model.servicedoc.ServiceDocRepository;
import poussecafe.doc.model.step.StepDocExtractor;
import poussecafe.doc.model.vodoc.InternalValueObjectDocDataAccess;
import poussecafe.doc.model.vodoc.ValueObjectDoc;
import poussecafe.doc.model.vodoc.ValueObjectDocData;
import poussecafe.doc.model.vodoc.ValueObjectDocFactory;
import poussecafe.doc.model.vodoc.ValueObjectDocRepository;
import poussecafe.doc.process.AggregateDocCreation;
import poussecafe.doc.process.BoundedContextDocCreation;
import poussecafe.doc.process.ComponentLinking;
import poussecafe.doc.process.DomainProcessDocCreation;
import poussecafe.doc.process.EntityDocCreation;
import poussecafe.doc.process.FactoryDocCreation;
import poussecafe.doc.process.ServiceDocCreation;
import poussecafe.doc.process.ValueObjectDocCreation;
import poussecafe.domain.EntityDefinition;
import poussecafe.domain.EntityImplementation;
import poussecafe.domain.Service;
import poussecafe.messaging.MessageImplementationConfiguration;
import poussecafe.process.DomainProcess;
import poussecafe.storage.internal.InternalStorage;

public class PousseCafeDoc extends BoundedContext {

    @Override
    protected void loadDefinitions(Set<EntityDefinition> definitions) {
        definitions.add(new EntityDefinition.Builder()
                .withEntityClass(BoundedContextDoc.class)
                .withFactoryClass(BoundedContextDocFactory.class)
                .withRepositoryClass(BoundedContextDocRepository.class)
                .build());

        definitions.add(new EntityDefinition.Builder()
                .withEntityClass(AggregateDoc.class)
                .withFactoryClass(AggregateDocFactory.class)
                .withRepositoryClass(AggregateDocRepository.class)
                .build());

        definitions.add(new EntityDefinition.Builder()
                .withEntityClass(ServiceDoc.class)
                .withFactoryClass(ServiceDocFactory.class)
                .withRepositoryClass(ServiceDocRepository.class)
                .build());

        definitions.add(new EntityDefinition.Builder()
                .withEntityClass(EntityDoc.class)
                .withFactoryClass(EntityDocFactory.class)
                .withRepositoryClass(EntityDocRepository.class)
                .build());

        definitions.add(new EntityDefinition.Builder()
                .withEntityClass(ValueObjectDoc.class)
                .withFactoryClass(ValueObjectDocFactory.class)
                .withRepositoryClass(ValueObjectDocRepository.class)
                .build());

        definitions.add(new EntityDefinition.Builder()
                .withEntityClass(Relation.class)
                .withFactoryClass(RelationFactory.class)
                .withRepositoryClass(RelationRepository.class)
                .build());

        definitions.add(new EntityDefinition.Builder()
                .withEntityClass(DomainProcessDoc.class)
                .withFactoryClass(DomainProcessDocFactory.class)
                .withRepositoryClass(DomainProcessDocRepository.class)
                .build());

        definitions.add(new EntityDefinition.Builder()
                .withEntityClass(FactoryDoc.class)
                .withFactoryClass(FactoryDocFactory.class)
                .withRepositoryClass(FactoryDocRepository.class)
                .build());
    }

    @Override
    protected void loadEntityImplementations(Set<EntityImplementation> implementations) {
        implementations.add(new EntityImplementation.Builder()
                .withEntityClass(BoundedContextDoc.class)
                .withDataFactory(BoundedContextDocData::new)
                .withDataAccessFactory(InternalBoundedContextDocDataAccess::new)
                .withStorage(InternalStorage.instance())
                .build());

        implementations.add(new EntityImplementation.Builder()
                .withEntityClass(AggregateDoc.class)
                .withDataFactory(AggregateDocData::new)
                .withDataAccessFactory(InternalAggregateDocDataAccess::new)
                .withStorage(InternalStorage.instance())
                .build());

        implementations.add(new EntityImplementation.Builder()
                .withEntityClass(ServiceDoc.class)
                .withDataFactory(ServiceDocData::new)
                .withDataAccessFactory(InternalServiceDocDataAccess::new)
                .withStorage(InternalStorage.instance())
                .build());

        implementations.add(new EntityImplementation.Builder()
                .withEntityClass(EntityDoc.class)
                .withDataFactory(EntityDocData::new)
                .withDataAccessFactory(InternalEntityDocDataAccess::new)
                .withStorage(InternalStorage.instance())
                .build());

        implementations.add(new EntityImplementation.Builder()
                .withEntityClass(ValueObjectDoc.class)
                .withDataFactory(ValueObjectDocData::new)
                .withDataAccessFactory(InternalValueObjectDocDataAccess::new)
                .withStorage(InternalStorage.instance())
                .build());

        implementations.add(new EntityImplementation.Builder()
                .withEntityClass(Relation.class)
                .withDataFactory(RelationData::new)
                .withDataAccessFactory(InternalRelationDataAccess::new)
                .withStorage(InternalStorage.instance())
                .build());

        implementations.add(new EntityImplementation.Builder()
                .withEntityClass(DomainProcessDoc.class)
                .withDataFactory(DomainProcessDocData::new)
                .withDataAccessFactory(InternalDomainProcessDocDataAccess::new)
                .withStorage(InternalStorage.instance())
                .build());

        implementations.add(new EntityImplementation.Builder()
                .withEntityClass(FactoryDoc.class)
                .withDataFactory(FactoryDocData::new)
                .withDataAccessFactory(InternalFactoryDocDataAccess::new)
                .withStorage(InternalStorage.instance())
                .build());
    }

    @Override
    protected void loadProcesses(Set<Class<? extends DomainProcess>> processes) {
        processes.add(BoundedContextDocCreation.class);
        processes.add(AggregateDocCreation.class);
        processes.add(ServiceDocCreation.class);
        processes.add(EntityDocCreation.class);
        processes.add(ValueObjectDocCreation.class);
        processes.add(ComponentLinking.class);
        processes.add(DomainProcessDocCreation.class);
        processes.add(FactoryDocCreation.class);
    }

    @Override
    protected void loadServices(Set<Class<? extends Service>> services) {
        services.add(GraphFactory.class);
        services.add(UbiquitousLanguageFactory.class);
        services.add(ComponentDocFactory.class);
        services.add(StepDocExtractor.class);
    }

    @Override
    protected void loadMessageImplementations(Set<MessageImplementationConfiguration> implementations) {
        // None
    }

}
