package poussecafe.source.generation.generatedfull.commands.adapters;

import java.io.Serializable;
import poussecafe.discovery.MessageImplementation;
import poussecafe.source.generation.generatedfull.commands.Command3;

@MessageImplementation(message = Command3.class)
@SuppressWarnings("serial")
public class Command3Data implements Serializable, Command3 {
}
