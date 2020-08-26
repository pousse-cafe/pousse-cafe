package poussecafe.source.generation.generatedfull.model.events.adapters;

import java.io.Serializable;
import poussecafe.discovery.MessageImplementation;
import poussecafe.source.generation.generatedfull.model.events.Event3;

@MessageImplementation(message = Event3.class)
@SuppressWarnings("serial")
public class Event3Data implements Serializable, Event3 {
}