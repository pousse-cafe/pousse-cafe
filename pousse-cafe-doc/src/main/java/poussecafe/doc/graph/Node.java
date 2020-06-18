package poussecafe.doc.graph;

import java.util.Objects;
import java.util.Optional;
import org.apache.commons.lang3.builder.EqualsBuilder;

import static poussecafe.util.Equality.referenceEquals;

public class Node implements Comparable<Node> {

    private String name;

    private Optional<Shape> shape = Optional.empty();

    private Optional<NodeStyle> style = Optional.empty();

    public static Node box(String name) {
        Node node = new Node(name);
        node.setShape(Optional.of(Shape.BOX));
        return node;
    }

    public static Node ellipse(String name) {
        Node node = new Node(name);
        node.setShape(Optional.of(Shape.ELLIPSE));
        return node;
    }

    public Node(String name) {
        setName(name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        Objects.requireNonNull(name);
        this.name = name;
    }

    public Optional<Shape> getShape() {
        return shape;
    }

    public void setShape(Optional<Shape> shape) {
        Objects.requireNonNull(shape);
        this.shape = shape;
    }

    public Optional<NodeStyle> getStyle() {
        return style;
    }

    public void setStyle(Optional<NodeStyle> style) {
        Objects.requireNonNull(style);
        this.style = style;
    }

    @Override
    public int compareTo(Node o) {
        return name.compareTo(o.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return referenceEquals(this, obj).orElse(other -> new EqualsBuilder()
                .append(name, other.name)
                .build());
    }
}
