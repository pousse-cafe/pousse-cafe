package poussecafe.doc;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import poussecafe.doc.graph.DirectedEdge;
import poussecafe.doc.graph.DirectedGraph;
import poussecafe.doc.graph.Node;
import poussecafe.doc.graph.NodeStyle;
import poussecafe.doc.graph.NodesAndEdges;
import poussecafe.doc.model.ComponentDoc;
import poussecafe.doc.model.ModuleComponentDoc;
import poussecafe.doc.model.aggregatedoc.AggregateDoc;
import poussecafe.doc.model.aggregatedoc.AggregateDocId;
import poussecafe.doc.model.aggregatedoc.AggregateDocRepository;
import poussecafe.doc.model.moduledoc.ModuleDocId;
import poussecafe.doc.model.processstepdoc.NameRequired;
import poussecafe.doc.model.processstepdoc.ProcessStepDoc;
import poussecafe.doc.model.processstepdoc.ProcessStepDocRepository;
import poussecafe.doc.model.processstepdoc.StepMethodSignature;
import poussecafe.domain.Service;

public class AggregateEventsGraphFactory implements Service {

    public DirectedGraph buildGraph(AggregateDoc aggregate) {
        ModuleComponentDoc aggregateModuleDoc = aggregate.attributes().moduleComponentDoc().value();
        ComponentDoc aggregateDoc = aggregateModuleDoc.componentDoc();
        Logger.info("Building events graph for aggregate {}", aggregateDoc.name());
        DirectedGraph graph = new DirectedGraph();
        NodesAndEdges nodesAndEdges = graph.getNodesAndEdges();

        String aggregateName = aggregateDoc.name();
        Node aggregateNode = Node.box(aggregateName);
        aggregateNode.setStyle(Optional.of(NodeStyle.BOLD));
        nodesAndEdges.addNode(aggregateNode);

        AggregateDocId aggregateDocId = aggregate.attributes().identifier().value();
        List<ProcessStepDoc> aggregateSteps = processStepDocRepository.findByAggregateDocId(aggregateDocId);
        for(ProcessStepDoc stepDoc : aggregateSteps) {
            StepMethodSignature signature = stepDoc.attributes().stepMethodSignature().value().orElseThrow();
            Optional<String> optionalConsumedEvent = signature.consumedEventName();
            if(optionalConsumedEvent.isPresent()) {
                String consumedEvent = optionalConsumedEvent.get();

                Node eventNode = Node.ellipse(consumedEvent);
                nodesAndEdges.addNode(eventNode);
                nodesAndEdges.addEdge(DirectedEdge.solidEdge(consumedEvent, aggregateName));

                ModuleDocId moduleDocId = aggregateModuleDoc.moduleDocId();
                List<ProcessStepDoc> fromSteps = processStepDocRepository.findProducing(moduleDocId, consumedEvent);
                for(ProcessStepDoc fromStep : fromSteps) {
                    Optional<AggregateDocId> optionalAggregateDocId = fromStep.attributes().aggregate().value();
                    if(optionalAggregateDocId.isPresent()) {
                        var fromAggregate = aggregateDocRepository.get(optionalAggregateDocId.get());
                        String fromAggregateName = fromAggregate.attributes().moduleComponentDoc().value()
                                .componentDoc().name();
                        Node fromExternalNode = Node.box(fromAggregateName);
                        nodesAndEdges.addNode(fromExternalNode);
                        nodesAndEdges.addEdge(DirectedEdge.solidEdge(fromAggregateName, consumedEvent));
                    }
                }

                Set<String> fromExternals = stepDoc.attributes().fromExternals().value();
                for(String fromExternal : fromExternals) {
                    Node fromExternalNode = Node.box(fromExternal);
                    fromExternalNode.setStyle(Optional.of(NodeStyle.DASHED));
                    nodesAndEdges.addNode(fromExternalNode);
                    nodesAndEdges.addEdge(DirectedEdge.solidEdge(fromExternal, consumedEvent));
                }

                for(NameRequired producedEvent : stepDoc.attributes().producedEvents().value()) {
                    Node producedEventNode = Node.ellipse(producedEvent.name());
                    nodesAndEdges.addNode(producedEventNode);
                    if(producedEvent.required()) {
                        nodesAndEdges.addEdge(DirectedEdge.solidEdge(aggregateName, producedEvent.name()));
                    } else {
                        nodesAndEdges.addEdge(DirectedEdge.dashedEdge(aggregateName, producedEvent.name()));
                    }

                    List<ProcessStepDoc> toSteps = processStepDocRepository.findConsuming(moduleDocId, producedEvent.name());
                    for(ProcessStepDoc toStep : toSteps) {
                        Optional<AggregateDocId> optionalAggregateDocId = toStep.attributes().aggregate().value();
                        if(optionalAggregateDocId.isPresent()) {
                            var toAggregate = aggregateDocRepository.get(optionalAggregateDocId.get());
                            String toAggregateName = toAggregate.attributes().moduleComponentDoc().value()
                                    .componentDoc().name();
                            Node fromExternalNode = Node.box(toAggregateName);
                            nodesAndEdges.addNode(fromExternalNode);
                            nodesAndEdges.addEdge(DirectedEdge.solidEdge(producedEvent.name(), toAggregateName));
                        }
                    }
                }

                Set<String> toExternals = stepDoc.attributes().toExternals().value();
                for(String toExternal : toExternals) {
                    Node toExternalNode = Node.box(toExternal);
                    toExternalNode.setStyle(Optional.of(NodeStyle.DASHED));
                    nodesAndEdges.addNode(toExternalNode);
                    nodesAndEdges.addEdge(DirectedEdge.solidEdge(aggregateName, toExternal));
                }

                Map<NameRequired, List<String>> toExternalsByEvent = stepDoc.attributes().toExternalsByEvent().value();
                for(Entry<NameRequired, List<String>> toExternal : toExternalsByEvent.entrySet()) {
                    NameRequired eventName = toExternal.getKey();
                    List<String> externalNames = toExternal.getValue();

                    Node toEventNode = Node.ellipse(eventName.name());
                    nodesAndEdges.addNode(toEventNode);
                    if(eventName.required()) {
                        nodesAndEdges.addEdge(DirectedEdge.solidEdge(aggregateName, eventName.name()));
                    } else {
                        nodesAndEdges.addEdge(DirectedEdge.dashedEdge(aggregateName, eventName.name()));
                    }

                    for(String externalName : externalNames) {
                        Node toExternalNode = Node.box(externalName);
                        toExternalNode.setStyle(Optional.of(NodeStyle.DASHED));
                        nodesAndEdges.addNode(toExternalNode);
                        nodesAndEdges.addEdge(DirectedEdge.solidEdge(eventName.name(), externalName));
                    }
                }
            }
        }

        return graph;
    }

    private AggregateDocRepository aggregateDocRepository;

    private ProcessStepDocRepository processStepDocRepository;
}
