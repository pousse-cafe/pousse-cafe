package poussecafe.shop.adapters.storage;

import java.io.Serializable;
import poussecafe.shop.domain.CustomerId;
import poussecafe.shop.domain.OrderId;
import poussecafe.shop.domain.ProductId;

@SuppressWarnings("serial")
public class OrderIdData implements Serializable {

    public static OrderIdData adapt(OrderId id) {
        OrderIdData data = new OrderIdData();
        data.productId = id.getProductId().stringValue();
        data.customerId = id.getCustomerId().stringValue();
        data.reference = id.getReference();
        return data;
    }

    private String productId;

    private String customerId;

    private String reference;

    public OrderId adapt() {
        return new OrderId(new ProductId(productId), new CustomerId(customerId), reference);
    }
}
