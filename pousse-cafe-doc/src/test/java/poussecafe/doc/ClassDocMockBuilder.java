package poussecafe.doc;

import com.sun.javadoc.ClassDoc;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import org.mockito.Mockito;

import static org.mockito.Mockito.when;

public class ClassDocMockBuilder {

    public ClassDocMockBuilder qualifiedName(String qualifiedName) {
        this.qualifiedName = qualifiedName;
        return this;
    }

    private String qualifiedName;

    public ClassDocMockBuilder isInterface(boolean isInterface) {
        this.isInterface = isInterface;
        return this;
    }

    private boolean isInterface;

    public ClassDocMockBuilder superclass(ClassDoc superclass) {
        this.superclass = superclass;
        return this;
    }

    private ClassDoc superclass;

    public ClassDocMockBuilder interfaces(Collection<ClassDoc> interfaces) {
        this.interfaces.addAll(interfaces);
        return this;
    }

    private List<ClassDoc> interfaces = new ArrayList<>();

    public ClassDocMockBuilder implementsInterface(ClassDoc interfaceDoc) {
        interfaces.add(interfaceDoc);
        return this;
    }

    public ClassDoc build() {
        Objects.requireNonNull(qualifiedName);

        ClassDoc mock = Mockito.mock(ClassDoc.class);
        when(mock.qualifiedName()).thenReturn(qualifiedName);
        when(mock.isInterface()).thenReturn(isInterface);
        when(mock.superclass()).thenReturn(superclass);
        ClassDoc[] interfacesArray = new ClassDoc[interfaces.size()];
        when(mock.interfaces()).thenReturn(interfaces.toArray(interfacesArray));
        return mock;
    }
}
