package poussecafe.messaging;

import java.util.concurrent.Future;
import java.util.function.Supplier;
import org.junit.Test;
import poussecafe.configuration.TestCommand;
import poussecafe.journal.CommandWatcher;
import poussecafe.messaging.CommandHandlingResult;
import poussecafe.messaging.CommandProcessor;
import poussecafe.messaging.MessageRouter;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class CommandProcessorTest {

    private MessageRouter router;

    private CommandWatcher watcher;

    private CommandProcessor processor;

    private TestCommand command;

    private Supplier<CommandHandlingResult> supplier;

    @Test
    public void noInteractionWithWatcherIfReturnedFutureUnused() {
        givenCommandProcessor();
        givenCommandProcessed();
        whenNotUsingReturnedSupplier();
        thenNoInteractionWithWatcher();
    }

    @SuppressWarnings("unchecked")
    private void givenCommandProcessor() {
        givenCommand();

        router = mock(MessageRouter.class);
        watcher = mock(CommandWatcher.class);
        when(watcher.watchCommand(command.getId())).thenReturn(mock(Future.class));

        processor = new CommandProcessor();
        processor.setMessageRouter(router);
        processor.setCommandWatcher(watcher);
    }

    private void givenCommand() {
        command = new TestCommand();
    }

    private void givenCommandProcessed() {
        supplier = processor.processCommand(command);
    }

    private void whenNotUsingReturnedSupplier() {
        // Don't do anything
    }

    private void thenNoInteractionWithWatcher() {
        verifyZeroInteractions(watcher);
    }

    @Test
    public void requestSubmittedToWatcherIfReturnedFutureUsed() {
        givenCommandProcessor();
        givenCommandProcessed();
        whenUsingReturnedSupplier();
        thenRequestSubmittedToWatcher();
    }

    private void whenUsingReturnedSupplier() {
        try {
            supplier.get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void thenRequestSubmittedToWatcher() {
        verify(watcher).watchCommand(command.getId());
    }
}
