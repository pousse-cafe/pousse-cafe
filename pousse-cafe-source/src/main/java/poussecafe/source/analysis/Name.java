package poussecafe.source.analysis;

import java.util.Arrays;
import org.eclipse.jdt.core.dom.AST;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.joining;
import static poussecafe.util.Equality.referenceEquals;

public class Name {

    public Name(org.eclipse.jdt.core.dom.Name jdomName) {
        this(jdomName.getFullyQualifiedName());
    }

    private String name;

    public Name(String name) {
        requireNonNull(name);
        this.name = name;
        evaluate();
    }

    public Name(String qualifier, String identifier) {
        qualified = true;

        requireNonNull(qualifier);
        this.qualifier = qualifier;

        requireNonNull(identifier);
        this.identifier = identifier;

        name = qualifier + "." + identifier;
    }

    private void evaluate() {
        int dotIndex = name.lastIndexOf('.');
        if(dotIndex == -1) {
            qualified = false;
            qualifier = "";
            identifier = name;
        } else {
            qualified = true;
            qualifier = name.substring(0, dotIndex);
            identifier = name.substring(dotIndex + 1, name.length());
        }
    }

    private boolean qualified;

    private String qualifier;

    private String identifier;

    public boolean isQualifiedName() {
        return qualified;
    }

    @Override
    public String toString() {
        return name;
    }

    public Name getIdentifier() {
        return new Name(identifier);
    }

    public Name getQualifier() {
        return new Name(qualifier);
    }

    public boolean isSimpleName() {
        return !qualified;
    }

    public String[] segments() {
        return name.split("\\.");
    }

    public Name withoutFirstSegment() {
        var segments = segments();
        return new Name(Arrays.stream(segments, 1, segments.length).collect(joining(".")));
    }

    public Name withoutLastSegment() {
        var segments = segments();
        return new Name(Arrays.stream(segments, 0, segments.length - 1).collect(joining(".")));
    }

    public org.eclipse.jdt.core.dom.Name toJdomName(AST ast) {
        if(qualified) {
            return ast.newQualifiedName(ast.newName(qualifier), ast.newSimpleName(identifier));
        } else {
            return ast.newSimpleName(identifier);
        }
    }

    @Override
    public boolean equals(Object obj) {
        return referenceEquals(this, obj).orElse(other -> name.equals(other.name));
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
