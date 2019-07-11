package poussecafe.doc;

import freemarker.template.Configuration;
import freemarker.template.Template;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import org.apache.commons.io.IOUtils;
import poussecafe.doc.model.Aggregate;
import poussecafe.doc.model.BoundedContext;
import poussecafe.doc.model.BoundedContextComponentDoc;
import poussecafe.doc.model.ComponentDoc;
import poussecafe.doc.model.Domain;
import poussecafe.doc.model.DomainFactory;
import poussecafe.doc.model.DomainProcessSteps;
import poussecafe.doc.model.DomainProcessStepsFactory;
import poussecafe.doc.model.UbiquitousLanguageEntry;
import poussecafe.doc.model.UbiquitousLanguageFactory;
import poussecafe.doc.model.aggregatedoc.AggregateDoc;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDoc;
import poussecafe.doc.model.domainprocessdoc.DomainProcessDoc;
import poussecafe.doc.model.domainprocessdoc.DomainProcessDocRepository;
import poussecafe.doc.model.domainprocessdoc.Step;
import poussecafe.doc.model.entitydoc.EntityDoc;
import poussecafe.doc.model.servicedoc.ServiceDoc;
import poussecafe.doc.model.servicedoc.ServiceDocRepository;
import poussecafe.doc.model.vodoc.ValueObjectDoc;

import static java.util.stream.Collectors.toList;

public class HtmlWriter {

    public void writeHtml() {
        try(FileWriter stream = new FileWriter(new File(configuration.outputDirectory(), "index.html"))) {
            copyCss();

            Configuration freemarkerConfig = new Configuration(Configuration.VERSION_2_3_28);
            freemarkerConfig.setClassForTemplateLoading(getClass(), "/");
            Template template = freemarkerConfig.getTemplate("index.html");

            Domain domain = domainFactory.buildDomain();
            HashMap<String, Object> domainMap = new HashMap<>();
            domainMap.put("name", domain.name());
            domainMap.put("version", domain.version());

            domainMap.put("boundedContexts",
                            domain.boundedContexts()
                                    .stream()
                                    .sorted(this::compareBoundedContexts)
                                    .map(this::adapt)
                                    .collect(toList()));

            HashMap<String, Object> model = new HashMap<>();
            model.put("includeGenerationDate", configuration.includeGenerationDate());
            model.put("domain", domainMap);
            model.put("generationDate", new Date());
            model.put("ubiquitousLanguage",
                            ubitquitousLanguageFactory
                                    .buildUbiquitousLanguage(domain)
                                    .stream()
                                    .filter(doc -> !doc.componentDoc().trivial())
                                    .map(this::adapt)
                                    .collect(toList()));
            template.process(model, stream);
        } catch (Exception e) {
            throw new RuntimeException("Error while writing HTML", e);
        }
    }

    private PousseCafeDocletConfiguration configuration;

    private DomainFactory domainFactory;

    private int compareBoundedContexts(BoundedContext boundedContextDoc1, BoundedContext boundedContextDoc2) {
        ComponentDoc doc1 = boundedContextDoc1.documentation().attributes().componentDoc().value();
        ComponentDoc doc2 = boundedContextDoc2.documentation().attributes().componentDoc().value();
        return compareTo(doc1, doc2);
    }

    private int compareTo(ComponentDoc componentDoc1,
            ComponentDoc componentDoc2) {
        return componentDoc1.name().compareTo(componentDoc2.name());
    }

    private HashMap<String, Object> adapt(BoundedContext boundedContext) {
        HashMap<String, Object> view = new HashMap<>();
        BoundedContextDoc boundedContextDoc = boundedContext.documentation();
        view.put("id", boundedContextDoc.id());
        view.put("name", boundedContextDoc.attributes().componentDoc().value().name());
        view.put("description", boundedContextDoc.attributes().componentDoc().value().description());

        view.put("aggregates", boundedContext.aggregates()
                .stream()
                .sorted(this::compareAggregates)
                .map(this::adapt)
                .collect(toList()));

        view.put("services", serviceDocRepository
                .findByBoundedContextId(boundedContextDoc.attributes().identifier().value())
                .stream()
                .sorted(this::compareServices)
                .map(this::adapt)
                .collect(toList()));

        view.put("domainProcesses", domainProcessDocRepository
                .findByBoundedContextId(boundedContextDoc.attributes().identifier().value())
                .stream()
                .sorted(this::compareDomainProcesses)
                .map(this::adapt)
                .collect(toList()));

        return view;
    }

    private int compareAggregates(Aggregate aggregateDoc1, Aggregate aggregateDoc2) {
        BoundedContextComponentDoc doc1 = aggregateDoc1.documentation().attributes().boundedContextComponentDoc().value();
        BoundedContextComponentDoc doc2 = aggregateDoc2.documentation().attributes().boundedContextComponentDoc().value();
        return compareTo(doc1, doc2);
    }

    private int compareTo(BoundedContextComponentDoc boundedContextComponentDoc1,
            BoundedContextComponentDoc boundedContextComponentDoc2) {
        return compareTo(boundedContextComponentDoc1.componentDoc(), boundedContextComponentDoc2.componentDoc());
    }

    private HashMap<String, Object> adapt(Aggregate aggregate) {
        HashMap<String, Object> view = new HashMap<>();
        AggregateDoc aggregateDoc = aggregate.documentation();
        view.put("id", aggregateDoc.id());
        view.put("name", aggregateDoc.attributes().boundedContextComponentDoc().value().componentDoc().name());
        view.put("description", aggregateDoc.attributes().boundedContextComponentDoc().value().componentDoc().description());

        view.put("entities", aggregate.entities().stream()
                .sorted(this::compareEntities)
                .map(this::adapt)
                .collect(toList()));

        view.put("valueObjects", aggregate.valueObjects().stream()
                .sorted(this::compareValueObjects)
                .map(this::adapt)
                .collect(toList()));

        return view;
    }

    private ServiceDocRepository serviceDocRepository;

    private int compareServices(ServiceDoc serviceDoc1, ServiceDoc serviceDoc2) {
        return compareTo(serviceDoc1.attributes().boundedContextComponentDoc().value(), serviceDoc2.attributes().boundedContextComponentDoc().value());
    }

    private DomainProcessDocRepository domainProcessDocRepository;

    private int compareDomainProcesses(DomainProcessDoc boundedContextDoc1, DomainProcessDoc boundedContextDoc2) {
        return compareTo(boundedContextDoc1.attributes().boundedContextComponentDoc().value(), boundedContextDoc2.attributes().boundedContextComponentDoc().value());
    }

    private int compareEntities(EntityDoc entityDoc1, EntityDoc entityDoc2) {
        return compareTo(entityDoc1.attributes().boundedContextComponentDoc().value(), entityDoc2.attributes().boundedContextComponentDoc().value());
    }

    private HashMap<String, Object> adapt(EntityDoc entityDoc) {
        HashMap<String, Object> view = new HashMap<>();
        view.put("id", entityDoc.id());
        view.put("name", entityDoc.attributes().boundedContextComponentDoc().value().componentDoc().name());
        view.put("description", entityDoc.attributes().boundedContextComponentDoc().value().componentDoc().description());
        return view;
    }

    private int compareValueObjects(ValueObjectDoc valueObjectDoc1, ValueObjectDoc valueObjectDoc2) {
        return compareTo(valueObjectDoc1.attributes().boundedContextComponentDoc().value(), valueObjectDoc2.attributes().boundedContextComponentDoc().value());
    }

    private HashMap<String, Object> adapt(ValueObjectDoc entityDoc) {
        HashMap<String, Object> view = new HashMap<>();
        view.put("id", entityDoc.id());
        view.put("name", entityDoc.attributes().boundedContextComponentDoc().value().componentDoc().name());
        view.put("description", entityDoc.attributes().boundedContextComponentDoc().value().componentDoc().description());
        return view;
    }

    private HashMap<String, Object> adapt(ServiceDoc serviceDoc) {
        HashMap<String, Object> view = new HashMap<>();
        view.put("id", serviceDoc.id());
        view.put("name", serviceDoc.attributes().boundedContextComponentDoc().value().componentDoc().name());
        view.put("description", serviceDoc.attributes().boundedContextComponentDoc().value().componentDoc().description());
        return view;
    }

    private HashMap<String, Object> adapt(DomainProcessDoc domainProcessDoc) {
        HashMap<String, Object> view = new HashMap<>();
        view.put("id", domainProcessDoc.id());
        view.put("name", domainProcessDoc.attributes().boundedContextComponentDoc().value().componentDoc().name());
        view.put("description", domainProcessDoc.attributes().boundedContextComponentDoc().value().componentDoc().description());
        DomainProcessSteps domainProcessSteps = domainProcessStepsFactory.buildDomainProcessSteps(domainProcessDoc);
        view.put("steps", domainProcessSteps.orderedSteps().stream()
                .filter(step -> !step.componentDoc().trivial())
                .filter(step -> !step.external())
                .map(this::adapt)
                .collect(toList()));
        return view;
    }

    private DomainProcessStepsFactory domainProcessStepsFactory;

    private HashMap<String, Object> adapt(Step step) {
        HashMap<String, Object> view = new HashMap<>();
        view.put("name", step.componentDoc().name());
        view.put("description", step.componentDoc().description());
        return view;
    }

    private HashMap<String, Object> adapt(UbiquitousLanguageEntry entry) {
        HashMap<String, Object> view = new HashMap<>();
        view.put("name", entry.qualifiedName());
        view.put("type", entry.getType());
        view.put("description", entry.componentDoc().shortDescription().orElse(entry.componentDoc().description()));
        return view;
    }

    private void copyCss()
            throws IOException {
        IOUtils.copy(getClass().getResourceAsStream("/style.css"),
                        new FileOutputStream(new File(configuration.outputDirectory(), "style.css")));
    }

    private UbiquitousLanguageFactory ubitquitousLanguageFactory;
}
