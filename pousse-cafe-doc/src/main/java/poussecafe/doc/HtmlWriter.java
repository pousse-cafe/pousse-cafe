package poussecafe.doc;

import freemarker.template.Configuration;
import freemarker.template.Template;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.apache.commons.io.IOUtils;
import poussecafe.doc.model.BoundedContextDoc;
import poussecafe.doc.model.BoundedContextDocRepository;

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

            Configuration freemarkerConfig = new Configuration(Configuration.VERSION_2_3_23);
            freemarkerConfig.setClassForTemplateLoading(getClass(), "/");
            Template template = freemarkerConfig.getTemplate("index.html");

            HashMap<String, Object> domain = new HashMap<>();
            domain.put("name", rootDocWrapper.domainName());
            domain.put("version", rootDocWrapper.version());
            domain.put("boundedContexts", boundedContextDocRepository.findAll());

            HashMap<String, Object> model = new HashMap<>();
            model.put("domain", domain);
            model.put("generationDate", new Date());
            model.put("ubiquitousLanguage", buildUbiquitousLanguage());
            template.process(model, stream);

            stream.close();
        } catch (Exception e) {
            throw new RuntimeException("Error while writing HTML", e);
        }
    }

    private List<UbiquitousLanguageEntry> buildUbiquitousLanguage() {
        List<UbiquitousLanguageEntry> language = new ArrayList<>();
        for (BoundedContextDoc boundedContext : boundedContextDocRepository.findAll()) {
            language
                    .add(new UbiquitousLanguageEntry(boundedContext.name(), "Bounded Context",
                            boundedContext.description()));
        }
        Collections.sort(language);
        return language;
    }

    private BoundedContextDocRepository boundedContextDocRepository;

    private void copyCss()
            throws IOException {
        IOUtils.copy(getClass().getResourceAsStream("/style.css"),
                        new FileOutputStream(new File(rootDocWrapper.outputPath(), "style.css")));
    }

}
