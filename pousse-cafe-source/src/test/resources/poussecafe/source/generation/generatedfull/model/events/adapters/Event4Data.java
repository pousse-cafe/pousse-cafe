package poussecafe.source.generation.generatedfull.model.events.adapters;

import java.io.Serializable;
import poussecafe.discovery.MessageImplementation;
import poussecafe.source.generation.generatedfull.model.events.Event4;

@MessageImplementation(message = Event4.class)
@SuppressWarnings("serial")
public class Event4Data implements Serializable, Event4 {
}