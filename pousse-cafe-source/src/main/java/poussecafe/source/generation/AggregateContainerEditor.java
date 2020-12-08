package poussecafe.source.generation;

import poussecafe.source.generation.tools.CompilationUnitEditor;
import poussecafe.source.generation.tools.Visibility;
import poussecafe.source.model.Aggregate;

import static java.util.Objects.requireNonNull;

public class AggregateContainerEditor {

    public void edit() {
        var typeEditor = compilationUnitEditor.typeDeclaration();
        if(typeEditor.isNewType()) {
            compilationUnitEditor.setPackage(aggregate.packageName());

            compilationUnitEditor.addImport(poussecafe.discovery.Aggregate.class.getCanonicalName());

            var modifiers = typeEditor.modifiers();
            modifiers.setVisibility(Visibility.PUBLIC);
            var aggregateContainerTypeName = NamingConventions.aggregateContainerTypeName(aggregate);
            typeEditor.setName(aggregateContainerTypeName);

            modifiers.markerAnnotation(poussecafe.discovery.Aggregate.class);

            var constructor = typeEditor.constructors(aggregateContainerTypeName.getIdentifier().toString()).get(0);
            constructor.modifiers().setVisibility(Visibility.PRIVATE);
            constructor.setEmptyBody();

            compilationUnitEditor.flush();
        }
    }

    private Aggregate aggregate;

    public static class Builder {

        private AggregateContainerEditor editor = new AggregateContainerEditor();

        public AggregateContainerEditor build() {
            requireNonNull(editor.compilationUnitEditor);
            requireNonNull(editor.aggregate);

            return editor;
        }

        public Builder compilationUnitEditor(CompilationUnitEditor compilationUnitEditor) {
            editor.compilationUnitEditor = compilationUnitEditor;
            return this;
        }

        public Builder aggregate(Aggregate aggregate) {
            editor.aggregate = aggregate;
            return this;
        }
    }

    private AggregateContainerEditor() {

    }

    private CompilationUnitEditor compilationUnitEditor;
}
