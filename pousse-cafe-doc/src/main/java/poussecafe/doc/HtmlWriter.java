package poussecafe.doc;

import freemarker.template.Configuration;
import freemarker.template.Template;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.apache.commons.io.IOUtils;
import poussecafe.doc.model.UbiquitousLanguageFactory;
import poussecafe.doc.model.aggregatedoc.AggregateDoc;
import poussecafe.doc.model.aggregatedoc.AggregateDocRepository;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDoc;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDocRepository;
import poussecafe.doc.model.entitydoc.EntityDoc;
import poussecafe.doc.model.entitydoc.EntityDocRepository;
import poussecafe.doc.model.servicedoc.ServiceDoc;
import poussecafe.doc.model.servicedoc.ServiceDocRepository;

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
            model.put("ubiquitousLanguage", ubitquitousLanguageFactory.buildUbiquitousLanguage());
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
        view.put("name", boundedContextDoc.name());
        view.put("description", boundedContextDoc.description());

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
        return view;
    }

    private AggregateDocRepository aggregateDocRepository;

    private ServiceDocRepository serviceDocRepository;

    private HashMap<String, Object> adapt(AggregateDoc aggregateDoc) {
        HashMap<String, Object> view = new HashMap<>();
        view.put("id", aggregateDoc.id());
        view.put("name", aggregateDoc.name());
        view.put("description", aggregateDoc.description());

        view.put("entities", entityDocRepository
                .findByAggregateDocKey(aggregateDoc.getKey())
                .stream()
                .map(this::adapt)
                .collect(toList()));

        return view;
    }

    private EntityDocRepository entityDocRepository;

    private HashMap<String, Object> adapt(EntityDoc entityDoc) {
        HashMap<String, Object> view = new HashMap<>();
        view.put("id", entityDoc.id());
        view.put("name", entityDoc.name());
        view.put("description", entityDoc.description());
        return view;
    }

    private HashMap<String, Object> adapt(ServiceDoc serviceDoc) {
        HashMap<String, Object> view = new HashMap<>();
        view.put("id", serviceDoc.id());
        view.put("name", serviceDoc.name());
        view.put("description", serviceDoc.description());
        return view;
    }

    private void copyCss()
            throws IOException {
        IOUtils.copy(getClass().getResourceAsStream("/style.css"),
                        new FileOutputStream(new File(rootDocWrapper.outputPath(), "style.css")));
    }

    private UbiquitousLanguageFactory ubitquitousLanguageFactory;
}
