package poussecafe.source.generation.generatedfull.model.events.adapters;

import java.io.Serializable;
import poussecafe.discovery.MessageImplementation;
import poussecafe.source.generation.generatedfull.model.events.Event2;

@MessageImplementation(message = Event2.class)
@SuppressWarnings("serial")
public class Event2Data implements Serializable, Event2 {
}