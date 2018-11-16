package poussecafe.sample.adapters.messaging;

import java.io.Serializable;
import poussecafe.sample.domain.CustomerKey;
import poussecafe.sample.domain.OrderDescription;

@SuppressWarnings("serial")
public class SerializableOrderDescription implements Serializable {

    public static SerializableOrderDescription adapt(OrderDescription description) {
        SerializableOrderDescription data = new SerializableOrderDescription();
        data.customerId = description.customerKey.getValue();
        data.reference = description.reference;
        data.units = description.units;
        return data;
    }

    public String customerId;

    public String reference;

    public int units;

    public OrderDescription adapt() {
        OrderDescription description = new OrderDescription();
        description.customerKey = new CustomerKey(customerId);
        description.reference = reference;
        description.units = units;
        return description;
    }
}
