package poussecafe.doc.model.step;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.FieldDoc;
import com.sun.javadoc.MethodDoc;
import com.sun.javadoc.Parameter;
import java.util.HashSet;
import java.util.Optional;
import poussecafe.doc.ClassDocPredicates;
import poussecafe.domain.DomainEvent;

import static poussecafe.check.Checks.checkThatValue;

public class ConsumedDomainEventExtractor {

    public ConsumedDomainEventExtractor(MethodDoc methodDoc) {
        checkThatValue(methodDoc).notNull();
        this.methodDoc = methodDoc;
    }

    private MethodDoc methodDoc;

    public Optional<String> consumedDomainEvent() {
        for(Parameter parameter : methodDoc.parameters()) {
            Optional<ClassDoc> parameterClassDoc = Optional.ofNullable(parameter.type().asClassDoc());
            if(parameterClassDoc.isPresent() && ClassDocPredicates.documentsWithSuperclass(parameterClassDoc.get(), DomainEvent.class)) {
                return Optional.of(parameterClassDoc.get().typeName());
            } else if(parameterClassDoc.isPresent()) {
                return consumedDomainEvent(parameterClassDoc.get());
            }
        }
        return Optional.empty();
    }

    private Optional<String> consumedDomainEvent(ClassDoc classDoc) {
        if(alreadyExplored(classDoc)) {
            return Optional.empty();
        }
        addExplored(classDoc);

        for(FieldDoc fieldDoc : classDoc.fields()) {
            Optional<ClassDoc> fieldType = Optional.ofNullable(fieldDoc.type().asClassDoc());
            if(fieldDoc.isPublic() && fieldType.isPresent() && ClassDocPredicates.documentsWithSuperclass(fieldType.get(), DomainEvent.class)) {
                return Optional.of(fieldType.get().typeName());
            } else if(fieldType.isPresent()) {
                return consumedDomainEvent(fieldType.get());
            }
        }
        for(MethodDoc methodDoc : classDoc.methods()) {
            Optional<ClassDoc> fieldType = Optional.ofNullable(methodDoc.returnType().asClassDoc());
            if(methodDoc.isPublic() && fieldType.isPresent() && ClassDocPredicates.documentsWithSuperclass(fieldType.get(), DomainEvent.class)) {
                return Optional.of(fieldType.get().typeName());
            } else if(fieldType.isPresent()) {
                return consumedDomainEvent(fieldType.get());
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
