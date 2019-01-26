package poussecafe.doc;

import freemarker.template.Configuration;
import freemarker.template.Template;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import org.apache.commons.io.IOUtils;
import poussecafe.doc.model.BoundedContextComponentDoc;
import poussecafe.doc.model.ComponentDoc;
import poussecafe.doc.model.UbiquitousLanguageEntry;
import poussecafe.doc.model.UbiquitousLanguageFactory;
import poussecafe.doc.model.aggregatedoc.AggregateDoc;
import poussecafe.doc.model.aggregatedoc.AggregateDocKey;
import poussecafe.doc.model.aggregatedoc.AggregateDocRepository;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDoc;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDocRepository;
import poussecafe.doc.model.domainprocessdoc.DomainProcessDoc;
import poussecafe.doc.model.domainprocessdoc.DomainProcessDocRepository;
import poussecafe.doc.model.domainprocessdoc.Step;
import poussecafe.doc.model.entitydoc.EntityDoc;
import poussecafe.doc.model.entitydoc.EntityDocKey;
import poussecafe.doc.model.entitydoc.EntityDocRepository;
import poussecafe.doc.model.relation.ComponentType;
import poussecafe.doc.model.relation.Relation;
import poussecafe.doc.model.relation.RelationRepository;
import poussecafe.doc.model.servicedoc.ServiceDoc;
import poussecafe.doc.model.servicedoc.ServiceDocRepository;
import poussecafe.doc.model.vodoc.ValueObjectDoc;
import poussecafe.doc.model.vodoc.ValueObjectDocKey;
import poussecafe.doc.model.vodoc.ValueObjectDocRepository;

import static java.util.stream.Collectors.toList;
import java.util.Objects;

public class HtmlWriter {

    public HtmlWriter(RootDocWrapper rootDocWrapper) {
        Objects.requireNonNull(rootDocWrapper);
        this.rootDocWrapper = rootDocWrapper;
    }

    private RootDocWrapper rootDocWrapper;

    public void writeHtml() {
        try {
            FileWriter stream = new FileWriter(new File(rootDocWrapper.outputPath(), "index.html"));
            copyCss();

            Configuration freemarkerConfig = new Configuration(Configuration.VERSION_2_3_28);
            freemarkerConfig.setClassForTemplateLoading(getClass(), "/");
            Template template = freemarkerConfig.getTemplate("index.html");

            HashMap<String, Object> domain = new HashMap<>();
            domain.put("name", rootDocWrapper.domainName());
            domain.put("version", rootDocWrapper.version());

            List<BoundedContextDoc> boundedContextDocs = boundedContextDocRepository.findAll();
            domain.put("boundedContexts",
                            boundedContextDocs
                                    .stream()
                                    .sorted(this::compareBoundedContexts)
                                    .map(this::adapt)
                                    .collect(toList()));

            HashMap<String, Object> model = new HashMap<>();
            model.put("domain", domain);
            model.put("generationDate", new Date());
            model.put("ubiquitousLanguage",
                            ubitquitousLanguageFactory
                                    .buildUbiquitousLanguage()
                                    .stream()
                                    .filter(doc -> !doc.componentDoc().trivial())
                                    .map(this::adapt)
                                    .collect(toList()));
            template.process(model, stream);

            stream.close();
        } catch (Exception e) {
            throw new RuntimeException("Error while writing HTML", e);
        }
    }

    private BoundedContextDocRepository boundedContextDocRepository;

    private int compareBoundedContexts(BoundedContextDoc boundedContextDoc1, BoundedContextDoc boundedContextDoc2) {
        return compareTo(boundedContextDoc1.componentDoc(), boundedContextDoc2.componentDoc());
    }

    private int compareTo(ComponentDoc componentDoc1,
            ComponentDoc componentDoc2) {
        return componentDoc1.name().compareTo(componentDoc2.name());
    }

    private HashMap<String, Object> adapt(BoundedContextDoc boundedContextDoc) {
        HashMap<String, Object> view = new HashMap<>();
        view.put("id", boundedContextDoc.id());
        view.put("name", boundedContextDoc.componentDoc().name());
        view.put("description", boundedContextDoc.componentDoc().description());

        view.put("aggregates", aggregateDocRepository
                .findByBoundedContextKey(boundedContextDoc.getKey())
                .stream()
                .sorted(this::compareAggregates)
                .map(this::adapt)
                .collect(toList()));

        view.put("services", serviceDocRepository
                .findByBoundedContextKey(boundedContextDoc.getKey())
                .stream()
                .sorted(this::compareServices)
                .map(this::adapt)
                .collect(toList()));

        view.put("domainProcesses", domainProcessDocRepository
                .findByBoundedContextKey(boundedContextDoc.getKey())
                .stream()
                .sorted(this::compareDomainProcesses)
                .map(this::adapt)
                .collect(toList()));

        return view;
    }

    private AggregateDocRepository aggregateDocRepository;

    private int compareAggregates(AggregateDoc aggregateDoc1, AggregateDoc aggregateDoc2) {
        return compareTo(aggregateDoc1.boundedContextComponentDoc(), aggregateDoc2.boundedContextComponentDoc());
    }

    private int compareTo(BoundedContextComponentDoc boundedContextComponentDoc1,
            BoundedContextComponentDoc boundedContextComponentDoc2) {
        return compareTo(boundedContextComponentDoc1.componentDoc(), boundedContextComponentDoc2.componentDoc());
    }

    private ServiceDocRepository serviceDocRepository;

    private int compareServices(ServiceDoc serviceDoc1, ServiceDoc serviceDoc2) {
        return compareTo(serviceDoc1.boundedContextComponentDoc(), serviceDoc2.boundedContextComponentDoc());
    }

    private DomainProcessDocRepository domainProcessDocRepository;

    private int compareDomainProcesses(DomainProcessDoc boundedContextDoc1, DomainProcessDoc boundedContextDoc2) {
        return compareTo(boundedContextDoc1.boundedContextComponentDoc(), boundedContextDoc2.boundedContextComponentDoc());
    }

    private HashMap<String, Object> adapt(AggregateDoc aggregateDoc) {
        HashMap<String, Object> view = new HashMap<>();
        view.put("id", aggregateDoc.id());
        view.put("name", aggregateDoc.boundedContextComponentDoc().componentDoc().name());
        view.put("description", aggregateDoc.boundedContextComponentDoc().componentDoc().description());

        view.put("entities", findEntities(aggregateDoc.getKey()).stream()
                .sorted(this::compareEntities)
                .map(this::adapt)
                .collect(toList()));

        view.put("valueObjects", findValueObjects(aggregateDoc.getKey()).stream()
                .sorted(this::compareValueObjects)
                .map(this::adapt)
                .collect(toList()));

        return view;
    }

    private List<EntityDoc> findEntities(AggregateDocKey aggregateDocKey) {
        return findEntities(aggregateDocKey.getValue()).stream()
                .map(entityDocRepository::get)
                .filter(doc -> !doc.boundedContextComponentDoc().componentDoc().trivial())
                .collect(toList());
    }

    private int compareEntities(EntityDoc entityDoc1, EntityDoc entityDoc2) {
        return compareTo(entityDoc1.boundedContextComponentDoc(), entityDoc2.boundedContextComponentDoc());
    }

    private Set<EntityDocKey> findEntities(String fromClassName) {
        Set<EntityDocKey> keys = new HashSet<>();
        for(Relation relation : relationRepository.findWithFromClassName(fromClassName)) {
            if(relation.toComponent().type() == ComponentType.ENTITY) {
                keys.add(EntityDocKey.ofClassName(relation.toComponent().className()));
            }
            if(relation.toComponent().type() != ComponentType.AGGREGATE) {
                keys.addAll(findEntities(relation.toComponent().className()));
            }
        }
        return keys;
    }

    private RelationRepository relationRepository;

    private EntityDocRepository entityDocRepository;

    private HashMap<String, Object> adapt(EntityDoc entityDoc) {
        HashMap<String, Object> view = new HashMap<>();
        view.put("id", entityDoc.id());
        view.put("name", entityDoc.boundedContextComponentDoc().componentDoc().name());
        view.put("description", entityDoc.boundedContextComponentDoc().componentDoc().description());
        return view;
    }

    private List<ValueObjectDoc> findValueObjects(AggregateDocKey aggregateDocKey) {
        return findValueObjects(aggregateDocKey.getValue()).stream()
                .map(valueObjectDocRepository::find)
                .filter(Objects::nonNull)
                .filter(doc -> !doc.boundedContextComponentDoc().componentDoc().trivial())
                .collect(toList());
    }

    private int compareValueObjects(ValueObjectDoc valueObjectDoc1, ValueObjectDoc valueObjectDoc2) {
        return compareTo(valueObjectDoc1.boundedContextComponentDoc(), valueObjectDoc2.boundedContextComponentDoc());
    }

    private Set<ValueObjectDocKey> findValueObjects(String fromClassName) {
        Set<ValueObjectDocKey> keys = new HashSet<>();
        for(Relation relation : relationRepository.findWithFromClassName(fromClassName)) {
            if(relation.toComponent().type() == ComponentType.VALUE_OBJECT) {
                keys.add(ValueObjectDocKey.ofClassName(relation.toComponent().className()));
            }
            if(relation.toComponent().type() != ComponentType.AGGREGATE) {
                keys.addAll(findValueObjects(relation.toComponent().className()));
            }
        }
        return keys;
    }

    private ValueObjectDocRepository valueObjectDocRepository;

    private HashMap<String, Object> adapt(ValueObjectDoc entityDoc) {
        HashMap<String, Object> view = new HashMap<>();
        view.put("id", entityDoc.id());
        view.put("name", entityDoc.boundedContextComponentDoc().componentDoc().name());
        view.put("description", entityDoc.boundedContextComponentDoc().componentDoc().description());
        return view;
    }

    private HashMap<String, Object> adapt(ServiceDoc serviceDoc) {
        HashMap<String, Object> view = new HashMap<>();
        view.put("id", serviceDoc.id());
        view.put("name", serviceDoc.boundedContextComponentDoc().componentDoc().name());
        view.put("description", serviceDoc.boundedContextComponentDoc().componentDoc().description());
        return view;
    }

    private HashMap<String, Object> adapt(DomainProcessDoc domainProcessDoc) {
        HashMap<String, Object> view = new HashMap<>();
        view.put("id", domainProcessDoc.id());
        view.put("name", domainProcessDoc.boundedContextComponentDoc().componentDoc().name());
        view.put("description", domainProcessDoc.boundedContextComponentDoc().componentDoc().description());
        view.put("steps", domainProcessDoc.orderedSteps().stream().filter(step -> !step.external()).map(this::adapt).collect(toList()));
        return view;
    }

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
                        new FileOutputStream(new File(rootDocWrapper.outputPath(), "style.css")));
    }

    private UbiquitousLanguageFactory ubitquitousLanguageFactory;
}
