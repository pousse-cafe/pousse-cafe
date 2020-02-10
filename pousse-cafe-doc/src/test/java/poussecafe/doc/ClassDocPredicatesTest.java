package poussecafe.doc;

import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Types;
import jdk.javadoc.doclet.DocletEnvironment;
import org.junit.Before;
import org.junit.Test;
import poussecafe.domain.DomainEvent;
import poussecafe.messaging.Message;
import poussecafe.runtime.Command;
import poussecafe.util.ReflectionUtils;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ClassDocPredicatesTest {

    @Before
    public void mockEnvironment() {
        if(docletEnvironment == null) {
            docletEnvironment = buildEnvironmentMock();
        }
        classDocPredicates = new ClassDocPredicates();
        ReflectionUtils.access(classDocPredicates).set("docletEnvironment", docletEnvironment);
    }

    private DocletEnvironment docletEnvironment;

    private DocletEnvironment buildEnvironmentMock() {
        DocletEnvironment environment = mock(DocletEnvironment.class);

        Types elements = mock(Types.class);
        when(environment.getTypeUtils()).thenReturn(elements);

        return environment;
    }

    @Test
    public void domainEventsAreDetected() {
        givenClassDoc(actualDomainEventClassDoc("MyEvent"));
        whenTestingIfDocumentsSubclassOf(Message.class);
        thenResultIs(true);
    }

    private TypeElement actualDomainEventClassDoc(String qualifiedName) {
        return new ClassDocMockBuilder(docletEnvironment)
                .qualifiedName(qualifiedName)
                .implementsInterface(domainEventDoc())
                .build();
    }

    private TypeElement domainEventDoc() {
        return new ClassDocMockBuilder(docletEnvironment)
                .qualifiedName(DomainEvent.class.getName())
                .implementsInterface(messageDoc())
                .build();
    }

    private TypeElement messageDoc() {
        return new ClassDocMockBuilder(docletEnvironment)
                .qualifiedName(Message.class.getName())
                .build();
    }

    private void givenClassDoc(TypeElement classDoc) {
        this.classDoc = classDoc;
    }

    private TypeElement classDoc;

    private void whenTestingIfDocumentsSubclassOf(Class<?> parentClassOrInterface) {
        result = classDocPredicates.documentsSubclassOf(classDoc, parentClassOrInterface);
    }

    private ClassDocPredicates classDocPredicates;

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

    private TypeElement actualCommandClassDoc(String qualifiedName) {
        return new ClassDocMockBuilder(docletEnvironment)
                .qualifiedName(qualifiedName)
                .implementsInterface(commandDoc())
                .build();
    }

    private TypeElement commandDoc() {
        return new ClassDocMockBuilder(docletEnvironment)
                .qualifiedName(Command.class.getName())
                .implementsInterface(messageDoc())
                .build();
    }
}
