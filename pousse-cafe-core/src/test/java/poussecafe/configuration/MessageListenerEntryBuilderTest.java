package poussecafe.configuration;

import java.lang.reflect.Method;
import org.junit.Test;
import poussecafe.messaging.Message;
import poussecafe.messaging.Queue;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class MessageListenerEntryBuilderTest {

    private Queue source;

    private Class<? extends Message> messageClass;

    private String givenListenerId;

    private Method method;

    private DummyWorkflow service;

    private MessageListenerEntry builtEntry;

    private String customListenerId;

    @Test
    public void emptyListenerIdTriggersGeneration() {
        givenListenerWithEmptyId();
        whenBuildingEntry();
        thenListenerIdIsDefault();
    }

    private void givenListenerWithEmptyId() {
        givenMessageListener();
        givenListenerId = "";
    }

    private void givenMessageListener() {
        source = Queue.forName("source");
        messageClass = TestDomainEvent.class;
        try {
            method = DummyWorkflow.class.getMethod("domainEventListenerWithDefaultId", TestDomainEvent.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        service = new DummyWorkflow();
    }

    protected void whenBuildingEntry() {
        builtEntry = new MessageListenerEntryBuilder()
                .withSource(source)
                .withMessageClass(messageClass)
                .withListenerId(givenListenerId)
                .withMethod(method)
                .withTarget(service)
                .build();
    }

    private void thenListenerIdIsDefault() {
        assertThat(builtEntry.getListener().getListenerId(), equalTo(defaultId()));
    }

    private String defaultId() {
        return service.getClass().getCanonicalName() + "::" + method.getName() + "("
                + messageClass.getCanonicalName() + ")";
    }

    @Test
    public void nullListenerIdTriggersGeneration() {
        givenListenerWithNullId();
        whenBuildingEntry();
        thenListenerIdIsDefault();
    }

    private void givenListenerWithNullId() {
        givenMessageListener();
        givenListenerId = null;
    }

    @Test
    public void givenListenerIdOverridesGeneration() {
        givenCustomListenerId();
        whenBuildingEntry();
        thenListenerIdIsCustom();
    }

    private void givenCustomListenerId() {
        givenMessageListener();
        customListenerId = "customId";
        givenListenerId = customListenerId;
    }

    private void thenListenerIdIsCustom() {
        assertThat(builtEntry.getListener().getListenerId(), equalTo(customListenerId));
    }
}
