package poussecafe.shop.adapters.messaging;

import java.io.Serializable;
import poussecafe.shop.domain.CustomerId;
import poussecafe.shop.domain.OrderDescription;

@SuppressWarnings("serial")
public class OrderDescriptionData implements Serializable {

    public static OrderDescriptionData adapt(OrderDescription description) {
        OrderDescriptionData data = new OrderDescriptionData();
        data.customerId = description.customerId().stringValue();
        data.reference = description.reference();
        data.units = description.units();
        return data;
    }

    private String customerId;

    private String reference;

    private int units;

    public OrderDescription adapt() {
        return new OrderDescription.Builder()
                .customerId(new CustomerId(customerId))
                .reference(reference)
                .units(units)
                .build();
    }
}
