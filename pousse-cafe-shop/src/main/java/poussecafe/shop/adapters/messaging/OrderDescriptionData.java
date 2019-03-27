package poussecafe.shop.adapters.messaging;

import java.io.Serializable;
import poussecafe.shop.domain.CustomerKey;
import poussecafe.shop.domain.OrderDescription;

@SuppressWarnings("serial")
public class OrderDescriptionData implements Serializable {

    public static OrderDescriptionData adapt(OrderDescription description) {
        OrderDescriptionData data = new OrderDescriptionData();
        data.customerId = description.customerKey().getValue();
        data.reference = description.reference();
        data.units = description.units();
        return data;
    }

    private String customerId;

    private String reference;

    private int units;

    public OrderDescription adapt() {
        return new OrderDescription.Builder()
                .customerKey(new CustomerKey(customerId))
                .reference(reference)
                .units(units)
                .build();
    }
}
