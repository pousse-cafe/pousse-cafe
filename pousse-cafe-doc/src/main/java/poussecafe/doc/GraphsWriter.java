package poussecafe.doc;

import java.io.File;
import java.io.IOException;
import java.util.List;
import poussecafe.doc.model.BoundedContextDoc;
import poussecafe.doc.model.BoundedContextDocRepository;

public class GraphsWriter {

    public GraphsWriter(RootDocWrapper rootDocWrapper) {
        this.rootDocWrapper = rootDocWrapper;
    }

    private RootDocWrapper rootDocWrapper;

    public void writeGraphs() {
        GraphFactory graphBuilder = new GraphFactory();
        GraphDrawer graphDrawer = new GraphDrawer();

        try {
            File outputDirectory = new File(rootDocWrapper.outputPath());
            List<BoundedContextDoc> boundedContextDocs = boundedContextDocRepository.findAll();
            rootDocWrapper.debug("Drawing all_entities graph...");
            graphDrawer.drawGraph(graphBuilder.buildFullGraph(boundedContextDocs), outputDirectory, "all_entities");
            for (BoundedContextDoc boundedContextDoc : boundedContextDocs) {
                rootDocWrapper.debug("Drawing BC " + boundedContextDoc.name() + " graph...");
                graphDrawer.drawGraph(graphBuilder.buildBoundedContextGraph(boundedContextDoc), outputDirectory, boundedContextDoc.id());
            }
        } catch (IOException e) {
            throw new RuntimeException("Error while writing graphs", e);
        }
    }

    private BoundedContextDocRepository boundedContextDocRepository;
}
