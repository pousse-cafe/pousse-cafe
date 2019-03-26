package poussecafe.shop.adapters.storage;

import java.io.Serializable;
import poussecafe.shop.domain.CustomerKey;
import poussecafe.shop.domain.OrderKey;
import poussecafe.shop.domain.ProductKey;

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
