package poussecafe.doc.graph;

import java.util.ArrayList;
import java.util.List;

public class UndirectedGraph {

    private List<UndirectedSubGraph> subGraphs = new ArrayList<>();

    private UndirectedGraphNodesAndEdges nodesAndEdges = new UndirectedGraphNodesAndEdges();

    public List<UndirectedSubGraph> getSubGraphs() {
        return new ArrayList<>(subGraphs);
    }

    public void addSubGraph(UndirectedSubGraph subGraph) {
        this.subGraphs.add(subGraph);
    }

    public UndirectedGraphNodesAndEdges getNodesAndEdges() {
        return nodesAndEdges;
    }

    public void setNodesAndEdges(UndirectedGraphNodesAndEdges nodesAndEdges) {
        this.nodesAndEdges = nodesAndEdges;
    }
}
