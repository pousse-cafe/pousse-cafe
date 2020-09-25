package poussecafe.source.generation.generatedfull.commands.adapters;

import java.io.Serializable;
import poussecafe.discovery.MessageImplementation;
import poussecafe.source.generation.generatedfull.commands.Command2;

@MessageImplementation(message = Command2.class)
@SuppressWarnings("serial")
public class Command2Data implements Serializable, Command2 {
}
