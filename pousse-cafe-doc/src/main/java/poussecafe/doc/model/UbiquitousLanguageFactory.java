package poussecafe.doc.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import poussecafe.doc.model.aggregatedoc.AggregateDoc;
import poussecafe.doc.model.domainprocessdoc.DomainProcessDoc;
import poussecafe.doc.model.entitydoc.EntityDoc;
import poussecafe.doc.model.moduledoc.ModuleDoc;
import poussecafe.doc.model.servicedoc.ServiceDoc;
import poussecafe.doc.model.vodoc.ValueObjectDoc;
import poussecafe.domain.Service;

import static java.util.stream.Collectors.toList;

public class UbiquitousLanguageFactory implements Service {

    public List<UbiquitousLanguageEntry> buildUbiquitousLanguage(Domain domain) {
        Set<UbiquitousLanguageEntry> language = new HashSet<>();
        for(Module module : domain.modules()) {
            ModuleDoc moduleDoc = module.documentation();
            language.add(new UbiquitousLanguageEntry.Builder()
                            .componentDoc(moduleDoc.attributes().componentDoc().value())
                            .type("Bounded Context")
                            .build());

            String moduleName = moduleDoc.attributes().componentDoc().value().name();
            for (Aggregate aggregate : module.aggregates()) {
                AggregateDoc aggregateDoc = aggregate.documentation();
                language
                        .add(new UbiquitousLanguageEntry.Builder()
                                .moduleName(moduleName)
                                .componentDoc(aggregateDoc.attributes().moduleComponentDoc().value().componentDoc())
                                .type("Aggregate")
                                .build());

                for (EntityDoc entityDoc : aggregate.entities()) {
                    language
                            .add(new UbiquitousLanguageEntry.Builder()
                                    .moduleName(moduleName)
                                    .componentDoc(entityDoc.attributes().moduleComponentDoc().value().componentDoc())
                                    .type("Entity")
                                    .build());
                }

                for (ValueObjectDoc valueObjectDoc : aggregate.valueObjects()) {
                    language
                            .add(new UbiquitousLanguageEntry.Builder()
                                    .moduleName(moduleName)
                                    .componentDoc(valueObjectDoc.attributes().moduleComponentDoc().value().componentDoc())
                                    .type("Value Object")
                                    .build());
                }
            }

            for (ServiceDoc serviceDoc : module.services()) {
                language
                        .add(new UbiquitousLanguageEntry.Builder()
                                .moduleName(moduleName)
                                .componentDoc(serviceDoc.attributes().moduleComponentDoc().value().componentDoc())
                                .type("Service")
                                .build());
            }

            for (DomainProcessDoc domainProcessDoc : module.processes()) {
                language
                        .add(new UbiquitousLanguageEntry.Builder()
                                .moduleName(moduleName)
                                .componentDoc(domainProcessDoc.attributes().moduleComponentDoc().value().componentDoc())
                                .type("Domain Process")
                                .build());
            }
        }

        return language.stream().sorted().collect(toList());
    }
}
