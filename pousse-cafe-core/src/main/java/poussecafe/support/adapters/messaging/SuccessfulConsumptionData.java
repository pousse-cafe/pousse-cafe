package poussecafe.support.adapters.messaging;

import java.io.Serializable;
import poussecafe.attribute.Attribute;
import poussecafe.attribute.AttributeBuilder;
import poussecafe.discovery.MessageImplementation;
import poussecafe.support.model.SuccessfulConsumption;

@MessageImplementation(message = SuccessfulConsumption.class)
@SuppressWarnings("serial")
public class SuccessfulConsumptionData implements Serializable, SuccessfulConsumption {

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
}
