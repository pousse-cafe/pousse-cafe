package poussecafe.doc.graph;

import java.util.ArrayList;
import java.util.List;

public class UndirectedGraph implements Graph {

    private List<UndirectedSubGraph> subGraphs = new ArrayList<>();

    private UndirectedGraphNodesAndEdges nodesAndEdges = new UndirectedGraphNodesAndEdges();

    @Override
    public List<SubGraph> getSubGraphs() {
        return new ArrayList<>(subGraphs);
    }

    public void addSubGraph(UndirectedSubGraph subGraph) {
        subGraphs.add(subGraph);
    }

    @Override
    public UndirectedGraphNodesAndEdges getNodesAndEdges() {
        return nodesAndEdges;
    }

    public void setNodesAndEdges(UndirectedGraphNodesAndEdges nodesAndEdges) {
        this.nodesAndEdges = nodesAndEdges;
    }
}
