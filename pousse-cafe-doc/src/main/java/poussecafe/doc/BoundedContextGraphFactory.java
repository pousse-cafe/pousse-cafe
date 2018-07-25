package poussecafe.doc;

import poussecafe.doc.graph.Node;
import poussecafe.doc.graph.NodeStyle;
import poussecafe.doc.graph.UndirectedEdge;
import poussecafe.doc.graph.UndirectedGraph;
import poussecafe.doc.model.aggregatedoc.AggregateDoc;
import poussecafe.doc.model.aggregatedoc.AggregateDocKey;
import poussecafe.doc.model.aggregatedoc.AggregateDocRepository;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDoc;
import poussecafe.doc.model.relation.Component;
import poussecafe.doc.model.relation.ComponentType;
import poussecafe.doc.model.relation.Relation;
import poussecafe.doc.model.relation.RelationRepository;

import static poussecafe.check.Checks.checkThatValue;

public class BoundedContextGraphFactory {

    public static class Builder {

        private BoundedContextGraphFactory factory = new BoundedContextGraphFactory();

        public Builder boundedContextDoc(BoundedContextDoc boundedContextDoc) {
            factory.boundedContextDoc = boundedContextDoc;
            return this;
        }

        public Builder relationRepository(RelationRepository relationRepository) {
            factory.relationRepository = relationRepository;
            return this;
        }

        public Builder aggregateDocRepository(AggregateDocRepository aggregateDocRepository) {
            factory.aggregateDocRepository = aggregateDocRepository;
            return this;
        }

        public BoundedContextGraphFactory build() {
            checkThatValue(factory.boundedContextDoc).notNull();
            checkThatValue(factory.relationRepository).notNull();
            checkThatValue(factory.aggregateDocRepository).notNull();
            return factory;
        }
    }

    private BoundedContextGraphFactory() {

    }

    public UndirectedGraph buildGraph() {
        addSimpleAggregates();
        return graph;
    }

    private BoundedContextDoc boundedContextDoc;

    private UndirectedGraph graph = new UndirectedGraph();

    private void addSimpleAggregates() {
        for (AggregateDoc aggregateDoc : aggregateDocRepository.findByBoundedContextKey(boundedContextDoc.getKey())) {
            addSimpleAggregate(aggregateDoc);
            addAggregateRelations(aggregateDoc);
        }
    }

    private AggregateDocRepository aggregateDocRepository;

    private void addSimpleAggregate(AggregateDoc aggregateDoc) {
        Node node = Node.box(aggregateDoc.boundedContextComponentDoc().componentDoc().name());
        node.setStyle(NodeStyle.BOLD);
        graph.getNodesAndEdges().addNode(node);
    }

    private void addAggregateRelations(AggregateDoc aggregateDoc) {
        addAggregateRelations(aggregateDoc, aggregateDoc.className());
    }

    private void addAggregateRelations(AggregateDoc aggregateDoc, String fromClassName) {
        for(Relation relation : relationRepository.findWithFromClassName(fromClassName)) {
            if(relation.toComponent().type() == ComponentType.AGGREGATE) {
                AggregateDoc otherAggregate = aggregateDocRepository.get(AggregateDocKey.ofClassName(relation.toComponent().className()));
                if(!aggregateDoc.className().equals(otherAggregate.className()) &&
                        otherAggregate.boundedContextComponentDoc().boundedContextDocKey().equals(boundedContextDoc.getKey())) {
                    UndirectedEdge edge = UndirectedEdge
                            .solidEdge(aggregateDoc.boundedContextComponentDoc().componentDoc().name(),
                                    name(relation.toComponent()));
                    graph.getNodesAndEdges().addEdge(edge);
                }
            } else {
                addAggregateRelations(aggregateDoc, relation.toComponent().className());
            }
        }
    }

    private RelationRepository relationRepository;

    private String name(Component component) {
        switch(component.type()) {
        case AGGREGATE:
            return aggregateDocRepository.get(AggregateDocKey.ofClassName(component.className())).boundedContextComponentDoc().componentDoc().name();
        default:
            throw new IllegalArgumentException("Unsupported component type " + component.type());
        }
    }
}
