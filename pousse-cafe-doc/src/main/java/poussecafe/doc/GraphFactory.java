package poussecafe.doc;

import poussecafe.doc.graph.Node;
import poussecafe.doc.graph.NodeStyle;
import poussecafe.doc.graph.UndirectedGraph;
import poussecafe.doc.model.aggregatedoc.AggregateDoc;
import poussecafe.doc.model.aggregatedoc.AggregateDocRepository;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDoc;
import poussecafe.domain.Service;

public class GraphFactory implements Service {

    public UndirectedGraph buildBoundedContextGraph(
            BoundedContextDoc boundedContextDoc) {
        UndirectedGraph graph = new UndirectedGraph();
        addAggregates(graph, boundedContextDoc);
        return graph;
    }

    private void addAggregates(UndirectedGraph graph,
            BoundedContextDoc boundedContextDoc) {
        for (AggregateDoc aggregateDoc : aggregateDocRepository.findByBoundedContextKey(boundedContextDoc.getKey())) {
            addAggregate(graph, aggregateDoc);
        }
    }

    private AggregateDocRepository aggregateDocRepository;

    public UndirectedGraph buildAggregateGraph(AggregateDoc aggregateDoc) {
        UndirectedGraph graph = new UndirectedGraph();
        addAggregate(graph, aggregateDoc);
        return graph;
    }

    private void addAggregate(UndirectedGraph graph, AggregateDoc aggregateDoc) {
        Node node = Node.box(aggregateDoc.name());
        node.setStyle(NodeStyle.BOLD);
        graph.getNodesAndEdges().addNode(node);
    }
}
