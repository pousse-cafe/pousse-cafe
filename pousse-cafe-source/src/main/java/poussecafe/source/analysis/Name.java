package poussecafe.source.analysis;

import java.util.Arrays;
import org.eclipse.jdt.core.dom.AST;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.joining;
import static poussecafe.util.Equality.referenceEquals;

public class Name {

    public Name getIdentifier() {
        return new Name(identifier);
    }

    private String identifier;

    public String simple() {
        return identifier;
    }

    public Name getQualifier() {
        return new Name(qualifier);
    }

    private String qualifier;

    public boolean isSimpleName() {
        return !qualified;
    }

    private boolean qualified;

    public String[] segments() {
        return qualifiedName.split("\\.");
    }

    private String qualifiedName;

    public String qualified() {
        return qualifiedName;
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

    public boolean isQualifiedName() {
        return qualified;
    }

    public Name(org.eclipse.jdt.core.dom.Name jdomName) {
        this(jdomName.getFullyQualifiedName());
    }

    public Name(String name) {
        requireNonNull(name);
        this.qualifiedName = name;
        evaluate();
    }

    public Name(String qualifier, String identifier) {
        qualified = true;

        requireNonNull(qualifier);
        this.qualifier = qualifier;

        requireNonNull(identifier);
        this.identifier = identifier;

        qualifiedName = qualifier + "." + identifier;
    }

    private void evaluate() {
        int dotIndex = qualifiedName.lastIndexOf('.');
        if(dotIndex == -1) {
            qualified = false;
            qualifier = "";
            identifier = qualifiedName;
        } else {
            qualified = true;
            qualifier = qualifiedName.substring(0, dotIndex);
            identifier = qualifiedName.substring(dotIndex + 1, qualifiedName.length());
        }
    }

    @Override
    public String toString() {
        return qualified();
    }

    @Override
    public boolean equals(Object obj) {
        return referenceEquals(this, obj).orElse(other -> qualifiedName.equals(other.qualifiedName));
    }

    @Override
    public int hashCode() {
        return qualifiedName.hashCode();
    }
}
