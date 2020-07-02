package poussecafe.source;

import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import poussecafe.discovery.MessageListener;

import static java.util.Objects.requireNonNull;

public class MessageListenerSource {

    public String methodName() {
        return methodName;
    }

    private String methodName;

    public String messageName() {
        return messageName;
    }

    private String messageName;

    public static class Builder {

        public Builder(Imports imports) {
            requireNonNull(imports);
            this.imports = imports;
        }

        private Imports imports;

        private MessageListenerSource source = new MessageListenerSource();

        public MessageListenerSource build() {
            requireNonNull(source.methodName);
            return source;
        }

        public Builder withMethodDeclaration(MethodDeclaration declaration) {
            if(!isMessageListener(imports, declaration)) {
                throw new IllegalArgumentException("Method " + declaration.getName() + " is not a message listener");
            }
            source.methodName = declaration.getName().getIdentifier();
            SingleVariableDeclaration message = (SingleVariableDeclaration) declaration.parameters().get(0);
            SimpleType messageType = (SimpleType) message.getType();
            Name messageName = messageType.getName();
            if(messageName.isSimpleName()) {
                SimpleName simpleMessageName = (SimpleName) messageName;
                source.messageName = simpleMessageName.getIdentifier();
            } else {
                QualifiedName qualifiedMessageName = (QualifiedName) messageName;
                source.messageName = qualifiedMessageName.getName().getIdentifier();
            }
            return this;
        }
    }

    @SuppressWarnings("unchecked")
    public static boolean isMessageListener(Imports imports, MethodDeclaration declaration) {
        return declaration.modifiers().stream()
                .filter(item -> item instanceof Annotation)
                .anyMatch(item -> isMessageListenerAnnotation(imports, (Annotation) item));
    }

    public static boolean isMessageListenerAnnotation(Imports imports, Annotation annotation) {
        Name annotationTypeName = annotation.getTypeName();
        return imports.resolve(annotationTypeName).isClass(MessageListener.class);
    }
}
