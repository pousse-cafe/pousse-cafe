package poussecafe.doc.model.processstepdoc;

import java.util.Optional;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import jdk.javadoc.doclet.DocletEnvironment;
import poussecafe.doc.ClassDocPredicates;
import poussecafe.doc.model.DocletAccess;
import poussecafe.domain.Service;
import poussecafe.messaging.Message;

public class ConsumedMessageExtractor implements Service {

    public Optional<String> consumedMessage(ExecutableElement methodDoc) {
        for(VariableElement parameter : methodDoc.getParameters()) {
            Element parameterElement = docletEnvironment.getTypeUtils().asElement(parameter.asType());
            if(!(parameterElement instanceof TypeElement)) {
                return Optional.empty();
            } else {
                TypeElement parameterTypeElement = (TypeElement) parameterElement;
                if(classDocPredicates.documentsSubclassOf(parameterTypeElement, Message.class)) {
                    return Optional.of(parameterTypeElement.getSimpleName().toString());
                } else {
                    return consumedMessage(new ConsumedMessageExtractionState(parameterTypeElement));
                }
            }
        }
        return Optional.empty();
    }

    private DocletEnvironment docletEnvironment;

    private ClassDocPredicates classDocPredicates;

    private Optional<String> consumedMessage(ConsumedMessageExtractionState state) {
        TypeElement nextTypeElement = state.nextTypeElement();
        if(state.alreadyExplored(nextTypeElement)) {
            return Optional.empty();
        }
        state.addExplored(nextTypeElement);

        for(VariableElement fieldDoc : docletAccess.fields(nextTypeElement)) {
            Element fieldElement = docletEnvironment.getTypeUtils().asElement(fieldDoc.asType());
            if(fieldElement instanceof TypeElement) {
                return consumedMessageOfElement(state, fieldElement);
            }
        }
        for(ExecutableElement methodDoc : docletAccess.methods(nextTypeElement)) {
            Element fieldElement = docletEnvironment.getTypeUtils().asElement(methodDoc.getReturnType());
            if(fieldElement instanceof TypeElement) {
                return consumedMessageOfElement(state, fieldElement);
            }
        }
        return Optional.empty();
    }

    private Optional<String> consumedMessageOfElement(ConsumedMessageExtractionState state,
            Element fieldElement) {
        TypeElement fieldTypeElement = (TypeElement) fieldElement;
        if(docletAccess.isPublic(fieldTypeElement) && classDocPredicates.documentsSubclassOf(fieldTypeElement, Message.class)) {
            return Optional.of(fieldTypeElement.getSimpleName().toString());
        } else {
            return consumedMessage(state.withNextTypeElement(fieldTypeElement));
        }
    }

    private DocletAccess docletAccess;
}
