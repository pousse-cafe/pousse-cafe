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
import poussecafe.doc.model.moduledoc.ModuleDoc;
import poussecafe.doc.model.relation.Component;
import poussecafe.doc.model.relation.ComponentType;
import poussecafe.doc.model.relation.Relation;
import poussecafe.doc.model.relation.RelationRepository;

public class ModuleGraphFactory {

    public static class Builder {

        private ModuleGraphFactory factory = new ModuleGraphFactory();

        public Builder moduleDoc(ModuleDoc moduleDoc) {
            factory.moduleDoc = moduleDoc;
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

        public ModuleGraphFactory build() {
            Objects.requireNonNull(factory.moduleDoc);
            Objects.requireNonNull(factory.relationRepository);
            Objects.requireNonNull(factory.aggregateDocRepository);
            return factory;
        }
    }

    private ModuleGraphFactory() {

    }

    public UndirectedGraph buildGraph() {
        addSimpleAggregates();
        return graph;
    }

    private ModuleDoc moduleDoc;

    private UndirectedGraph graph = new UndirectedGraph();

    private void addSimpleAggregates() {
        for (AggregateDoc aggregateDoc : aggregateDocRepository.findByModule(moduleDoc.attributes().identifier().value())) {
            addSimpleAggregate(aggregateDoc);
            addAggregateRelations(aggregateDoc);
        }
    }

    private AggregateDocRepository aggregateDocRepository;

    private void addSimpleAggregate(AggregateDoc aggregateDoc) {
        Node node = Node.box(aggregateDoc.attributes().moduleComponentDoc().value().componentDoc().name());
        node.setStyle(NodeStyle.BOLD);
        graph.getNodesAndEdges().addNode(node);
    }

    private void addAggregateRelations(AggregateDoc aggregateDoc) {
        addAggregateRelations(aggregateDoc, aggregateDoc.className(), new ExplorationState());
    }

    private class ExplorationState {

        Set<String> exploredComponends = new HashSet<>();
    }

    private void addAggregateRelations(AggregateDoc aggregateDoc, String fromClassName, ExplorationState explorationState) {
        explorationState.exploredComponends.add(fromClassName);
        for(Relation relation : relationRepository.findWithFromClassName(fromClassName)) {
            if(relation.toComponent().type() == ComponentType.AGGREGATE) {
                AggregateDoc otherAggregate = aggregateDocRepository.get(AggregateDocId.ofClassName(relation.toComponent().className()));
                if(!aggregateDoc.className().equals(otherAggregate.className()) &&
                        otherAggregate.attributes().moduleComponentDoc().value().moduleDocId().equals(moduleDoc.attributes().identifier().value())) {
                    UndirectedEdge edge = UndirectedEdge
                            .solidEdge(aggregateDoc.attributes().moduleComponentDoc().value().componentDoc().name(),
                                    name(relation.toComponent()));
                    graph.getNodesAndEdges().addEdge(edge);
                }
            } else if(!explorationState.exploredComponends.contains(relation.toComponent().className())) {
                addAggregateRelations(aggregateDoc, relation.toComponent().className(), explorationState);
            }
        }
    }

    private RelationRepository relationRepository;

    private String name(Component component) {
        if(component.type() == ComponentType.AGGREGATE) {
            return aggregateDocRepository.get(AggregateDocId.ofClassName(component.className())).attributes().moduleComponentDoc().value().componentDoc().name();
        } else {
            throw new IllegalArgumentException("Unsupported component type " + component.type());
        }
    }
}
