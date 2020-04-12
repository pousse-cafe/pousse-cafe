package poussecafe.processing;

import java.util.Optional;
import org.junit.Test;
import poussecafe.testmodule.SimpleAggregate;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SuppressWarnings("rawtypes")
public class DefaultProcessingThreadSelectorTest {

    @Test
    public void selectGroupWithAggregateSetsAssignment() {
        givenMessageListenersGroupWithAggregate(Optional.of(SimpleAggregate.class));
        whenSelecting();
        thenAssignmentQueuedGroupsIs(SimpleAggregate.class, Optional.of(1));
        thenLoadIs(selectedThread, 3);
    }

    private void givenMessageListenersGroupWithAggregate(Optional<Class> aggregateRootClass) {
        messageListenersGroup = mock(MessageListenersGroup.class);
        when(messageListenersGroup.aggregateRootClass()).thenReturn(aggregateRootClass);
        when(messageListenersGroup.listeners()).thenReturn(asList(null, null, null));
    }

    private MessageListenersGroup messageListenersGroup;

    private void whenSelecting() {
        selector.selectFor(messageListenersGroup);
    }

    private DefaultProcessingThreadSelector selector = new DefaultProcessingThreadSelector(8);

    private int selectedThread;

    private void thenAssignmentQueuedGroupsIs(Class aggregateRootClass, Optional<Integer> expected) {
        assertThat(selector.queuedGroupsFor(aggregateRootClass), is(expected));
    }

    private void thenLoadIs(int threadId, int expected) {
        assertThat(selector.loadOf(threadId), is(expected));
    }

    @Test
    public void selectThenUnselectGroupWithAggregateClearsAssignment() {
        givenSelectedThread();
        whenUnselecting();
        thenAssignmentQueuedGroupsIs(SimpleAggregate.class, Optional.empty());
        thenLoadIs(selectedThread, 0);
    }

    private void givenSelectedThread() {
        givenMessageListenersGroupWithAggregate(Optional.of(SimpleAggregate.class));
        whenSelecting();
    }

    private void whenUnselecting() {
        selector.unselect(selectedThread, messageListenersGroup);
    }
}
