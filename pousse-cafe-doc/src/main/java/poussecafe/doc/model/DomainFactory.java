package poussecafe.doc.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import poussecafe.doc.PousseCafeDocletConfiguration;
import poussecafe.doc.model.aggregatedoc.AggregateDoc;
import poussecafe.doc.model.aggregatedoc.AggregateDocRepository;
import poussecafe.doc.model.domainprocessdoc.DomainProcessDoc;
import poussecafe.doc.model.domainprocessdoc.DomainProcessDocRepository;
import poussecafe.doc.model.entitydoc.EntityDoc;
import poussecafe.doc.model.entitydoc.EntityDocId;
import poussecafe.doc.model.entitydoc.EntityDocRepository;
import poussecafe.doc.model.moduledoc.ModuleDoc;
import poussecafe.doc.model.moduledoc.ModuleDocRepository;
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
                .modules(modules())
                .build();
    }

    private PousseCafeDocletConfiguration configuration;

    private List<Module> modules() {
        List<Module> modules = new ArrayList<>();
        for(ModuleDoc moduleDoc : moduleDocRepository.findAll()) {
            modules.add(module(moduleDoc));
        }
        return modules;
    }

    private ModuleDocRepository moduleDocRepository;

    private Module module(ModuleDoc moduleDoc) {
        return new Module.Builder()
                .documentation(moduleDoc)
                .aggregates(aggregates(moduleDoc))
                .services(services(moduleDoc))
                .processes(processes(moduleDoc))
                .build();
    }

    private List<Aggregate> aggregates(ModuleDoc moduleDoc) {
        List<Aggregate> aggregates = new ArrayList<>();
        for(AggregateDoc aggregateDoc : aggregateDocRepository.findByModule(moduleDoc.attributes().identifier().value())) {
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
                .map(entityDocRepository::getOptional)
                .filter(Optional::isPresent)
                .map(Optional::get)
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
                .map(valueObjectDocRepository::getOptional)
                .filter(Optional::isPresent)
                .map(Optional::get)
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

    private List<ServiceDoc> services(ModuleDoc moduleDoc) {
        return serviceDocRepository.findByModuleId(moduleDoc.attributes().identifier().value());
    }

    private ServiceDocRepository serviceDocRepository;

    private List<DomainProcessDoc> processes(ModuleDoc moduleDoc) {
        return domainProcessDocRepository.findByModuleId(moduleDoc.attributes().identifier().value());
    }

    private DomainProcessDocRepository domainProcessDocRepository;
}
