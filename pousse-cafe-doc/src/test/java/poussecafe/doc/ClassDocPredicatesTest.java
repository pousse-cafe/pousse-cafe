package poussecafe.doc;

import com.sun.javadoc.ClassDoc;
import org.junit.Test;
import poussecafe.domain.DomainEvent;
import poussecafe.messaging.Message;
import poussecafe.runtime.Command;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ClassDocPredicatesTest {

    @Test
    public void domainEventsAreDetected() {
        givenClassDoc(actualDomainEventClassDoc("MyEvent"));
        whenTestingIfDocumentsSubclassOf(Message.class);
        thenResultIs(true);
    }

    private ClassDoc actualDomainEventClassDoc(String qualifiedName) {
        return new ClassDocMockBuilder()
                .qualifiedName(qualifiedName)
                .isInterface(true)
                .implementsInterface(domainEventDoc())
                .build();
    }

    private ClassDoc domainEventDoc() {
        return new ClassDocMockBuilder()
                .qualifiedName(DomainEvent.class.getName())
                .isInterface(true)
                .implementsInterface(messageDoc())
                .build();
    }

    private ClassDoc messageDoc() {
        return new ClassDocMockBuilder()
                .qualifiedName(Message.class.getName())
                .isInterface(true)
                .build();
    }

    private void givenClassDoc(ClassDoc classDoc) {
        this.classDoc = classDoc;
    }

    private ClassDoc classDoc;

    private void whenTestingIfDocumentsSubclassOf(Class<?> parentClassOrInterface) {
        result = ClassDocPredicates.documentsSubclassOf(classDoc, parentClassOrInterface);
    }

    private boolean result;

    private void thenResultIs(boolean expected) {
        assertThat(result, is(expected));
    }

    @Test
    public void commandsAreDetected() {
        givenClassDoc(actualCommandClassDoc("MyCommand"));
        whenTestingIfDocumentsSubclassOf(Message.class);
        thenResultIs(true);
    }

    private ClassDoc actualCommandClassDoc(String qualifiedName) {
        return new ClassDocMockBuilder()
                .qualifiedName(qualifiedName)
                .isInterface(true)
                .implementsInterface(commandDoc())
                .build();
    }

    private ClassDoc commandDoc() {
        return new ClassDocMockBuilder()
                .qualifiedName(Command.class.getName())
                .isInterface(true)
                .implementsInterface(messageDoc())
                .build();
    }
}
