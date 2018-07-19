package poussecafe.doc;

import java.io.File;
import java.io.IOException;
import java.util.List;
import poussecafe.doc.model.aggregatedoc.AggregateDoc;
import poussecafe.doc.model.aggregatedoc.AggregateDocRepository;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDoc;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDocRepository;

public class GraphsWriter {

    public GraphsWriter(RootDocWrapper rootDocWrapper) {
        this.rootDocWrapper = rootDocWrapper;
    }

    private RootDocWrapper rootDocWrapper;

    public void writeGraphs() {
        try {
            File outputDirectory = outputDirectory();
            List<BoundedContextDoc> boundedContextDocs = boundedContextDocRepository.findAll();
            for (BoundedContextDoc boundedContextDoc : boundedContextDocs) {
                rootDocWrapper.debug("Drawing BC " + boundedContextDoc.name() + " graph...");
                graphDrawer
                        .drawGraph(graphFactory.buildBoundedContextGraph(boundedContextDoc), outputDirectory,
                                boundedContextDoc.id());

                writeAggregatesGraphs(boundedContextDoc);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error while writing graphs", e);
        }
    }

    private File outputDirectory() {
        return new File(rootDocWrapper.outputPath());
    }

    private GraphDrawer graphDrawer = new GraphDrawer();

    private void writeAggregatesGraphs(BoundedContextDoc boundedContextDoc) throws IOException {
        File outputDirectory = outputDirectory();
        for (AggregateDoc aggregateDoc : aggregateDocRepository
                .findByBoundedContextKey(boundedContextDoc.getKey())) {
            rootDocWrapper.debug("Drawing aggregate " + aggregateDoc.name() + " graph...");
            graphDrawer
                    .drawGraph(graphFactory.buildAggregateGraph(aggregateDoc), outputDirectory,
                            boundedContextDoc.id() + "_" + aggregateDoc.id());
        }
    }

    private BoundedContextDocRepository boundedContextDocRepository;

    private AggregateDocRepository aggregateDocRepository;

    private GraphFactory graphFactory;
}
