package poussecafe.source.generation;

import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import poussecafe.listeners.UpdateOneRunner;
import poussecafe.source.analysis.Name;
import poussecafe.source.generation.tools.AstWrapper;
import poussecafe.source.generation.tools.ComilationUnitEditor;
import poussecafe.source.generation.tools.MethodDeclarationEditor;
import poussecafe.source.generation.tools.Visibility;
import poussecafe.source.model.Aggregate;
import poussecafe.source.model.MessageListener;
import poussecafe.source.model.MessageType;
import poussecafe.source.model.Model;

import static java.util.Objects.requireNonNull;

public class RunnerEditor {

    @SuppressWarnings("unchecked")
    public void edit() {
        compilationUnitEditor.setPackage(CodeGenerationConventions.runnerPackage(aggregate));

        compilationUnitEditor.addImportFirst(UpdateOneRunner.class);
        compilationUnitEditor.addImportFirst(messageName());

        var typeEditor = compilationUnitEditor.typeDeclaration();
        typeEditor.modifiers().setVisibility(Visibility.PUBLIC);
        typeEditor.setName(messageListener.runnerName().orElseThrow());

        var idName = AggregateCodeGenerationConventions.aggregateIdentifierTypeName(aggregate);
        Name messageTypeName = new Name(messageListener.consumedMessage().name());
        if(typeEditor.isNewType()) {
            var supertype = ast.newParameterizedType(UpdateOneRunner.class);
            supertype.typeArguments().add(ast.newSimpleType(messageTypeName));
            supertype.typeArguments().add(ast.newSimpleType(idName.getIdentifier()));
            supertype.typeArguments().add(ast.newSimpleType(new Name(aggregate.name())));
            typeEditor.setSuperclass(supertype);
        }

        var existingIdExtractorMethod = typeEditor.findMethods("aggregateId").stream()
                .filter(this::consumesEvent)
                .findFirst();
        MethodDeclarationEditor idExtractorEditor;
        if(!existingIdExtractorMethod.isPresent()) {
            idExtractorEditor = typeEditor.insertNewMethodFirst();
            idExtractorEditor.setName("aggregateId");
            idExtractorEditor.modifiers().setVisibility(Visibility.PROTECTED);
            idExtractorEditor.modifiers().markerAnnotation(Override.class);
            idExtractorEditor.setReturnType(ast.newSimpleType(idName.getIdentifier()));

            idExtractorEditor.addParameter(messageTypeName, "message");

            Block block = ast.ast().newBlock();
            block.statements().add(ast.newReturnNullStatement());
            idExtractorEditor.setBody(block);
        }

        compilationUnitEditor.flush();
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

    private boolean consumesEvent(MethodDeclaration method) {
        return method.parameters().size() == 1
                && isListenerEvent((SingleVariableDeclaration) method.parameters().get(0));
    }

    private boolean isListenerEvent(SingleVariableDeclaration parameter) {
        if(parameter.getType().isSimpleType()) {
            SimpleType simpleType = (SimpleType) parameter.getType();
            return simpleType.getName().getFullyQualifiedName().equals(messageListener.consumedMessage().name());
        } else {
            return false;
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

        public Builder compilationUnitEditor(ComilationUnitEditor compilationUnitEditor) {
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

    private ComilationUnitEditor compilationUnitEditor;

    private AstWrapper ast;
}
