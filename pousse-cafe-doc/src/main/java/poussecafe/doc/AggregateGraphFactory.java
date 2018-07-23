package poussecafe.doc;

import poussecafe.doc.graph.Node;
import poussecafe.doc.graph.NodeStyle;
import poussecafe.doc.graph.UndirectedEdge;
import poussecafe.doc.graph.UndirectedGraph;
import poussecafe.doc.model.aggregatedoc.AggregateDoc;
import poussecafe.doc.model.aggregatedoc.AggregateDocKey;
import poussecafe.doc.model.aggregatedoc.AggregateDocRepository;
import poussecafe.doc.model.entitydoc.EntityDocKey;
import poussecafe.doc.model.entitydoc.EntityDocRepository;
import poussecafe.doc.model.relation.Component;
import poussecafe.doc.model.relation.ComponentType;
import poussecafe.doc.model.relation.Relation;
import poussecafe.doc.model.relation.RelationRepository;
import poussecafe.doc.model.vodoc.ValueObjectDoc;
import poussecafe.doc.model.vodoc.ValueObjectDocKey;
import poussecafe.doc.model.vodoc.ValueObjectDocRepository;

import static poussecafe.check.Checks.checkThatValue;

public class AggregateGraphFactory {

    public static class Builder {

        private AggregateGraphFactory factory = new AggregateGraphFactory();

        public Builder aggregateDoc(AggregateDoc aggregateDoc) {
            factory.aggregateDoc = aggregateDoc;
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

        public Builder entityDocRepository(EntityDocRepository entityDocRepository) {
            factory.entityDocRepository = entityDocRepository;
            return this;
        }

        public Builder valueObjectDocRepository(ValueObjectDocRepository valueObjectDocRepository) {
            factory.valueObjectDocRepository = valueObjectDocRepository;
            return this;
        }

        public AggregateGraphFactory build() {
            checkThatValue(factory.aggregateDoc).notNull();
            checkThatValue(factory.relationRepository).notNull();
            checkThatValue(factory.aggregateDocRepository).notNull();
            checkThatValue(factory.entityDocRepository).notNull();
            checkThatValue(factory.valueObjectDocRepository).notNull();
            return factory;
        }
    }

    private AggregateGraphFactory() {

    }

    private AggregateDoc aggregateDoc;

    private UndirectedGraph graph = new UndirectedGraph();

    public UndirectedGraph buildGraph() {
        addAggregate();
        addAllRelations(aggregateDoc.className());
        return graph;
    }

    private void addAggregate() {
        Node node = Node.box(aggregateDoc.boundedContextComponentDoc().componentDoc().name());
        node.setStyle(NodeStyle.BOLD);
        graph.getNodesAndEdges().addNode(node);
    }

    private void addAllRelations(String fromClassName) {
        for(Relation relation : relationRepository.findWithFromClassName(fromClassName)) {
            Component toComponent = relation.toComponent();
            if(toComponent.type() != ComponentType.AGGREGATE) {
                addNonAggregateRelation(relation);
                addAllRelations(relation.toComponent().className());
            } else {
                addAggregateRelation(toComponent);
            }
        }
    }

    private void addAggregateRelation(Component toComponent) {
        AggregateDoc aggregateDoc = aggregateDocRepository.get(AggregateDocKey.ofClassName(toComponent.className()));
        ValueObjectDoc keyDoc = valueObjectDocRepository.get(ValueObjectDocKey.ofClassName(aggregateDoc.keyClassName()));
        String fromName = keyDoc.boundedContextComponentDoc().componentDoc().name();
        String toName = aggregateDoc.boundedContextComponentDoc().componentDoc().name();

        if(tryAddRelationNode(toComponent, toName)) {
            UndirectedEdge edge = UndirectedEdge.dashedEdge(fromName, toName);
            graph.getNodesAndEdges().addEdge(edge);
        }
    }

    private RelationRepository relationRepository;

    private void addNonAggregateRelation(Relation relation) {
        String fromName = name(relation.fromComponent());
        String toName = name(relation.toComponent());
        if(tryAddRelationNode(relation.fromComponent(), fromName) || tryAddRelationNode(relation.toComponent(), toName)) {
            UndirectedEdge edge = UndirectedEdge.solidEdge(fromName, toName);
            graph.getNodesAndEdges().addEdge(edge);
        }
    }

    private String name(Component component) {
        switch(component.type()) {
        case AGGREGATE:
            return aggregateDocRepository.get(AggregateDocKey.ofClassName(component.className())).boundedContextComponentDoc().componentDoc().name();
        case ENTITY:
            return entityDocRepository.get(EntityDocKey.ofClassName(component.className())).boundedContextComponentDoc().componentDoc().name();
        case VALUE_OBJECT:
            return valueObjectDocRepository.get(ValueObjectDocKey.ofClassName(component.className())).boundedContextComponentDoc().componentDoc().name();
        default:
            throw new IllegalArgumentException("Unsupported component type " + component.type());
        }
    }

    private AggregateDocRepository aggregateDocRepository;

    private EntityDocRepository entityDocRepository;

    private ValueObjectDocRepository valueObjectDocRepository;

    private boolean tryAddRelationNode(Component component,
            String componentName) {
        if(graph.getNodesAndEdges().getNode(componentName) != null) {
            return false;
        } else {
            Node node = node(component, componentName);
            if(node != null) {
                graph.getNodesAndEdges().addNode(node);
                return true;
            } else {
                return false;
            }
        }
    }

    private Node node(Component component,
            String name) {
        if(component.type() == ComponentType.ENTITY) {
            return Node.box(name);
        } else if(component.type() == ComponentType.VALUE_OBJECT) {
            return Node.ellipse(name);
        } else if(component.type() == ComponentType.AGGREGATE) {
            Node node = Node.box(name);
            node.setStyle(NodeStyle.BOLD);
            return node;
        } else {
            throw new IllegalArgumentException("Unsupported component type " + component.type());
        }
    }
}
