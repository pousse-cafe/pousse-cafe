package poussecafe.doc;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import poussecafe.doc.graph.Node;
import poussecafe.doc.graph.NodeStyle;
import poussecafe.doc.graph.UndirectedEdge;
import poussecafe.doc.graph.UndirectedGraph;
import poussecafe.doc.model.aggregatedoc.AggregateDoc;
import poussecafe.doc.model.aggregatedoc.AggregateDocId;
import poussecafe.doc.model.aggregatedoc.AggregateDocRepository;
import poussecafe.doc.model.entitydoc.EntityDocId;
import poussecafe.doc.model.entitydoc.EntityDocRepository;
import poussecafe.doc.model.relation.Component;
import poussecafe.doc.model.relation.ComponentType;
import poussecafe.doc.model.relation.Relation;
import poussecafe.doc.model.relation.RelationRepository;
import poussecafe.doc.model.vodoc.ValueObjectDocId;
import poussecafe.doc.model.vodoc.ValueObjectDocRepository;
import poussecafe.exception.NotFoundException;

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
            Objects.requireNonNull(factory.aggregateDoc);
            Objects.requireNonNull(factory.relationRepository);
            Objects.requireNonNull(factory.aggregateDocRepository);
            Objects.requireNonNull(factory.entityDocRepository);
            Objects.requireNonNull(factory.valueObjectDocRepository);
            return factory;
        }
    }

    private AggregateGraphFactory() {

    }

    private AggregateDoc aggregateDoc;

    private UndirectedGraph graph = new UndirectedGraph();

    private Set<String> exploredPaths = new HashSet<>();

    public UndirectedGraph buildGraph() {
        String aggregateNodeName = addAggregate();
        AggregateGraphPath path = new AggregateGraphPath().with(aggregateNodeName);
        addAllRelations(path, aggregateDoc.className());
        return graph;
    }

    private String addAggregate() {
        Logger.debug("Aggregate " + aggregateDoc.className());
        String nodeName = aggregateDoc.attributes().boundedContextComponentDoc().value().componentDoc().name();
        Node node = Node.box(nodeName);
        node.setStyle(NodeStyle.BOLD);
        graph.getNodesAndEdges().addNode(node);
        return nodeName;
    }

    private void addAllRelations(AggregateGraphPath path, String fromClassName) {
        for(Relation relation : relationRepository.findWithFromClassName(fromClassName)) {
            Component toComponent = relation.toComponent();

            Logger.debug("Relation " + fromClassName + " -> " + toComponent.className());
            if(toComponent.type() != ComponentType.AGGREGATE) {
                String newNodeName = name(toComponent);

                String formattedNewPath = path.formatNamesWith(newNodeName);
                if(!exploredPaths.contains(formattedNewPath)) {
                    Logger.debug("New path: " + formattedNewPath);
                    exploredPaths.add(formattedNewPath);

                    AggregateGraphPath newPath = path.with(newNodeName);
                    addNonAggregateRelation(path, toComponent, newNodeName);
                    addAllRelations(newPath, relation.toComponent().className());
                } else {
                    Logger.debug("Ignored known path: " + formattedNewPath);
                }
            } else {
                Logger.debug("New path to aggregate");
                addAggregateRelation(path, toComponent);
            }
        }
    }

    private void addAggregateRelation(AggregateGraphPath path, Component toComponent) {
        if(toComponent.className().equals(aggregateDoc.className())) {
            return;
        }

        AggregateDoc toAggregateDoc = aggregateDocRepository.get(AggregateDocId.ofClassName(toComponent.className()));
        if(!toAggregateDoc.attributes().boundedContextComponentDoc().value().boundedContextDocId().equals(aggregateDoc.attributes().boundedContextComponentDoc().value().boundedContextDocId())) {
            return;
        }

        String toName = name(toComponent);
        addNode(toComponent, toName);
        UndirectedEdge edge = UndirectedEdge.dashedEdge(path.lastName(), toName);
        graph.getNodesAndEdges().addEdge(edge);
    }

    private RelationRepository relationRepository;

    private String addNonAggregateRelation(AggregateGraphPath path, Component toComponent, String toName) {
        addNode(toComponent, toName);
        UndirectedEdge edge = UndirectedEdge.solidEdge(path.lastName(), toName);
        graph.getNodesAndEdges().addEdge(edge);
        return toName;
    }

    private String name(Component component) {
        try {
            switch(component.type()) {
            case AGGREGATE:
                return aggregateDocRepository.get(AggregateDocId.ofClassName(component.className())).attributes().boundedContextComponentDoc().value().componentDoc().name();
            case ENTITY:
                return entityDocRepository.get(EntityDocId.ofClassName(component.className())).attributes().boundedContextComponentDoc().value().componentDoc().name();
            case VALUE_OBJECT:
                    return valueObjectDocRepository.get(ValueObjectDocId.ofClassName(component.className())).attributes().boundedContextComponentDoc().value().componentDoc().name();
            default:
                throw new IllegalArgumentException("Unsupported component type " + component.type());
            }
        } catch (NotFoundException e) {
            return component.className();
        }
    }

    private AggregateDocRepository aggregateDocRepository;

    private EntityDocRepository entityDocRepository;

    private ValueObjectDocRepository valueObjectDocRepository;

    private void addNode(Component component, String candidateName) {
        if(graph.getNodesAndEdges().getNode(candidateName) == null) {
            Node node = node(component, candidateName);
            graph.getNodesAndEdges().addNode(node);
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
