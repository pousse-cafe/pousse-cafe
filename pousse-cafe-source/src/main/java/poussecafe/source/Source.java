package poussecafe.source;

import java.io.Serializable;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

import static java.util.Objects.requireNonNull;
import static poussecafe.util.Equality.referenceEquals;

@SuppressWarnings("serial")
public abstract class Source implements Serializable {

    public String id() {
        return id;
    }

    private String id;

    public CompilationUnit compilationUnit() {
        if(compilationUnit == null) {
            ASTParser parser = ASTParser.newParser(AST.JLS14);
            configure(parser);
            compilationUnit = (CompilationUnit) parser.createAST(null);
        }
        return compilationUnit;
    }

    private transient CompilationUnit compilationUnit;

    protected abstract void configure(ASTParser parser);

    public void connect(Object project) {
        // This is an optional operation
    }

    protected Source(String id) {
        requireNonNull(id);
        this.id = id;
    }

    protected Source() {

    }

    @Override
    public String toString() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        return referenceEquals(this, obj).orElse(other -> new EqualsBuilder()
                .append(id, other.id)
                .build());
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(id)
                .build();
    }
}
