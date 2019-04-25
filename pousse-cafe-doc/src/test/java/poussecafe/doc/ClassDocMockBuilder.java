package poussecafe.doc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import javax.lang.model.element.Element;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.NoType;
import javax.lang.model.type.TypeMirror;
import jdk.javadoc.doclet.DocletEnvironment;
import org.mockito.Mockito;

import static org.mockito.Mockito.when;

public class ClassDocMockBuilder {

    public ClassDocMockBuilder(DocletEnvironment docletEnvironment) {
        Objects.requireNonNull(docletEnvironment);
        this.docletEnvironment = docletEnvironment;
    }

    private DocletEnvironment docletEnvironment;

    public ClassDocMockBuilder qualifiedName(String qualifiedName) {
        this.qualifiedName = qualifiedName;
        return this;
    }

    private String qualifiedName;

    public ClassDocMockBuilder superclass(TypeElement superclass) {
        this.superclass = superclass;
        return this;
    }

    private TypeElement superclass;

    public ClassDocMockBuilder interfaces(Collection<TypeElement> interfaces) {
        this.interfaces.addAll(interfaces);
        return this;
    }

    private List<TypeElement> interfaces = new ArrayList<>();

    public ClassDocMockBuilder implementsInterface(TypeElement interfaceDoc) {
        interfaces.add(interfaceDoc);
        return this;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public TypeElement build() {
        Objects.requireNonNull(qualifiedName);

        TypeElement mock = Mockito.mock(TypeElement.class);

        Name qualifiedName = Mockito.mock(Name.class);
        when(qualifiedName.toString()).thenReturn(this.qualifiedName);
        when(mock.getQualifiedName()).thenReturn(qualifiedName);

        TypeMirror superclassTypeMirror = mirror(superclass);
        when(mock.getSuperclass()).thenReturn(superclassTypeMirror);

        List mockInterfaces = new ArrayList<>();
        for(TypeElement element : interfaces) {
            mockInterfaces.add(mirror(element));
        }
        when(mock.getInterfaces()).thenReturn(mockInterfaces);
        return mock;
    }

    private TypeMirror mirror(Element element) {
        if(element == null) {
            NoType noType = Mockito.mock(NoType.class);
            when(docletEnvironment.getTypeUtils().asElement(noType)).thenReturn(null);
            return noType;
        } else {
            DeclaredType typeMirror = Mockito.mock(DeclaredType.class);
            when(docletEnvironment.getTypeUtils().asElement(typeMirror)).thenReturn(element);
            return typeMirror;
        }
    }
}
