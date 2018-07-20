package poussecafe.doc;

import poussecafe.doc.graph.Node;
import poussecafe.doc.graph.NodeStyle;
import poussecafe.doc.graph.UndirectedEdge;
import poussecafe.doc.graph.UndirectedGraph;
import poussecafe.doc.model.aggregatedoc.AggregateDoc;
import poussecafe.doc.model.aggregatedoc.AggregateDocRepository;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDoc;
import poussecafe.doc.model.entitydoc.EntityDoc;
import poussecafe.doc.model.entitydoc.EntityDocRepository;
import poussecafe.domain.Service;

public class GraphFactory implements Service {

    public UndirectedGraph buildBoundedContextGraph(
            BoundedContextDoc boundedContextDoc) {
        UndirectedGraph graph = new UndirectedGraph();
        addSimpleAggregates(graph, boundedContextDoc);
        return graph;
    }

    private void addSimpleAggregates(UndirectedGraph graph,
            BoundedContextDoc boundedContextDoc) {
        for (AggregateDoc aggregateDoc : aggregateDocRepository.findByBoundedContextKey(boundedContextDoc.getKey())) {
            addSimpleAggregate(graph, aggregateDoc);
        }
    }

    private void addSimpleAggregate(UndirectedGraph graph, AggregateDoc aggregateDoc) {
        Node node = Node.box(aggregateDoc.name());
        node.setStyle(NodeStyle.BOLD);
        graph.getNodesAndEdges().addNode(node);
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

        addEntities(graph, aggregateDoc);
    }

    private void addEntities(UndirectedGraph graph,
            AggregateDoc aggregateDoc) {
        for(EntityDoc entityDoc : entityDocRepository.findByAggregateDocKey(aggregateDoc.getKey())) {
            Node entityNode = Node.box(entityDoc.name());
            graph.getNodesAndEdges().addNode(entityNode);

            UndirectedEdge edge = UndirectedEdge.solidEdge(aggregateDoc.name(), entityDoc.name());
            graph.getNodesAndEdges().addEdge(edge);
        }
    }

    private EntityDocRepository entityDocRepository;
}
