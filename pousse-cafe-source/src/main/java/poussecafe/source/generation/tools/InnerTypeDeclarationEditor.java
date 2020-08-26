package poussecafe.source.generation.tools;

import static java.util.Objects.requireNonNull;

public class InnerTypeDeclarationEditor extends TypeDeclarationEditor {

    public TypeDeclarationEditor parent() {
        return parent;
    }

    private TypeDeclarationEditor parent;

    public InnerTypeDeclarationEditor(NodeRewrite rewrite, TypeDeclarationEditor parent, boolean isNew) {
        super(rewrite, isNew);

        requireNonNull(parent);
        this.parent = parent;
    }
}
