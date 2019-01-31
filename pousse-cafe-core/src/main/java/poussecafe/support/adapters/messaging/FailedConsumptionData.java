package poussecafe.support.adapters.messaging;

import java.io.Serializable;
import poussecafe.contextconfigurer.MessageImplementation;
import poussecafe.property.Property;
import poussecafe.property.PropertyBuilder;
import poussecafe.support.model.FailedConsumption;

@MessageImplementation(message = FailedConsumption.class)
@SuppressWarnings("serial")
public class FailedConsumptionData implements Serializable, FailedConsumption {

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

    @Override
    public Property<String> error() {
        return PropertyBuilder.simple(String.class)
                .get(() -> error)
                .set(value -> error = value)
                .build();
    }

    private String error;
}
