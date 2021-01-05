package poussecafe.source.validation.message;

import poussecafe.discovery.MessageImplementation;
import poussecafe.runtime.Command;

@MessageImplementation(message = AutoImplementedCommand.class)
public class AutoImplementedCommand implements Command {

}
