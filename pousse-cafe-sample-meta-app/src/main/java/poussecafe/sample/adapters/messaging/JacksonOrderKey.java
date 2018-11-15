package poussecafe.sample.adapters.messaging;

import poussecafe.sample.domain.CustomerKey;
import poussecafe.sample.domain.OrderKey;
import poussecafe.sample.domain.ProductKey;

public class JacksonOrderKey {

    public static JacksonOrderKey adapt(OrderKey orderKey) {
        JacksonOrderKey data = new JacksonOrderKey();
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
