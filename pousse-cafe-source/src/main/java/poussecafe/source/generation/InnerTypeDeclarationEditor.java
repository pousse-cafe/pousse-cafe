package poussecafe.source.generation;

import static java.util.Objects.requireNonNull;

public class InnerTypeDeclarationEditor extends TypeDeclarationEditor {

    public TypeDeclarationEditor parent() {
        return parent;
    }

    private TypeDeclarationEditor parent;

    public InnerTypeDeclarationEditor(NodeRewrite rewrite, TypeDeclarationEditor parent) {
        super(rewrite);

        requireNonNull(parent);
        this.parent = parent;
    }
}
