package poussecafe.adapters.messaging;

import java.io.Serializable;
import poussecafe.events.SuccessfulConsumption;
import poussecafe.messaging.MessageImplementation;
import poussecafe.property.Property;
import poussecafe.property.PropertyBuilder;

@MessageImplementation(message = SuccessfulConsumption.class)
@SuppressWarnings("serial")
public class SuccessfulConsumptionData implements Serializable, SuccessfulConsumption {

    @Override
    public Property<String> consumptionId() {
        return PropertyBuilder.simple(String.class)
                .get(() -> consumptionId)
                .set(value -> consumptionId = value)
                .build();
    }

    private String consumptionId;

    @Override
    public Property<String> listenerId() {
        return PropertyBuilder.simple(String.class)
                .get(() -> listenerId)
                .set(value -> listenerId = value)
                .build();
    }

    private String listenerId;

    @Override
    public Property<String> rawMessage() {
        return PropertyBuilder.simple(String.class)
                .get(() -> rawMessage)
                .set(value -> rawMessage = value)
                .build();
    }

    private String rawMessage;
}
