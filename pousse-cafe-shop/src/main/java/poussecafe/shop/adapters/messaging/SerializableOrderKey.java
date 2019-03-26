package poussecafe.shop.adapters.messaging;

import java.io.Serializable;
import poussecafe.shop.domain.CustomerKey;
import poussecafe.shop.domain.OrderKey;
import poussecafe.shop.domain.ProductKey;

@SuppressWarnings("serial")
public class SerializableOrderKey implements Serializable {

    public static SerializableOrderKey adapt(OrderKey orderKey) {
        SerializableOrderKey data = new SerializableOrderKey();
        data.productId = orderKey.getProductKey().getValue();
        data.customerId = orderKey.getCustomerKey().getValue();
        data.reference = orderKey.getReference();
        return data;
    }

    private String productId;

    private String customerId;

    private String reference;

    public OrderKey adapt() {
        return new OrderKey(new ProductKey(productId), new CustomerKey(customerId), reference);
    }
}
