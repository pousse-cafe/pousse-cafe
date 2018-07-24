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
import java.util.Set;
import org.apache.commons.io.IOUtils;
import poussecafe.doc.model.UbiquitousLanguageEntry;
import poussecafe.doc.model.UbiquitousLanguageFactory;
import poussecafe.doc.model.aggregatedoc.AggregateDoc;
import poussecafe.doc.model.aggregatedoc.AggregateDocKey;
import poussecafe.doc.model.aggregatedoc.AggregateDocRepository;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDoc;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDocRepository;
import poussecafe.doc.model.domainprocessdoc.DomainProcessDoc;
import poussecafe.doc.model.domainprocessdoc.DomainProcessDocRepository;
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
import static poussecafe.check.Checks.checkThatValue;

public class HtmlWriter {

    public HtmlWriter(RootDocWrapper rootDocWrapper) {
        checkThatValue(rootDocWrapper).notNull();
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
            domain.put("boundedContexts", boundedContextDocs.stream().map(this::adapt).collect(toList()));

            HashMap<String, Object> model = new HashMap<>();
            model.put("domain", domain);
            model.put("generationDate", new Date());
            model.put("ubiquitousLanguage",
                            ubitquitousLanguageFactory
                                    .buildUbiquitousLanguage()
                                    .stream()
                                    .map(this::adapt)
                                    .collect(toList()));
            template.process(model, stream);

            stream.close();
        } catch (Exception e) {
            throw new RuntimeException("Error while writing HTML", e);
        }
    }

    private BoundedContextDocRepository boundedContextDocRepository;

    private HashMap<String, Object> adapt(BoundedContextDoc boundedContextDoc) {
        HashMap<String, Object> view = new HashMap<>();
        view.put("id", boundedContextDoc.id());
        view.put("name", boundedContextDoc.componentDoc().name());
        view.put("description", boundedContextDoc.componentDoc().description());

        view.put("aggregates", aggregateDocRepository
                .findByBoundedContextKey(boundedContextDoc.getKey())
                .stream()
                .map(this::adapt)
                .collect(toList()));

        view.put("services", serviceDocRepository
                .findByBoundedContextKey(boundedContextDoc.getKey())
                .stream()
                .map(this::adapt)
                .collect(toList()));

        view.put("domainProcesses", domainProcessDocRepository
                .findByBoundedContextKey(boundedContextDoc.getKey())
                .stream()
                .map(this::adapt)
                .collect(toList()));

        return view;
    }

    private AggregateDocRepository aggregateDocRepository;

    private ServiceDocRepository serviceDocRepository;

    private DomainProcessDocRepository domainProcessDocRepository;

    private HashMap<String, Object> adapt(AggregateDoc aggregateDoc) {
        HashMap<String, Object> view = new HashMap<>();
        view.put("id", aggregateDoc.id());
        view.put("name", aggregateDoc.boundedContextComponentDoc().componentDoc().name());
        view.put("description", aggregateDoc.boundedContextComponentDoc().componentDoc().description());

        view.put("entities", findEntities(aggregateDoc.getKey()).stream().map(this::adapt).collect(toList()));

        view.put("valueObjects", findValueObjects(aggregateDoc.getKey()).stream().map(this::adapt).collect(toList()));

        return view;
    }

    private List<EntityDoc> findEntities(AggregateDocKey aggregateDocKey) {
        return findEntities(aggregateDocKey.getValue()).stream().map(entityDocRepository::get).collect(toList());
    }

    private Set<EntityDocKey> findEntities(String fromClassName) {
        Set<EntityDocKey> keys = new HashSet<>();
        for(Relation relation : relationRepository.findWithFromClassName(fromClassName)) {
            if(relation.toComponent().type() == ComponentType.ENTITY) {
                keys.add(EntityDocKey.ofClassName(relation.toComponent().className()));
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
        return findValueObjects(aggregateDocKey.getValue()).stream().map(valueObjectDocRepository::get).collect(toList());
    }

    private Set<ValueObjectDocKey> findValueObjects(String fromClassName) {
        Set<ValueObjectDocKey> keys = new HashSet<>();
        for(Relation relation : relationRepository.findWithFromClassName(fromClassName)) {
            if(relation.toComponent().type() == ComponentType.VALUE_OBJECT) {
                keys.add(ValueObjectDocKey.ofClassName(relation.toComponent().className()));
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
        return view;
    }

    private HashMap<String, Object> adapt(UbiquitousLanguageEntry entry) {
        HashMap<String, Object> view = new HashMap<>();
        view.put("name", entry.qualifiedName());
        view.put("type", entry.getType());
        view.put("description", entry.componentDoc().description());
        return view;
    }

    private void copyCss()
            throws IOException {
        IOUtils.copy(getClass().getResourceAsStream("/style.css"),
                        new FileOutputStream(new File(rootDocWrapper.outputPath(), "style.css")));
    }

    private UbiquitousLanguageFactory ubitquitousLanguageFactory;
}
