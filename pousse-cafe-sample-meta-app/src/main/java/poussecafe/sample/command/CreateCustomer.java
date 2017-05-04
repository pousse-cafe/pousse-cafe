package poussecafe.sample.command;

import poussecafe.messaging.Command;
import poussecafe.sample.domain.CustomerKey;

import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;

public class CreateCustomer extends Command {

    private CustomerKey customerKey;

    public CreateCustomer(CustomerKey customerKey) {
        setCustomerKey(customerKey);
    }

    public CustomerKey getCustomerKey() {
        return customerKey;
    }

    private void setCustomerKey(CustomerKey customerKey) {
        checkThat(value(customerKey).notNull().because("Customer key cannot be null"));
        this.customerKey = customerKey;
    }

}
