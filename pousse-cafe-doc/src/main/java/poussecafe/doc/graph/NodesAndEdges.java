package poussecafe.doc.graph;

import java.util.List;

public interface NodesAndEdges {

    void addNode(Node node);

    void addEdge(Edge solidEdge);

    List<Node> nodes();
}
