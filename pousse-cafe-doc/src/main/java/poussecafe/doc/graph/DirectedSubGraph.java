package poussecafe.doc.graph;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

public class DirectedSubGraph implements SubGraph {

    private List<DirectedSubGraph> subGraphs = new ArrayList<>();

    private DirectedGraphNodesAndEdges nodesAndEdges = new DirectedGraphNodesAndEdges();

    private String name;

    @Override
    public List<SubGraph> getSubGraphs() {
        return new ArrayList<>(subGraphs);
    }

    public void addSubGraph(DirectedSubGraph subGraph) {
        subGraphs.add(subGraph);
    }

    public DirectedGraphNodesAndEdges getNodesAndEdges() {
        return nodesAndEdges;
    }

    public void setNodesAndEdges(DirectedGraphNodesAndEdges nodesAndEdges) {
        this.nodesAndEdges = nodesAndEdges;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean hasName() {
        return !StringUtils.isEmpty(name);
    }

}