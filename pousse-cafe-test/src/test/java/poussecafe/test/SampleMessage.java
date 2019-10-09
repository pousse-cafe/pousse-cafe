package poussecafe.test;

import java.io.Serializable;
import poussecafe.discovery.MessageImplementation;
import poussecafe.domain.DomainEvent;

@SuppressWarnings("serial")
@MessageImplementation(message = SampleMessage.class)
public class SampleMessage implements Serializable, DomainEvent {

}
