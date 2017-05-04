package poussecafe.messaging;

import java.util.concurrent.TimeUnit;
import poussecafe.exception.PousseCafeException;
import poussecafe.journal.CommandWatcher;

import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;

public class CommandProcessor {

    private MessageRouter router;

    private CommandWatcher watcher;

    public BlockingSupplier<CommandHandlingResult> processCommand(Command command) {
        router.routeMessage(command);
        return new BlockingSupplier<>(timeOut -> {
            try {
                return watcher.watchCommand(command.getId()).get(timeOut.toMillis(), TimeUnit.MILLISECONDS);
            } catch (Exception e) {
                throw new PousseCafeException("Unable to get handling result", e);
            }
        });
    }

    public void setMessageRouter(MessageRouter router) {
        checkThat(value(router).notNull().because("Message router cannot be null"));
        this.router = router;
    }

    public void setCommandWatcher(CommandWatcher poller) {
        checkThat(value(poller).notNull().because("Command watcher cannot be null"));
        watcher = poller;
    }
}
