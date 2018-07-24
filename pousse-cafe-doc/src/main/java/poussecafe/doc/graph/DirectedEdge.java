package poussecafe.doc.graph;

import java.util.Optional;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class DirectedEdge implements Edge {

    private String node1;

    private String node2;

    private EdgeStyle style;

    private String label;

    public static DirectedEdge solidEdge(String entity1, String entity2) {
        DirectedEdge edge = new DirectedEdge(entity1, entity2);
        edge.setStyle(EdgeStyle.SOLID);
        return edge;
    }

    public static DirectedEdge dashedEdge(String entity1, String entity2) {
        DirectedEdge edge = new DirectedEdge(entity1, entity2);
        edge.setStyle(EdgeStyle.DOTTED);
        return edge;
    }

    public EdgeStyle getStyle() {
        return style;
    }

    public void setStyle(EdgeStyle style) {
        this.style = style;
    }

    public DirectedEdge(String node1, String node2) {
        setNode1(node1);
        setNode2(node2);
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

    public void setLabel(String label) {
        this.label = label;
    }

    public Optional<String> getLabel() {
        return Optional.ofNullable(label);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(node1)
                .append(node2)
                .append(style)
                .append(label)
                .build();
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
        DirectedEdge other = (DirectedEdge) obj;
        return new EqualsBuilder()
                .append(node1, other.node1)
                .append(node2, other.node2)
                .append(style, other.style)
                .append(label, other.label)
                .build();
    }

}
