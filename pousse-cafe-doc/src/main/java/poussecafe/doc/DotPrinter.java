package poussecafe.doc;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import poussecafe.doc.graph.DirectedEdge;
import poussecafe.doc.graph.DirectedGraphNodesAndEdges;
import poussecafe.doc.graph.DirectedSubGraph;
import poussecafe.doc.graph.Graph;
import poussecafe.doc.graph.Node;
import poussecafe.doc.graph.NodesAndEdges;
import poussecafe.doc.graph.SubGraph;
import poussecafe.doc.graph.UndirectedEdge;
import poussecafe.doc.graph.UndirectedGraph;
import poussecafe.doc.graph.UndirectedGraphNodesAndEdges;
import poussecafe.doc.graph.UndirectedSubGraph;

import static java.util.stream.Collectors.joining;

public class DotPrinter {

    private PrintStream stream;

    private int subGraphCount;

    public DotPrinter(PrintStream stream) {
        this.stream = stream;
    }

    public void print(Graph graph) {
        printGraphHeader(graph);
        printNodesAndEdges(graph.getNodesAndEdges());
        printSubGraphs(graph.getSubGraphs());
        printGraphFooter();
    }

    private void printGraphHeader(Graph graph) {
        if(graph instanceof UndirectedGraph) {
            printUndirectedGraphHeader();
        } else {
            printDirectedGraphHeader();
        }
    }

    private void printNodesAndEdges(NodesAndEdges nodesAndEdges) {
        if(nodesAndEdges instanceof UndirectedGraphNodesAndEdges) {
            printUndirectedNodesAndEdges((UndirectedGraphNodesAndEdges) nodesAndEdges);
        } else {
            printDirectedNodesAndEdges((DirectedGraphNodesAndEdges) nodesAndEdges);
        }
    }

    private void printSubGraphs(List<SubGraph> subGraphs) {
        for (SubGraph subGraph : subGraphs) {
            printSubGraph(subGraph);
        }
    }

    private void printSubGraph(SubGraph subGraph) {
        if(subGraph instanceof UndirectedSubGraph) {
            printUndirectedSubGraph((UndirectedSubGraph) subGraph);
        } else {
            printDirectedSubGraph((DirectedSubGraph) subGraph);
        }
    }

    private void printUndirectedGraphHeader() {
        stream.println("graph {");
        stream.println("splines=spline;");
        stream.println("overlap=false;");
    }

    private void printGraphFooter() {
        stream.println("}");
    }

    private void printUndirectedSubGraph(UndirectedSubGraph subGraph) {
        stream.println("subgraph cluster_" + (subGraphCount++) + " {");
        if (subGraph.hasName()) {
            stream.println("label=\"" + subGraph.getName() + "\";");
        }
        printUndirectedNodesAndEdges(subGraph.getNodesAndEdges());
        printSubGraphs(subGraph.getSubGraphs());
        stream.println("}");
    }

    private void printUndirectedNodesAndEdges(
            UndirectedGraphNodesAndEdges nodesAndEdges) {
        for (UndirectedEdge edge : nodesAndEdges.edges()) {
            printUndirectedEdge(edge);
        }
        for (Node node : nodesAndEdges.nodes()) {
            printNode(node);
        }
    }

    private void printNode(Node node) {
        stream.print("\"");
        stream.print(node.getName());
        stream.print("\"");

        List<String> attributes = new ArrayList<>();
        if(node.getShape().isPresent()) {
            attributes.add("shape=" + node.getShape().orElseThrow().name().toLowerCase());
        }

        if (node.getStyle().isPresent()) {
            attributes.add("style=" + node.getStyle().orElseThrow().name().toLowerCase());
        }

        if (!attributes.isEmpty()) {
            stream.print("[");
            stream.print(StringUtils.join(attributes, ','));
            stream.print("]");
        }
        stream.println(";");
    }

    private void printUndirectedEdge(UndirectedEdge edge) {
        stream.print("\"");
        stream.print(edge.getNode1());
        stream.print("\" -- ");
        stream.print("\"");
        stream.print(edge.getNode2());
        stream.print("\"");
        stream.print(" [style=" + edge.getStyle().name().toLowerCase() + "]");
        stream.println(";");
    }

    private void printDirectedGraphHeader() {
        stream.println("digraph {");
        stream.println("splines=spline;");
    }

    private void printDirectedSubGraph(DirectedSubGraph subGraph) {
        stream.println("subgraph cluster_" + (subGraphCount++) + " {");
        if (subGraph.hasName()) {
            stream.println("label=\"" + subGraph.getName() + "\";");
        }
        printDirectedNodesAndEdges(subGraph.getNodesAndEdges());
        printSubGraphs(subGraph.getSubGraphs());
        stream.println("}");
    }

    private void printDirectedNodesAndEdges(
            DirectedGraphNodesAndEdges nodesAndEdges) {
        for (DirectedEdge edge : nodesAndEdges.edges()) {
            printDirectedEdge(edge);
        }
        for (Node node : nodesAndEdges.nodes()) {
            printNode(node);
        }
    }

    private void printDirectedEdge(DirectedEdge edge) {
        stream.print("\"");
        stream.print(edge.getNode1());
        stream.print("\" -> ");
        stream.print("\"");
        stream.print(edge.getNode2());
        stream.print("\"");

        Map<String, String> attributes = new HashMap<>();
        attributes.put("style", edge.getStyle().name().toLowerCase());
        if(edge.getLabel().isPresent()) {
            attributes.put("label", quote(edge.getLabel().get()));
        }
        stream.print(" [" + nodeOrEdgeAttributes(attributes) + "]");
        stream.println(";");
    }

    private String quote(String string) {
        return "\"" + string + "\"";
    }

    private String nodeOrEdgeAttributes(Map<String, String> attributes) {
        return attributes.entrySet().stream()
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .collect(joining(","));
    }
}
