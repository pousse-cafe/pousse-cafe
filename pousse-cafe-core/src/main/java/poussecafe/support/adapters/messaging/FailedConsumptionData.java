package poussecafe.support.adapters.messaging;

import java.io.Serializable;
import poussecafe.attribute.Attribute;
import poussecafe.attribute.AttributeBuilder;
import poussecafe.discovery.MessageImplementation;
import poussecafe.support.model.FailedConsumption;

@MessageImplementation(message = FailedConsumption.class)
@SuppressWarnings("serial")
public class FailedConsumptionData implements Serializable, FailedConsumption {

    @Override
    public Attribute<String> consumptionId() {
        return AttributeBuilder.single(String.class)
                .read(() -> consumptionId)
                .write(value -> consumptionId = value)
                .build();
    }

    private String consumptionId;

    @Override
    public Attribute<String> listenerId() {
        return AttributeBuilder.single(String.class)
                .read(() -> listenerId)
                .write(value -> listenerId = value)
                .build();
    }

    private String listenerId;

    @Override
    public Attribute<String> rawMessage() {
        return AttributeBuilder.single(String.class)
                .read(() -> rawMessage)
                .write(value -> rawMessage = value)
                .build();
    }

    private String rawMessage;

    @Override
    public Attribute<String> error() {
        return AttributeBuilder.single(String.class)
                .read(() -> error)
                .write(value -> error = value)
                .build();
    }

    private String error;
}
