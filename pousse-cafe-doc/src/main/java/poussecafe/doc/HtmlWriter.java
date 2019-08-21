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
import poussecafe.doc.model.Module;
import poussecafe.doc.model.ModuleComponentDoc;
import poussecafe.doc.model.ComponentDoc;
import poussecafe.doc.model.Domain;
import poussecafe.doc.model.DomainFactory;
import poussecafe.doc.model.DomainProcessSteps;
import poussecafe.doc.model.DomainProcessStepsFactory;
import poussecafe.doc.model.UbiquitousLanguageEntry;
import poussecafe.doc.model.UbiquitousLanguageFactory;
import poussecafe.doc.model.aggregatedoc.AggregateDoc;
import poussecafe.doc.model.domainprocessdoc.DomainProcessDoc;
import poussecafe.doc.model.domainprocessdoc.DomainProcessDocRepository;
import poussecafe.doc.model.domainprocessdoc.Step;
import poussecafe.doc.model.entitydoc.EntityDoc;
import poussecafe.doc.model.moduledoc.ModuleDoc;
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

            domainMap.put("modules",
                            domain.modules()
                                    .stream()
                                    .sorted(this::compareModules)
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

    private int compareModules(Module moduleDoc1, Module moduleDoc2) {
        ComponentDoc doc1 = moduleDoc1.documentation().attributes().componentDoc().value();
        ComponentDoc doc2 = moduleDoc2.documentation().attributes().componentDoc().value();
        return compareTo(doc1, doc2);
    }

    private int compareTo(ComponentDoc componentDoc1,
            ComponentDoc componentDoc2) {
        return componentDoc1.name().compareTo(componentDoc2.name());
    }

    private HashMap<String, Object> adapt(Module module) {
        HashMap<String, Object> view = new HashMap<>();
        ModuleDoc moduleDoc = module.documentation();
        view.put("id", moduleDoc.id());
        view.put("name", moduleDoc.attributes().componentDoc().value().name());
        view.put("description", moduleDoc.attributes().componentDoc().value().description());

        view.put("aggregates", module.aggregates()
                .stream()
                .filter(aggregate -> !aggregate.documentation().attributes().moduleComponentDoc().value().componentDoc().trivial())
                .sorted(this::compareAggregates)
                .map(this::adapt)
                .collect(toList()));

        view.put("services", serviceDocRepository
                .findByModuleId(moduleDoc.attributes().identifier().value())
                .stream()
                .filter(doc -> !doc.attributes().moduleComponentDoc().value().componentDoc().trivial())
                .sorted(this::compareServices)
                .map(this::adapt)
                .collect(toList()));

        view.put("domainProcesses", domainProcessDocRepository
                .findByModuleId(moduleDoc.attributes().identifier().value())
                .stream()
                .filter(doc -> !doc.attributes().moduleComponentDoc().value().componentDoc().trivial())
                .sorted(this::compareDomainProcesses)
                .map(this::adapt)
                .collect(toList()));

        return view;
    }

    private int compareAggregates(Aggregate aggregateDoc1, Aggregate aggregateDoc2) {
        ModuleComponentDoc doc1 = aggregateDoc1.documentation().attributes().moduleComponentDoc().value();
        ModuleComponentDoc doc2 = aggregateDoc2.documentation().attributes().moduleComponentDoc().value();
        return compareTo(doc1, doc2);
    }

    private int compareTo(ModuleComponentDoc moduleComponentDoc1,
            ModuleComponentDoc moduleComponentDoc2) {
        return compareTo(moduleComponentDoc1.componentDoc(), moduleComponentDoc2.componentDoc());
    }

    private HashMap<String, Object> adapt(Aggregate aggregate) {
        HashMap<String, Object> view = new HashMap<>();
        AggregateDoc aggregateDoc = aggregate.documentation();
        view.put("id", aggregateDoc.id());
        view.put("name", aggregateDoc.attributes().moduleComponentDoc().value().componentDoc().name());
        view.put("description", aggregateDoc.attributes().moduleComponentDoc().value().componentDoc().description());

        view.put("entities", aggregate.entities().stream()
                .filter(doc -> !doc.attributes().moduleComponentDoc().value().componentDoc().trivial())
                .sorted(this::compareEntities)
                .map(this::adapt)
                .collect(toList()));

        view.put("valueObjects", aggregate.valueObjects().stream()
                .filter(doc -> !doc.attributes().moduleComponentDoc().value().componentDoc().trivial())
                .sorted(this::compareValueObjects)
                .map(this::adapt)
                .collect(toList()));

        return view;
    }

    private ServiceDocRepository serviceDocRepository;

    private int compareServices(ServiceDoc serviceDoc1, ServiceDoc serviceDoc2) {
        return compareTo(serviceDoc1.attributes().moduleComponentDoc().value(), serviceDoc2.attributes().moduleComponentDoc().value());
    }

    private DomainProcessDocRepository domainProcessDocRepository;

    private int compareDomainProcesses(DomainProcessDoc moduleDoc1, DomainProcessDoc moduleDoc2) {
        return compareTo(moduleDoc1.attributes().moduleComponentDoc().value(), moduleDoc2.attributes().moduleComponentDoc().value());
    }

    private int compareEntities(EntityDoc entityDoc1, EntityDoc entityDoc2) {
        return compareTo(entityDoc1.attributes().moduleComponentDoc().value(), entityDoc2.attributes().moduleComponentDoc().value());
    }

    private HashMap<String, Object> adapt(EntityDoc entityDoc) {
        HashMap<String, Object> view = new HashMap<>();
        view.put("id", entityDoc.id());
        view.put("name", entityDoc.attributes().moduleComponentDoc().value().componentDoc().name());
        view.put("description", entityDoc.attributes().moduleComponentDoc().value().componentDoc().description());
        return view;
    }

    private int compareValueObjects(ValueObjectDoc valueObjectDoc1, ValueObjectDoc valueObjectDoc2) {
        return compareTo(valueObjectDoc1.attributes().moduleComponentDoc().value(), valueObjectDoc2.attributes().moduleComponentDoc().value());
    }

    private HashMap<String, Object> adapt(ValueObjectDoc entityDoc) {
        HashMap<String, Object> view = new HashMap<>();
        view.put("id", entityDoc.id());
        view.put("name", entityDoc.attributes().moduleComponentDoc().value().componentDoc().name());
        view.put("description", entityDoc.attributes().moduleComponentDoc().value().componentDoc().description());
        return view;
    }

    private HashMap<String, Object> adapt(ServiceDoc serviceDoc) {
        HashMap<String, Object> view = new HashMap<>();
        view.put("id", serviceDoc.id());
        view.put("name", serviceDoc.attributes().moduleComponentDoc().value().componentDoc().name());
        view.put("description", serviceDoc.attributes().moduleComponentDoc().value().componentDoc().description());
        return view;
    }

    private HashMap<String, Object> adapt(DomainProcessDoc domainProcessDoc) {
        HashMap<String, Object> view = new HashMap<>();
        view.put("id", domainProcessDoc.id());
        view.put("name", domainProcessDoc.attributes().moduleComponentDoc().value().componentDoc().name());
        view.put("description", domainProcessDoc.attributes().moduleComponentDoc().value().componentDoc().description());
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
