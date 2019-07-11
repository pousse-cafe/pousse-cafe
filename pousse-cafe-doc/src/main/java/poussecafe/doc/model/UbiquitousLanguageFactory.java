package poussecafe.doc.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import poussecafe.doc.model.aggregatedoc.AggregateDoc;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDoc;
import poussecafe.doc.model.domainprocessdoc.DomainProcessDoc;
import poussecafe.doc.model.entitydoc.EntityDoc;
import poussecafe.doc.model.servicedoc.ServiceDoc;
import poussecafe.doc.model.vodoc.ValueObjectDoc;
import poussecafe.domain.Service;

import static java.util.stream.Collectors.toList;

public class UbiquitousLanguageFactory implements Service {

    public List<UbiquitousLanguageEntry> buildUbiquitousLanguage(Domain domain) {
        Set<UbiquitousLanguageEntry> language = new HashSet<>();
        for(BoundedContext boundedContext : domain.boundedContexts()) {
            BoundedContextDoc boundedContextDoc = boundedContext.documentation();
            language.add(new UbiquitousLanguageEntry.Builder()
                            .componentDoc(boundedContextDoc.attributes().componentDoc().value())
                            .type("Bounded Context")
                            .build());

            String boundedContextName = boundedContextDoc.attributes().componentDoc().value().name();
            for (Aggregate aggregate : boundedContext.aggregates()) {
                AggregateDoc aggregateDoc = aggregate.documentation();
                language
                        .add(new UbiquitousLanguageEntry.Builder()
                                .boundedContextName(boundedContextName)
                                .componentDoc(aggregateDoc.attributes().boundedContextComponentDoc().value().componentDoc())
                                .type("Aggregate")
                                .build());

                for (EntityDoc entityDoc : aggregate.entities()) {
                    language
                            .add(new UbiquitousLanguageEntry.Builder()
                                    .boundedContextName(boundedContextName)
                                    .componentDoc(entityDoc.attributes().boundedContextComponentDoc().value().componentDoc())
                                    .type("Entity")
                                    .build());
                }

                for (ValueObjectDoc valueObjectDoc : aggregate.valueObjects()) {
                    language
                            .add(new UbiquitousLanguageEntry.Builder()
                                    .boundedContextName(boundedContextName)
                                    .componentDoc(valueObjectDoc.attributes().boundedContextComponentDoc().value().componentDoc())
                                    .type("Value Object")
                                    .build());
                }
            }

            for (ServiceDoc serviceDoc : boundedContext.services()) {
                language
                        .add(new UbiquitousLanguageEntry.Builder()
                                .boundedContextName(boundedContextName)
                                .componentDoc(serviceDoc.attributes().boundedContextComponentDoc().value().componentDoc())
                                .type("Service")
                                .build());
            }

            for (DomainProcessDoc domainProcessDoc : boundedContext.processes()) {
                language
                        .add(new UbiquitousLanguageEntry.Builder()
                                .boundedContextName(boundedContextName)
                                .componentDoc(domainProcessDoc.attributes().boundedContextComponentDoc().value().componentDoc())
                                .type("Domain Process")
                                .build());
            }
        }

        return language.stream().sorted().collect(toList());
    }
}
