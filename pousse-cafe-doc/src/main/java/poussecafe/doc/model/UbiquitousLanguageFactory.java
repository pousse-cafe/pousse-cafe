package poussecafe.doc.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import poussecafe.doc.model.aggregatedoc.AggregateDoc;
import poussecafe.doc.model.aggregatedoc.AggregateDocRepository;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDoc;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDocRepository;
import poussecafe.doc.model.entitydoc.EntityDoc;
import poussecafe.doc.model.entitydoc.EntityDocRepository;
import poussecafe.doc.model.servicedoc.ServiceDoc;
import poussecafe.doc.model.servicedoc.ServiceDocRepository;
import poussecafe.doc.model.vodoc.ValueObjectDoc;
import poussecafe.doc.model.vodoc.ValueObjectDocRepository;
import poussecafe.domain.Service;

public class UbiquitousLanguageFactory implements Service {

    public List<UbiquitousLanguageEntry> buildUbiquitousLanguage() {
        List<UbiquitousLanguageEntry> language = new ArrayList<>();
        for (BoundedContextDoc boundedContext : boundedContextDocRepository.findAll()) {
            language
                    .add(new UbiquitousLanguageEntry.Builder()
                            .componentDoc(boundedContext.componentDoc())
                            .type("Bounded Context")
                            .build());
        }
        for (AggregateDoc aggregateDoc : aggregateDocRepository.findAll()) {
            language
                    .add(new UbiquitousLanguageEntry.Builder()
                            .boundedContextName(boundedContextName(aggregateDoc.boundedContextComponentDoc()))
                            .componentDoc(aggregateDoc.boundedContextComponentDoc().componentDoc())
                            .type("Aggregate")
                            .build());
        }
        for (ServiceDoc serviceDoc : serviceDocRepository.findAll()) {
            language
                    .add(new UbiquitousLanguageEntry.Builder()
                            .boundedContextName(boundedContextName(serviceDoc.boundedContextComponentDoc()))
                            .componentDoc(serviceDoc.boundedContextComponentDoc().componentDoc())
                            .type("Service")
                            .build());
        }
        for (EntityDoc entityDoc : entityDocRepository.findAll()) {
            language
                    .add(new UbiquitousLanguageEntry.Builder()
                            .boundedContextName(boundedContextName(entityDoc.boundedContextComponentDoc()))
                            .componentDoc(entityDoc.boundedContextComponentDoc().componentDoc())
                            .type("Entity")
                            .build());
        }
        for (ValueObjectDoc valueObjectDoc : valueObjectDocRepository.findAll()) {
            language
                    .add(new UbiquitousLanguageEntry.Builder()
                            .boundedContextName(boundedContextName(valueObjectDoc.boundedContextComponentDoc()))
                            .componentDoc(valueObjectDoc.boundedContextComponentDoc().componentDoc())
                            .type("Value Object")
                            .build());
        }
        Collections.sort(language);
        return language;
    }

    private String boundedContextName(BoundedContextComponentDoc doc) {
        return boundedContextDocRepository.get(doc.boundedContextDocKey()).componentDoc().name();
    }

    private BoundedContextDocRepository boundedContextDocRepository;

    private AggregateDocRepository aggregateDocRepository;

    private ServiceDocRepository serviceDocRepository;

    private EntityDocRepository entityDocRepository;

    private ValueObjectDocRepository valueObjectDocRepository;
}
