package poussecafe.doc.model;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.NoType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import jdk.javadoc.doclet.DocletEnvironment;
import poussecafe.domain.Service;

public class DocletAccess implements Service {

    public Set<TypeElement> typeElements() {
        return ElementFilter.typesIn(docletEnvironment.getIncludedElements());
    }

    private DocletEnvironment docletEnvironment;

    public List<ExecutableElement> methods(TypeElement componentClassDoc) {
        return ElementFilter.methodsIn(componentClassDoc.getEnclosedElements());
    }

    public PackageElement packageElement(TypeElement typeElement) {
        return docletEnvironment.getElementUtils().getPackageOf(typeElement);
    }

    public List<VariableElement> fields(TypeElement typeElement) {
        return ElementFilter.fieldsIn(typeElement.getEnclosedElements());
    }

    public boolean isPublic(Element element) {
        return element.getModifiers().contains(Modifier.PUBLIC);
    }

    public boolean isOverride(ExecutableElement element) {
        Element enclosingElement = element.getEnclosingElement();
        if(enclosingElement instanceof TypeElement) {
            TypeElement enclosingTypeElement = (TypeElement) enclosingElement;
            TypeMirror superclassTypeMirror = enclosingTypeElement.getSuperclass();
            if(superclassTypeMirror instanceof NoType) {
                return false;
            } else {
                TypeElement superclassTypeElement = (TypeElement) docletEnvironment.getTypeUtils().asElement(superclassTypeMirror);
                return isOverride(element, superclassTypeElement, enclosingTypeElement);
            }
        } else {
            return false;
        }
    }

    private boolean isOverride(ExecutableElement element, TypeElement superclassTypeElement, TypeElement enclosingTypeElement) {
        Optional<ExecutableElement> overridden = ElementFilter.methodsIn(superclassTypeElement.getEnclosedElements()).stream()
            .filter(method -> docletEnvironment.getElementUtils().overrides(element, method, enclosingTypeElement))
            .findFirst();
        if(!overridden.isPresent()) {
            TypeMirror supersuperclassTypeMirror = superclassTypeElement.getSuperclass();
            if(supersuperclassTypeMirror instanceof NoType) {
                return false;
            } else {
                TypeElement supersuperclassTypeElement = (TypeElement) docletEnvironment.getTypeUtils().asElement(supersuperclassTypeMirror);
                return isOverride(element, supersuperclassTypeElement, enclosingTypeElement);
            }
        } else {
            return true;
        }
    }
}
