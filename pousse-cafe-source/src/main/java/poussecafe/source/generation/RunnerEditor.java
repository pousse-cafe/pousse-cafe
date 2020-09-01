package poussecafe.source.generation;

import org.eclipse.jdt.core.dom.Block;
import poussecafe.listeners.UpdateOneRunner;
import poussecafe.source.analysis.Name;
import poussecafe.source.generation.tools.AstWrapper;
import poussecafe.source.generation.tools.CompilationUnitEditor;
import poussecafe.source.generation.tools.Visibility;
import poussecafe.source.model.Aggregate;
import poussecafe.source.model.MessageListener;
import poussecafe.source.model.MessageType;
import poussecafe.source.model.Model;

import static java.util.Objects.requireNonNull;

public class RunnerEditor {

    @SuppressWarnings("unchecked")
    public void edit() {
        var typeEditor = compilationUnitEditor.typeDeclaration();
        if(typeEditor.isNewType()) {
            compilationUnitEditor.setPackage(NamingConventions.runnerPackage(aggregate));

            compilationUnitEditor.addImport(UpdateOneRunner.class);
            compilationUnitEditor.addImport(messageName());

            typeEditor.modifiers().setVisibility(Visibility.PUBLIC);
            typeEditor.setName(messageListener.runnerName().orElseThrow());

            var supertype = ast.newParameterizedType(UpdateOneRunner.class);

            var messageTypeName = new Name(messageListener.consumedMessage().name());
            supertype.typeArguments().add(ast.newSimpleType(messageTypeName));

            var idName = NamingConventions.aggregateIdentifierTypeName(aggregate);
            supertype.typeArguments().add(ast.newSimpleType(idName.getIdentifier()));
            supertype.typeArguments().add(ast.newSimpleType(aggregate.simpleName()));
            typeEditor.setSuperclass(supertype);

            var idExtractorEditor = typeEditor.insertNewMethodFirst();
            idExtractorEditor.setName("aggregateId");
            idExtractorEditor.modifiers().setVisibility(Visibility.PROTECTED);
            idExtractorEditor.modifiers().markerAnnotation(Override.class);
            idExtractorEditor.setReturnType(ast.newSimpleType(idName.getIdentifier()));

            idExtractorEditor.addParameter(messageTypeName, "message");

            Block block = ast.ast().newBlock();
            block.statements().add(ast.newReturnNullStatement());
            idExtractorEditor.setBody(block);

            compilationUnitEditor.flush();
        }
    }

    private Name messageName() {
        var message = messageListener.consumedMessage();
        if(message.type() == MessageType.COMMAND) {
            return model.command(message.name()).orElseThrow().name();
        } else if(message.type() == MessageType.DOMAIN_EVENT) {
            return model.event(message.name()).orElseThrow().name();
        } else {
            throw new UnsupportedOperationException("Unsupported message type " + message.type());
        }
    }

    private Model model;

    private Aggregate aggregate;

    private MessageListener messageListener;

    public static class Builder {

        private RunnerEditor editor = new RunnerEditor();

        public RunnerEditor build() {
            requireNonNull(editor.compilationUnitEditor);
            requireNonNull(editor.model);
            requireNonNull(editor.messageListener);

            editor.ast = editor.compilationUnitEditor.ast();

            return editor;
        }

        public Builder compilationUnitEditor(CompilationUnitEditor compilationUnitEditor) {
            editor.compilationUnitEditor = compilationUnitEditor;
            return this;
        }

        public Builder model(Model model) {
            editor.model = model;
            return this;
        }

        public Builder aggregate(Aggregate aggregate) {
            editor.aggregate = aggregate;
            return this;
        }

        public Builder messageListener(MessageListener messageListener) {
            editor.messageListener = messageListener;
            return this;
        }
    }

    private RunnerEditor() {

    }

    private CompilationUnitEditor compilationUnitEditor;

    private AstWrapper ast;
}
