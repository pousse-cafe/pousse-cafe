package poussecafe.doc.graph;

import java.util.ArrayList;
import java.util.List;

public class DirectedGraph implements Graph {

    private List<DirectedSubGraph> subGraphs = new ArrayList<>();

    private DirectedGraphNodesAndEdges nodesAndEdges = new DirectedGraphNodesAndEdges();

    @Override
    public List<SubGraph> getSubGraphs() {
        return new ArrayList<>(subGraphs);
    }

    public void addSubGraph(DirectedSubGraph subGraph) {
        subGraphs.add(subGraph);
    }

    @Override
    public NodesAndEdges getNodesAndEdges() {
        return nodesAndEdges;
    }

    public void setNodesAndEdges(DirectedGraphNodesAndEdges nodesAndEdges) {
        this.nodesAndEdges = nodesAndEdges;
    }
}
