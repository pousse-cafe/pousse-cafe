package poussecafe.runtime;

import java.lang.reflect.Method;
import org.junit.Test;
import poussecafe.environment.DeclaredMessageListenerIdBuilder;
import poussecafe.messaging.Message;
import poussecafe.testclasses.DummyProcess;
import poussecafe.testmodule.TestDomainEvent;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class MessageListenerEntryBuilderTest {

    @Test
    public void builderGeneratesExpectedString() {
        givenDeclaredMessageListener();
        whenBuildingId();
        thenBuiltIdIs(defaultId());
    }

    private void givenDeclaredMessageListener() {
        messageClass = TestDomainEvent.class;
        try {
            method = DummyProcess.class.getMethod("domainEventListenerWithDefaultId", TestDomainEvent.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        service = new DummyProcess();
    }

    private Class<? extends Message> messageClass;

    private Method method;

    private DummyProcess service;

    protected void whenBuildingId() {
        builtId = new DeclaredMessageListenerIdBuilder()
                .messageClass(messageClass)
                .method(method)
                .build();
    }

    private String builtId;

    private String defaultId() {
        return service.getClass().getCanonicalName() + "::" + method.getName() + "("
                + messageClass.getCanonicalName() + ")";
    }

    private void thenBuiltIdIs(String expectedId) {
        assertThat(builtId, equalTo(expectedId));
    }
}
