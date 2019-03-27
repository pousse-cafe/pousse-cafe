package poussecafe.shop.adapters.storage;

import java.io.Serializable;
import poussecafe.shop.domain.CustomerKey;
import poussecafe.shop.domain.OrderKey;
import poussecafe.shop.domain.ProductKey;

@SuppressWarnings("serial")
public class OrderKeyData implements Serializable {

    public static OrderKeyData adapt(OrderKey key) {
        OrderKeyData data = new OrderKeyData();
        data.productId = key.getProductKey().getValue();
        data.customerId = key.getCustomerKey().getValue();
        data.reference = key.getReference();
        return data;
    }

    private String productId;

    private String customerId;

    private String reference;

    public OrderKey adapt() {
        return new OrderKey(new ProductKey(productId), new CustomerKey(customerId), reference);
    }
}
