package poussecafe.source.generation.generatedfull.model.events.adapters;

import java.io.Serializable;
import poussecafe.discovery.MessageImplementation;
import poussecafe.source.generation.generatedfull.model.events.Event5;

@MessageImplementation(message = Event5.class)
@SuppressWarnings("serial")
public class Event5Data implements Serializable, Event5 {
}