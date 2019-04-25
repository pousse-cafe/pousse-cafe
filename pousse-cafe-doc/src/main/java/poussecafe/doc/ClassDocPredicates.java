package poussecafe.doc;

import java.util.Collection;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.NoType;
import javax.lang.model.type.TypeMirror;
import jdk.javadoc.doclet.DocletEnvironment;
import poussecafe.domain.Service;

public class ClassDocPredicates implements Service {

    public boolean documentsWithSuperclass(TypeElement typeElement, Class<?> expectedSuperclass) {
        if(expectedSuperclass.isInterface()) {
            throw new IllegalArgumentException("Given expected class is an interface");
        }
        return documents(asElement(typeElement.getSuperclass()), expectedSuperclass);
    }

    private Element asElement(TypeMirror typeMirror) {
        return docletEnvironment.getTypeUtils().asElement(typeMirror);
    }

    private DocletEnvironment docletEnvironment;

    public boolean documents(TypeMirror typeElement,
            Class<?> documentedClass) {
        return documents(asElement(typeElement), documentedClass);
    }

    public boolean documents(Element element,
            Class<?> documentedClass) {
        if(element instanceof TypeElement) {
            TypeElement typeElement = (TypeElement) element;
            return typeElement.getQualifiedName().toString().equals(documentedClass.getName());
        } else {
            return false;
        }
    }

    public boolean documentsWithSuperinterface(TypeElement typeElement, Class<?> expectedInterface) {
        if(!expectedInterface.isInterface()) {
            throw new IllegalArgumentException("Given expected class is not an interface");
        }
        return documents(typeElement.getInterfaces(), expectedInterface);
    }

    public boolean documents(Collection<? extends TypeMirror> typeMirrors,
            Class<?> expectedType) {
        for(TypeMirror classDoc : typeMirrors) {
            if(documents(classDoc, expectedType)) {
                return true;
            }
        }
        return false;
    }

    public boolean documentsSubclassOf(TypeElement typeElement, Class<?> expectedSuperclass) {
        if(expectedSuperclass.isInterface()) {
            return documentsImplementationOf(typeElement, expectedSuperclass);
        } else {
            return documentsExtensionOf(typeElement, expectedSuperclass);
        }
    }

    private boolean documentsImplementationOf(TypeElement typeElement,
            Class<?> expectedSuperclass) {
        if(documentsWithSuperinterface(typeElement, expectedSuperclass)) {
            return true;
        } else {
            TypeMirror superClass = typeElement.getSuperclass();
            if(!(superClass instanceof NoType) &&
                    documentsImplementationOf(asElement(superClass), expectedSuperclass)) {
                return true;
            } else {
                for(TypeMirror superInterface : typeElement.getInterfaces()) {
                    if(documentsImplementationOf(asElement(superInterface), expectedSuperclass)) {
                        return true;
                    }
                }
                return false;
            }
        }
    }

    private boolean documentsImplementationOf(Element element,
            Class<?> expectedSuperclass) {
        if(element instanceof TypeElement) {
            return documentsImplementationOf((TypeElement) element, expectedSuperclass);
        } else {
            return false;
        }
    }

    private boolean documentsExtensionOf(TypeElement typeElement,
            Class<?> expectedSuperclass) {
        throw new UnsupportedOperationException();
    }

    public boolean isEnum(TypeElement classDoc) {
        return classDoc.getKind() == ElementKind.ENUM;
    }
}
