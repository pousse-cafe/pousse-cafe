package poussecafe.doc.model.processstepdoc;

import java.util.HashSet;
import java.util.Objects;
import javax.lang.model.element.TypeElement;

public class ConsumedMessageExtractionState {

    public ConsumedMessageExtractionState(TypeElement nextTypeElement) {
        withNextTypeElement(nextTypeElement);
    }

    public ConsumedMessageExtractionState withNextTypeElement(TypeElement nextTypeElement) {
        Objects.requireNonNull(nextTypeElement);
        this.nextTypeElement = nextTypeElement;
        return this;
    }

    private TypeElement nextTypeElement;

    public TypeElement nextTypeElement() {
        return nextTypeElement;
    }

    public boolean alreadyExplored(TypeElement classDoc) {
        return alreadyExplored.contains(classDoc.getQualifiedName().toString());
    }

    public void addExplored(TypeElement classDoc) {
        alreadyExplored.add(classDoc.getQualifiedName().toString());
    }

    private HashSet<String> alreadyExplored = new HashSet<>();
}
