package poussecafe.storage.internal;

import java.util.Objects;
import java.util.function.Function;
import poussecafe.storage.SortDirection;

public class Order<T, U extends Comparable<?>> {

    public static class Builder<V, W extends Comparable<?>> {

        private Order<V, W> order = new Order<>();

        public Builder<V, W> valueSupplier(Function<V, W> valueSupplier) {
            order.valueSupplier = valueSupplier;
            return this;
        }

        public Builder<V, W> sortDirection(SortDirection sortDirection) {
            order.sortDirection = sortDirection;
            return this;
        }

        public Order<V, W> build() {
            Objects.requireNonNull(order.valueSupplier);
            Objects.requireNonNull(order.sortDirection);
            return order;
        }
    }

    private Order() {

    }

    private Function<T, U> valueSupplier;

    public Function<T, U> valueSupplier() {
        return valueSupplier;
    }

    private SortDirection sortDirection;

    public SortDirection sortDirection() {
        return sortDirection;
    }
}
