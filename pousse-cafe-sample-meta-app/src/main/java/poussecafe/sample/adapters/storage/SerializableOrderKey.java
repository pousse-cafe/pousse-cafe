package poussecafe.sample.adapters.storage;

import java.io.Serializable;
import poussecafe.sample.domain.CustomerKey;
import poussecafe.sample.domain.OrderKey;
import poussecafe.sample.domain.ProductKey;

@SuppressWarnings("serial")
public class SerializableOrderKey implements Serializable {

    public SerializableOrderKey(OrderKey key) {
        productKey = key.getProductKey().getValue();
        customerKey = key.getCustomerKey().getValue();
        reference = key.getReference();
    }

    private String productKey;

    private String customerKey;

    private String reference;

    public OrderKey toOrderKey() {
        return new OrderKey(new ProductKey(productKey), new CustomerKey(customerKey), reference);
    }

}
