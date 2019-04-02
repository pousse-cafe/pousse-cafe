package poussecafe.shop.domain;

import java.util.Objects;

public class OrderDescription {

    public static class Builder {

        private OrderDescription description = new OrderDescription();

        public Builder customerId(CustomerId customerId) {
            description.customerId = customerId;
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
            Objects.requireNonNull(description.customerId);
            Objects.requireNonNull(description.reference);
            if(description.units <= 0) {
                throw new IllegalArgumentException();
            }
            return description;
        }
    }

    private OrderDescription() {

    }

    private CustomerId customerId;

    public CustomerId customerId() {
        return customerId;
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
