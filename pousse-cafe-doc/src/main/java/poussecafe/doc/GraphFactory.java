package poussecafe.doc;

import java.util.List;
import poussecafe.doc.graph.UndirectedGraph;
import poussecafe.doc.graph.UndirectedSubGraph;
import poussecafe.doc.model.BoundedContextDoc;

public class GraphFactory {

    public UndirectedGraph buildFullGraph(List<BoundedContextDoc> boundedContextDocs) {
        UndirectedGraph graph = new UndirectedGraph();
        for (BoundedContextDoc boundedContextDoc : boundedContextDocs) {
            addBoundedContextSubGraph(graph, boundedContextDoc);
        }
        return graph;
    }

    public UndirectedGraph buildBoundedContextGraph(
            BoundedContextDoc boundedContextDoc) {
        UndirectedGraph graph = new UndirectedGraph();
        addBoundedContextSubGraph(graph, boundedContextDoc);
        return graph;
    }

    private void addBoundedContextSubGraph(UndirectedGraph graph,
            BoundedContextDoc boundedContextDoc) {
        UndirectedSubGraph boundedContextGraph = new UndirectedSubGraph();
        boundedContextGraph.setName(boundedContextDoc.name());
        graph.addSubGraph(boundedContextGraph);
    }
}
