package poussecafe.doc;

import java.util.Map;
import java.util.Optional;
import poussecafe.doc.graph.DirectedEdge;
import poussecafe.doc.graph.DirectedGraph;
import poussecafe.doc.graph.Node;
import poussecafe.doc.graph.NodeStyle;
import poussecafe.doc.model.domainprocessdoc.DomainProcessDoc;
import poussecafe.doc.model.domainprocessdoc.Step;
import poussecafe.doc.model.domainprocessdoc.ToStep;

import static poussecafe.check.Checks.checkThatValue;

public class DomainProcessGraphFactory {

    public DomainProcessGraphFactory(DomainProcessDoc domainProcessDoc) {
        checkThatValue(domainProcessDoc).notNull();
        this.domainProcessDoc = domainProcessDoc;
    }

    private DomainProcessDoc domainProcessDoc;

    public DirectedGraph buildGraph() {
        DirectedGraph graph = new DirectedGraph();
        Map<String, Step> steps = domainProcessDoc.steps();
        for(Step step : domainProcessDoc.orderedSteps()) {
            if(step.external()) {
                Node node = Node.box(step.componentDoc().name());
                node.setStyle(NodeStyle.DASHED);
                graph.getNodesAndEdges().addNode(node);
            } else {
                graph.getNodesAndEdges().addNode(Node.ellipse(step.componentDoc().name()));
            }
            for(ToStep to : step.tos()) {
                Step stepTo = steps.get(to.name());
                DirectedEdge edge;
                if(to.directly()) {
                    edge = DirectedEdge.solidEdge(step.componentDoc().name(), to.name());
                } else {
                    edge = DirectedEdge.dashedEdge(step.componentDoc().name(), to.name());
                }

                Optional<String> consumedEvent = stepTo.consumedEvent();
                if(consumedEvent.isPresent()) {
                    edge.setLabel(consumedEvent.get());
                }

                graph.getNodesAndEdges().addEdge(edge);
            }
        }
        return graph;
    }
}
