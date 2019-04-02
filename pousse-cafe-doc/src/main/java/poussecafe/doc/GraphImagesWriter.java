package poussecafe.doc;

import java.io.File;
import java.io.IOException;
import java.util.List;
import poussecafe.doc.model.GraphFactory;
import poussecafe.doc.model.aggregatedoc.AggregateDoc;
import poussecafe.doc.model.aggregatedoc.AggregateDocRepository;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDoc;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDocRepository;
import poussecafe.doc.model.domainprocessdoc.DomainProcessDoc;
import poussecafe.doc.model.domainprocessdoc.DomainProcessDocRepository;

public class GraphImagesWriter {

    private static final String IMAGES_SUB_DIRECTORY = "img";

    public GraphImagesWriter(RootDocWrapper rootDocWrapper) {
        this.rootDocWrapper = rootDocWrapper;
    }

    private RootDocWrapper rootDocWrapper;

    public void writeImages() {
        try {
            File outputDirectory = outputDirectory();
            List<BoundedContextDoc> boundedContextDocs = boundedContextDocRepository.findAll();
            for (BoundedContextDoc boundedContextDoc : boundedContextDocs) {
                Logger.debug("Drawing BC " + boundedContextDoc.attributes().componentDoc().value().name() + " graph...");
                graphImageWriter
                        .writeImage(graphFactory.buildBoundedContextGraph(boundedContextDoc), outputDirectory,
                                boundedContextDoc.id());

                writeAggregatesGraphs(boundedContextDoc);
                writeDomainProcessesGraphs(boundedContextDoc);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error while writing graphs", e);
        }
    }

    private File outputDirectory() {
        File outputDirectory = new File(rootDocWrapper.outputPath(), IMAGES_SUB_DIRECTORY);
        outputDirectory.mkdirs();
        return outputDirectory;
    }

    private GraphImageWriter graphImageWriter = new GraphImageWriter();

    private void writeAggregatesGraphs(BoundedContextDoc boundedContextDoc) throws IOException {
        File outputDirectory = outputDirectory();
        for (AggregateDoc aggregateDoc : aggregateDocRepository
                .findByBoundedContextId(boundedContextDoc.attributes().identifier().value())) {
            Logger.debug("Drawing aggregate " + aggregateDoc.attributes().boundedContextComponentDoc().value().componentDoc().name() + " graph...");
            graphImageWriter
                    .writeImage(graphFactory.buildAggregateGraph(aggregateDoc), outputDirectory,
                            boundedContextDoc.id() + "_" + aggregateDoc.id());
        }
    }

    private BoundedContextDocRepository boundedContextDocRepository;

    private AggregateDocRepository aggregateDocRepository;

    private GraphFactory graphFactory;

    private void writeDomainProcessesGraphs(BoundedContextDoc boundedContextDoc) throws IOException {
        File outputDirectory = outputDirectory();
        for (DomainProcessDoc domainProcessDoc : domainProcessDocRepository
                .findByBoundedContextId(boundedContextDoc.attributes().identifier().value())) {
            Logger.debug("Drawing domain process " + domainProcessDoc.attributes().boundedContextComponentDoc().value().componentDoc().name() + " graph...");
            graphImageWriter
                    .writeImage(graphFactory.buildDomainProcessGraph(domainProcessDoc), outputDirectory,
                            boundedContextDoc.id() + "_" + domainProcessDoc.id());
        }
    }

    private DomainProcessDocRepository domainProcessDocRepository;
}
