package poussecafe.configuration;

import java.lang.reflect.Method;
import org.junit.Test;
import poussecafe.consequence.Consequence;
import poussecafe.consequence.Source;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class ConsequenceListenerEntryBuilderTest {

    private Source source;

    private Class<? extends Consequence> consequenceClass;

    private String givenListenerId;

    private Method method;

    private DummyWorkflow service;

    private ConsequenceListenerEntry builtEntry;

    private String customListenerId;

    @Test
    public void emptyListenerIdTriggersGeneration() {
        givenListenerWithEmptyId();
        whenBuildingEntry();
        thenListenerIdIsDefault();
    }

    private void givenListenerWithEmptyId() {
        givenConsequenceListener();
        givenListenerId = "";
    }

    private void givenConsequenceListener() {
        source = Source.forName("source");
        consequenceClass = TestDomainEvent.class;
        try {
            method = DummyWorkflow.class.getMethod("domainEventListenerWithDefaultId", TestDomainEvent.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        service = new DummyWorkflow();
    }

    protected void whenBuildingEntry() {
        builtEntry = new ConsequenceListenerEntryBuilder()
                .withSource(source)
                .withConsequenceClass(consequenceClass)
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
                + consequenceClass.getCanonicalName() + ")";
    }

    @Test
    public void nullListenerIdTriggersGeneration() {
        givenListenerWithNullId();
        whenBuildingEntry();
        thenListenerIdIsDefault();
    }

    private void givenListenerWithNullId() {
        givenConsequenceListener();
        givenListenerId = null;
    }

    @Test
    public void givenListenerIdOverridesGeneration() {
        givenCustomListenerId();
        whenBuildingEntry();
        thenListenerIdIsCustom();
    }

    private void givenCustomListenerId() {
        givenConsequenceListener();
        customListenerId = "customId";
        givenListenerId = customListenerId;
    }

    private void thenListenerIdIsCustom() {
        assertThat(builtEntry.getListener().getListenerId(), equalTo(customListenerId));
    }
}
