package poussecafe.doc.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class UndirectedGraphNodesAndEdges {

    private Map<String, Node> nodes = new HashMap<>();

    private Set<UndirectedEdge> edges = new HashSet<>();

    public void addEdge(UndirectedEdge edge) {
        edges.add(edge);
    }

    public List<UndirectedEdge> edges() {
        return new ArrayList<>(edges);
    }

    public List<Node> nodes() {
        return new ArrayList<>(nodes.values());
    }

    public void addNode(Node node) {
        nodes.put(node.getName(), node);
    }

    public Node getNode(String name) {
        return nodes.get(name);
    }
}
