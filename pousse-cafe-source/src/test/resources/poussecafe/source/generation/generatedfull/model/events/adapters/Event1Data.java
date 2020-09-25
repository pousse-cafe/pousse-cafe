package poussecafe.source.generation.generatedfull.model.events.adapters;

import java.io.Serializable;
import poussecafe.discovery.MessageImplementation;
import poussecafe.source.generation.generatedfull.model.events.Event1;

@MessageImplementation(message = Event1.class)
@SuppressWarnings("serial")
public class Event1Data implements Serializable, Event1 {
}