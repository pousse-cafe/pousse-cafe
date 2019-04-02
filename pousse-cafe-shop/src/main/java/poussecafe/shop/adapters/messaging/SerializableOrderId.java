package poussecafe.shop.adapters.messaging;

import java.io.Serializable;
import poussecafe.shop.domain.CustomerId;
import poussecafe.shop.domain.OrderId;
import poussecafe.shop.domain.ProductId;

@SuppressWarnings("serial")
public class SerializableOrderId implements Serializable {

    public static SerializableOrderId adapt(OrderId orderId) {
        SerializableOrderId data = new SerializableOrderId();
        data.productId = orderId.getProductId().getValue();
        data.customerId = orderId.getCustomerId().getValue();
        data.reference = orderId.getReference();
        return data;
    }

    private String productId;

    private String customerId;

    private String reference;

    public OrderId adapt() {
        return new OrderId(new ProductId(productId), new CustomerId(customerId), reference);
    }
}
