package poussecafe.shop.command;

import java.util.Objects;
import poussecafe.shop.domain.CustomerKey;

public class CreateCustomer {

    private CustomerKey customerKey;

    public CreateCustomer(CustomerKey customerKey) {
        setCustomerKey(customerKey);
    }

    public CustomerKey getCustomerKey() {
        return customerKey;
    }

    private void setCustomerKey(CustomerKey customerKey) {
        Objects.requireNonNull(customerKey);
        this.customerKey = customerKey;
    }

}
