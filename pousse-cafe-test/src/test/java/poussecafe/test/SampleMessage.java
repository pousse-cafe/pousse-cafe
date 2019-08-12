package poussecafe.test;

import java.io.Serializable;
import poussecafe.discovery.MessageImplementation;
import poussecafe.runtime.Command;

@SuppressWarnings("serial")
@MessageImplementation(message = SampleMessage.class)
public class SampleMessage implements Serializable, Command {

}
