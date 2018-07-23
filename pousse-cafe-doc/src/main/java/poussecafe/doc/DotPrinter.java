package poussecafe.doc;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import poussecafe.doc.graph.Node;
import poussecafe.doc.graph.NodeStyle;
import poussecafe.doc.graph.Shape;
import poussecafe.doc.graph.UndirectedEdge;
import poussecafe.doc.graph.UndirectedGraph;
import poussecafe.doc.graph.UndirectedGraphNodesAndEdges;
import poussecafe.doc.graph.UndirectedSubGraph;

public class DotPrinter {

    private PrintStream stream;

    private int subGraphCount;

    public DotPrinter(PrintStream stream) {
        this.stream = stream;
    }

    public void print(UndirectedGraph graph) {
        printGraphHeader();
        printNodesAndEdges(graph.getNodesAndEdges());
        printSubGraphs(graph.getSubGraphs());
        printGraphFooter();
    }

    private void printSubGraphs(List<UndirectedSubGraph> subGraphs) {
        for (UndirectedSubGraph subGraph : subGraphs) {
            printSubGraph(subGraph);
        }
    }

    private void printGraphHeader() {
        stream.println("graph {");
        stream.println("splines=ortho;");
    }

    private void printGraphFooter() {
        stream.println("}");
    }

    private void printSubGraph(UndirectedSubGraph subGraph) {
        stream.println("subgraph cluster_" + (subGraphCount++) + " {");
        if (subGraph.hasName()) {
            stream.println("label=\"" + subGraph.getName() + "\";");
        }
        printNodesAndEdges(subGraph.getNodesAndEdges());
        printSubGraphs(subGraph.getSubGraphs());
        stream.println("}");
    }

    private void printNodesAndEdges(
            UndirectedGraphNodesAndEdges nodesAndEdges) {
        for (UndirectedEdge edge : nodesAndEdges.edges()) {
            printEdge(edge);
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
        if (node.getShape() == Shape.BOX) {
            attributes.add("shape=box");
        } else if (node.getShape() == Shape.ELLIPSE) {
            attributes.add("shape=ellipse");
        }
        if (node.getStyle() == NodeStyle.BOLD) {
            attributes.add("style=bold");
        }
        if (attributes.size() > 0) {
            stream.print("[");
            stream.print(StringUtils.join(attributes, ','));
            stream.print("]");
        }
        stream.println(";");
    }

    private void printEdge(UndirectedEdge edge) {
        stream.print("\"");
        stream.print(edge.getNode1());
        stream.print("\" -- ");
        stream.print("\"");
        stream.print(edge.getNode2());
        stream.print("\"");
        stream.print(" [style=" + edge.getStyle().name().toLowerCase() + "]");
        stream.println(";");
    }

}
