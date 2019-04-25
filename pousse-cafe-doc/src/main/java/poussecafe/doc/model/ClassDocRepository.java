package poussecafe.doc.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.lang.model.element.TypeElement;
import poussecafe.domain.Service;
import poussecafe.exception.NotFoundException;

public class ClassDocRepository implements Service {

    public void registerTypeElements(Set<TypeElement> typeElements) {
        typeElements.forEach(this::registerClassDoc);
    }

    public void registerClassDoc(TypeElement typeElement) {
        classDocMap.put(typeElement.getQualifiedName().toString(), typeElement);
    }

    private Map<String, TypeElement> classDocMap = new HashMap<>();

    public TypeElement getClassDoc(String qualifiedName) {
        TypeElement classDoc = classDocMap.get(qualifiedName);
        if(classDoc == null) {
            throw new NotFoundException("No class with name " + qualifiedName);
        }
        return classDoc;
    }
}
