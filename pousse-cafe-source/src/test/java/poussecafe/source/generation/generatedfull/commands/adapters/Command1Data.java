package poussecafe.source.generation.generatedfull.commands.adapters;

import java.io.Serializable;
import poussecafe.discovery.MessageImplementation;
import poussecafe.source.generation.generatedfull.commands.Command1;

@MessageImplementation(message = Command1.class)
@SuppressWarnings("serial")
public class Command1Data implements Serializable, Command1 {
}
