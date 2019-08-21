package poussecafe.doc;

import java.io.File;
import java.io.IOException;
import java.util.List;
import poussecafe.doc.model.GraphFactory;
import poussecafe.doc.model.aggregatedoc.AggregateDoc;
import poussecafe.doc.model.aggregatedoc.AggregateDocRepository;
import poussecafe.doc.model.domainprocessdoc.DomainProcessDoc;
import poussecafe.doc.model.domainprocessdoc.DomainProcessDocRepository;
import poussecafe.doc.model.moduledoc.ModuleDoc;
import poussecafe.doc.model.moduledoc.ModuleDocRepository;

public class GraphImagesWriter {

    private static final String IMAGES_SUB_DIRECTORY = "img";

    public GraphImagesWriter(PousseCafeDocletConfiguration configuration) {
        this.configuration = configuration;
        graphImageWriter = new GraphImageWriter.Builder()
                .customDotExecutable(configuration.customDotExecutable())
                .customFdpExecutable(configuration.customFdpExecutable())
                .build();
    }

    private PousseCafeDocletConfiguration configuration;

    public void writeImages() {
        try {
            File outputDirectory = outputDirectory();
            List<ModuleDoc> moduleDocs = moduleDocRepository.findAll();
            for (ModuleDoc moduleDoc : moduleDocs) {
                Logger.debug("Drawing BC " + moduleDoc.attributes().componentDoc().value().name() + " graph...");
                graphImageWriter
                        .writeImage(graphFactory.buildModuleGraph(moduleDoc), outputDirectory,
                                moduleDoc.id());

                writeAggregatesGraphs(moduleDoc);
                writeDomainProcessesGraphs(moduleDoc);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error while writing graphs", e);
        }
    }

    private File outputDirectory() {
        File outputDirectory = new File(configuration.outputDirectory(), IMAGES_SUB_DIRECTORY);
        outputDirectory.mkdirs();
        return outputDirectory;
    }

    private GraphImageWriter graphImageWriter;

    private void writeAggregatesGraphs(ModuleDoc moduleDoc) throws IOException {
        File outputDirectory = outputDirectory();
        for (AggregateDoc aggregateDoc : aggregateDocRepository
                .findByModule(moduleDoc.attributes().identifier().value())) {
            Logger.debug("Drawing aggregate " + aggregateDoc.attributes().moduleComponentDoc().value().componentDoc().name() + " graph...");
            graphImageWriter
                    .writeImage(graphFactory.buildAggregateGraph(aggregateDoc), outputDirectory,
                            moduleDoc.id() + "_" + aggregateDoc.id());
        }
    }

    private ModuleDocRepository moduleDocRepository;

    private AggregateDocRepository aggregateDocRepository;

    private GraphFactory graphFactory;

    private void writeDomainProcessesGraphs(ModuleDoc moduleDoc) throws IOException {
        File outputDirectory = outputDirectory();
        for (DomainProcessDoc domainProcessDoc : domainProcessDocRepository
                .findByModuleId(moduleDoc.attributes().identifier().value())) {
            Logger.debug("Drawing domain process " + domainProcessDoc.attributes().moduleComponentDoc().value().componentDoc().name() + " graph...");
            graphImageWriter
                    .writeImage(graphFactory.buildDomainProcessGraph(domainProcessDoc), outputDirectory,
                            moduleDoc.id() + "_" + domainProcessDoc.id());
        }
    }

    private DomainProcessDocRepository domainProcessDocRepository;
}
