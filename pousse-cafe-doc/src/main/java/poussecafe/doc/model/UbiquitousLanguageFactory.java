package poussecafe.doc.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import poussecafe.doc.model.aggregatedoc.AggregateDoc;
import poussecafe.doc.model.aggregatedoc.AggregateDocRepository;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDoc;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDocRepository;
import poussecafe.domain.Service;

public class UbiquitousLanguageFactory implements Service {

    public List<UbiquitousLanguageEntry> buildUbiquitousLanguage() {
        List<UbiquitousLanguageEntry> language = new ArrayList<>();
        for (BoundedContextDoc boundedContext : boundedContextDocRepository.findAll()) {
            language.add(new UbiquitousLanguageEntry(boundedContext.name(), "Bounded Context",
                            boundedContext.description()));
        }
        for (AggregateDoc aggregateDoc : aggregateDocRepository.findAll()) {
            language.add(new UbiquitousLanguageEntry(aggregateDoc.name(), "Aggregate", aggregateDoc.description()));
        }
        Collections.sort(language);
        return language;
    }

    private BoundedContextDocRepository boundedContextDocRepository;

    private AggregateDocRepository aggregateDocRepository;
}
