package poussecafe.doc.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import poussecafe.doc.PousseCafeDocletConfiguration;
import poussecafe.doc.model.aggregatedoc.AggregateDoc;
import poussecafe.doc.model.aggregatedoc.AggregateDocRepository;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDoc;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDocRepository;
import poussecafe.doc.model.domainprocessdoc.DomainProcessDoc;
import poussecafe.doc.model.domainprocessdoc.DomainProcessDocRepository;
import poussecafe.doc.model.entitydoc.EntityDoc;
import poussecafe.doc.model.entitydoc.EntityDocId;
import poussecafe.doc.model.entitydoc.EntityDocRepository;
import poussecafe.doc.model.relation.ComponentType;
import poussecafe.doc.model.relation.Relation;
import poussecafe.doc.model.relation.RelationRepository;
import poussecafe.doc.model.servicedoc.ServiceDoc;
import poussecafe.doc.model.servicedoc.ServiceDocRepository;
import poussecafe.doc.model.vodoc.ValueObjectDoc;
import poussecafe.doc.model.vodoc.ValueObjectDocId;
import poussecafe.doc.model.vodoc.ValueObjectDocRepository;
import poussecafe.domain.Service;

import static java.util.stream.Collectors.toList;

public class DomainFactory implements Service {

    public Domain buildDomain() {
        return new Domain.Builder()
                .name(configuration.domainName())
                .version(configuration.version())
                .boundedContexts(boundedContexts())
                .build();
    }

    private PousseCafeDocletConfiguration configuration;

    private List<BoundedContext> boundedContexts() {
        List<BoundedContext> boundedContexts = new ArrayList<>();
        for(BoundedContextDoc boundedContextDoc : boundedContextDocRepository.findAll()) {
            boundedContexts.add(boundedContext(boundedContextDoc));
        }
        return boundedContexts;
    }

    private BoundedContextDocRepository boundedContextDocRepository;

    private BoundedContext boundedContext(BoundedContextDoc boundedContextDoc) {
        return new BoundedContext.Builder()
                .documentation(boundedContextDoc)
                .aggregates(aggregates(boundedContextDoc))
                .services(services(boundedContextDoc))
                .processes(processes(boundedContextDoc))
                .build();
    }

    private List<Aggregate> aggregates(BoundedContextDoc boundedContextDoc) {
        List<Aggregate> aggregates = new ArrayList<>();
        for(AggregateDoc aggregateDoc : aggregateDocRepository.findByBoundedContextId(boundedContextDoc.attributes().identifier().value())) {
            aggregates.add(aggregate(aggregateDoc));
        }
        return aggregates;
    }

    private AggregateDocRepository aggregateDocRepository;

    private Aggregate aggregate(AggregateDoc aggregateDoc) {
        return new Aggregate.Builder()
                .documentation(aggregateDoc)
                .entities(entities(aggregateDoc))
                .valueObjects(valueObjects(aggregateDoc))
                .build();
    }

    private List<EntityDoc> entities(AggregateDoc aggregateDoc) {
        return findEntities(aggregateDoc.className()).stream()
                .map(entityDocRepository::find)
                .filter(Objects::nonNull)
                .collect(toList());
    }

    private Set<EntityDocId> findEntities(String fromClassName) {
        Set<EntityDocId> ids = new HashSet<>();
        for(Relation relation : relationRepository.findWithFromClassName(fromClassName)) {
            if(relation.toComponent().type() == ComponentType.ENTITY) {
                ids.add(EntityDocId.ofClassName(relation.toComponent().className()));
            }
            if(relation.toComponent().type() != ComponentType.AGGREGATE) {
                ids.addAll(findEntities(relation.toComponent().className()));
            }
        }
        return ids;
    }

    private RelationRepository relationRepository;

    private EntityDocRepository entityDocRepository;

    private List<ValueObjectDoc> valueObjects(AggregateDoc aggregateDoc) {
        return findValueObjects(aggregateDoc.className()).stream()
                .map(valueObjectDocRepository::find)
                .filter(Objects::nonNull)
                .collect(toList());
    }

    private Set<ValueObjectDocId> findValueObjects(String fromClassName) {
        Set<ValueObjectDocId> ids = new HashSet<>();
        for(Relation relation : relationRepository.findWithFromClassName(fromClassName)) {
            if(relation.toComponent().type() == ComponentType.VALUE_OBJECT) {
                ids.add(ValueObjectDocId.ofClassName(relation.toComponent().className()));
            }
            if(relation.toComponent().type() != ComponentType.AGGREGATE) {
                ids.addAll(findValueObjects(relation.toComponent().className()));
            }
        }
        return ids;
    }

    private ValueObjectDocRepository valueObjectDocRepository;

    private List<ServiceDoc> services(BoundedContextDoc boundedContextDoc) {
        return serviceDocRepository.findByBoundedContextId(boundedContextDoc.attributes().identifier().value());
    }

    private ServiceDocRepository serviceDocRepository;

    private List<DomainProcessDoc> processes(BoundedContextDoc boundedContextDoc) {
        return domainProcessDocRepository.findByBoundedContextId(boundedContextDoc.attributes().identifier().value());
    }

    private DomainProcessDocRepository domainProcessDocRepository;
}
