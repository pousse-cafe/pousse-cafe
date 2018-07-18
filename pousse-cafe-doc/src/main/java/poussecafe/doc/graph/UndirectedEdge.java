package poussecafe.doc.graph;

public class UndirectedEdge {

    private String node1;

    private String node2;

    private EdgeStyle style;

    public static UndirectedEdge solidEdge(String entity1, String entity2) {
        UndirectedEdge edge = new UndirectedEdge(entity1, entity2);
        edge.setStyle(EdgeStyle.SOLID);
        return edge;
    }

    public static UndirectedEdge dashedEdge(String entity1, String entity2) {
        UndirectedEdge edge = new UndirectedEdge(entity1, entity2);
        edge.setStyle(EdgeStyle.DOTTED);
        return edge;
    }

    public EdgeStyle getStyle() {
        return style;
    }

    public void setStyle(EdgeStyle style) {
        this.style = style;
    }

    public UndirectedEdge(String node1, String node2) {
        if (node1.compareTo(node2) < 0) {
            setNode1(node1);
            setNode2(node2);
        } else {
            setNode1(node2);
            setNode2(node1);
        }
    }

    public String getNode1() {
        return node1;
    }

    private void setNode1(String node1) {
        this.node1 = node1;
    }

    public String getNode2() {
        return node2;
    }

    private void setNode2(String node2) {
        this.node2 = node2;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((node1 == null) ? 0 : node1.hashCode());
        result = prime * result + ((node2 == null) ? 0 : node2.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        UndirectedEdge other = (UndirectedEdge) obj;
        if (node1 == null) {
            if (other.node1 != null) {
                return false;
            }
        } else if (!node1.equals(other.node1)) {
            return false;
        }
        if (node2 == null) {
            if (other.node2 != null) {
                return false;
            }
        } else if (!node2.equals(other.node2)) {
            return false;
        }
        return true;
    }

}
