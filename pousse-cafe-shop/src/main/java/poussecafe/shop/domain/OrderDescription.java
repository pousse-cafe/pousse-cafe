package poussecafe.shop.domain;

import java.util.Objects;

public class OrderDescription {

    public static class Builder {

        private OrderDescription description = new OrderDescription();

        public Builder customerKey(CustomerKey customerKey) {
            description.customerKey = customerKey;
            return this;
        }

        public Builder reference(String reference) {
            description.reference = reference;
            return this;
        }

        public Builder units(int units) {
            description.units = units;
            return this;
        }

        public OrderDescription build() {
            Objects.requireNonNull(description.customerKey);
            Objects.requireNonNull(description.reference);
            if(description.units <= 0) {
                throw new IllegalArgumentException();
            }
            return description;
        }
    }

    private OrderDescription() {

    }

    private CustomerKey customerKey;

    public CustomerKey customerKey() {
        return customerKey;
    }

    private String reference;

    public String reference() {
        return reference;
    }

    private int units;

    public int units() {
        return units;
    }
}
