package poussecafe.environment;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class MessageListenerTypeTest {

    @Test
    public void valuesHaveExpectedOrder() {
        assertTrue(MessageListenerType.REPOSITORY.ordinal() == 0);
        assertTrue(MessageListenerType.AGGREGATE.ordinal() == 1);
        assertTrue(MessageListenerType.FACTORY.ordinal() == 2);
        assertTrue(MessageListenerType.DOMAIN_PROCESS.ordinal() == 3);
        assertTrue(MessageListenerType.CUSTOM.ordinal() == 4);
    }
}
