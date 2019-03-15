package poussecafe.doc.model.processstepdoc;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.FieldDoc;
import com.sun.javadoc.MethodDoc;
import com.sun.javadoc.Parameter;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import poussecafe.doc.ClassDocPredicates;
import poussecafe.domain.DomainEvent;

public class ConsumedMessageExtractor {

    public ConsumedMessageExtractor(MethodDoc methodDoc) {
        Objects.requireNonNull(methodDoc);
        this.methodDoc = methodDoc;
    }

    private MethodDoc methodDoc;

    public Optional<String> consumedDomainEvent() {
        for(Parameter parameter : methodDoc.parameters()) {
            Optional<ClassDoc> parameterClassDoc = Optional.ofNullable(parameter.type().asClassDoc());
            if(parameterClassDoc.isPresent() &&
                    ClassDocPredicates.documentsWithSuperinterface(parameterClassDoc.get(), DomainEvent.class)) {
                return Optional.of(parameterClassDoc.get().typeName());
            } else if(parameterClassDoc.isPresent()) {
                return consumedMessage(parameterClassDoc.get());
            }
        }
        return Optional.empty();
    }

    private Optional<String> consumedMessage(ClassDoc classDoc) {
        if(alreadyExplored(classDoc)) {
            return Optional.empty();
        }
        addExplored(classDoc);

        for(FieldDoc fieldDoc : classDoc.fields()) {
            Optional<ClassDoc> fieldType = Optional.ofNullable(fieldDoc.type().asClassDoc());
            if(fieldDoc.isPublic() && fieldType.isPresent() && ClassDocPredicates.documentsWithSuperinterface(fieldType.get(), DomainEvent.class)) {
                return Optional.of(fieldType.get().typeName());
            } else if(fieldType.isPresent()) {
                return consumedMessage(fieldType.get());
            }
        }
        for(MethodDoc methodDoc : classDoc.methods()) {
            Optional<ClassDoc> fieldType = Optional.ofNullable(methodDoc.returnType().asClassDoc());
            if(methodDoc.isPublic() && fieldType.isPresent() && ClassDocPredicates.documentsWithSuperinterface(fieldType.get(), DomainEvent.class)) {
                return Optional.of(fieldType.get().typeName());
            } else if(fieldType.isPresent()) {
                return consumedMessage(fieldType.get());
            }
        }
        return Optional.empty();
    }

    private boolean alreadyExplored(ClassDoc classDoc) {
        return alreadyExplored.contains(classDoc.qualifiedTypeName());
    }

    private void addExplored(ClassDoc classDoc) {
        alreadyExplored.add(classDoc.qualifiedTypeName());
    }

    private HashSet<String> alreadyExplored = new HashSet<>();
}
