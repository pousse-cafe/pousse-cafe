package poussecafe.support.adapters.messaging;

import java.io.Serializable;
import poussecafe.attribute.Attribute;
import poussecafe.attribute.AttributeBuilder;
import poussecafe.contextconfigurer.MessageImplementation;
import poussecafe.support.model.SuccessfulConsumption;

@MessageImplementation(message = SuccessfulConsumption.class)
@SuppressWarnings("serial")
public class SuccessfulConsumptionData implements Serializable, SuccessfulConsumption {

    @Override
    public Attribute<String> consumptionId() {
        return AttributeBuilder.simple(String.class)
                .get(() -> consumptionId)
                .set(value -> consumptionId = value)
                .build();
    }

    private String consumptionId;

    @Override
    public Attribute<String> listenerId() {
        return AttributeBuilder.simple(String.class)
                .get(() -> listenerId)
                .set(value -> listenerId = value)
                .build();
    }

    private String listenerId;

    @Override
    public Attribute<String> rawMessage() {
        return AttributeBuilder.simple(String.class)
                .get(() -> rawMessage)
                .set(value -> rawMessage = value)
                .build();
    }

    private String rawMessage;
}
