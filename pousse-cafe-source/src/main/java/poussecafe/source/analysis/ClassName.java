package poussecafe.source.analysis;

import java.io.Serializable;
import java.util.Arrays;
import org.eclipse.jdt.core.dom.AST;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.joining;
import static poussecafe.util.Equality.referenceEquals;

@SuppressWarnings("serial")
public class ClassName implements Serializable {

    public ClassName getIdentifier() {
        return new ClassName(identifier);
    }

    private String identifier;

    public String simple() {
        return identifier;
    }

    public ClassName getQualifier() {
        return new ClassName(qualifier);
    }

    private String qualifier;

    public String qualifier() {
        return qualifier;
    }

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

    public ClassName withoutFirstSegment() {
        var segments = segments();
        return new ClassName(Arrays.stream(segments, 1, segments.length).collect(joining(".")));
    }

    public ClassName withoutLastSegment() {
        return withoutLastSegments(1);
    }

    public ClassName withoutLastSegments(int n) {
        var segments = segments();
        return new ClassName(Arrays.stream(segments, 0, segments.length - n).collect(joining(".")));
    }

    public ClassName withLastSegment(String segment) {
        return new ClassName(qualified() + "." + segment);
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

    public ClassName(org.eclipse.jdt.core.dom.Name jdomName) {
        this(jdomName.getFullyQualifiedName());
    }

    public ClassName(String name) {
        if(!Character.isLetter(name.charAt(0))) {
            throw new IllegalArgumentException("Invalid type name " + name);
        }
        requireNonNull(name);
        qualifiedName = name;
        evaluate();
    }

    public ClassName(String qualifier, String identifier) {
        if(!Character.isLetter(qualifier.charAt(0))) {
            throw new IllegalArgumentException("Invalid qualified " + qualifier);
        }
        if(!Character.isLetter(identifier.charAt(0))
                || identifier.indexOf('.') != -1) {
            throw new IllegalArgumentException("Invalid identifier " + identifier);
        }

        qualified = !qualifier.isEmpty();

        requireNonNull(qualifier);
        this.qualifier = qualifier;

        requireNonNull(identifier);
        this.identifier = identifier;

        if(qualified) {
            qualifiedName = qualifier + "." + identifier;
        } else {
            qualifiedName = identifier;
        }
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
