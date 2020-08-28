package poussecafe.storage.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;
import poussecafe.storage.Page;
import poussecafe.storage.SortDirection;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;

public class StreamBuilder<T> {

    /**
     * @deprecated use StreamBuilder constructor
     */
    @Deprecated(since = "0.23")
    public static class Builder<U> {

        private StreamBuilder<U> streamBuilder;

        public Builder<U> dataClass(Class<U> dataClass) {
            return this;
        }

        public Builder<U> stream(Stream<U> stream) {
            streamBuilder = new StreamBuilder<>(stream);
            return this;
        }

        public StreamBuilder<U> build() {
            Objects.requireNonNull(streamBuilder);
            return streamBuilder;
        }
    }

    public StreamBuilder(Stream<T> stream) {
        requireNonNull(stream);
        this.stream = stream;
    }

    private Stream<T> stream;

    public <U> StreamBuilder<T> optionalAndEqual(Function<T, U> valueSupplier, Optional<U> expectedValue) {
        if(expectedValue.isPresent()) {
            andEqual(valueSupplier, expectedValue.get());
        }
        return this;
    }

    public <U> StreamBuilder<T> andEqual(Function<T, U> valueSupplier, U expectedValue) {
        and(data -> expectedValue.equals(valueSupplier.apply(data)));
        return this;
    }

    public <U extends Comparable<?>> StreamBuilder<T> optionalAndLowerOrEqual(Function<T, U> valueSupplier,
            Optional<U> referenceValue) {
        if(referenceValue.isPresent()) {
            andLowerOrEqual(valueSupplier, referenceValue.get());
        }
        return this;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public <U extends Comparable> StreamBuilder<T> andLowerOrEqual(Function<T, U> valueSupplier,
            U referenceValue) {
        and(data -> valueSupplier.apply(data).compareTo(referenceValue) <= 0);
        return this;
    }

    public <U extends Comparable<?>> StreamBuilder<T> optionalAndGreaterOrEqual(Function<T, U> valueSupplier,
            Optional<U> referenceValue) {
        if(referenceValue.isPresent()) {
            andGreaterOrEqual(valueSupplier, referenceValue.get());
        }
        return this;
    }

    public <U> StreamBuilder<T> andIn(Function<T, U> valueSupplier, Collection<U> expectedValues) {
        if(!expectedValues.isEmpty()) {
            and(data -> expectedValues.contains(valueSupplier.apply(data)));
        }
        return this;
    }

    public StreamBuilder<T> optionalAndContains(Function<T, String> valueSupplier, Optional<String> part) {
        if(part.isPresent()) {
            andContains(valueSupplier, part.get());
        }
        return this;
    }

    public StreamBuilder<T> andContains(Function<T, String> valueSupplier, String string) {
        and(data -> valueSupplier.apply(data).contains(string));
        return this;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public <U extends Comparable> StreamBuilder<T> andGreaterOrEqual(Function<T, U> valueSupplier,
            U referenceValue) {
        and(data -> valueSupplier.apply(data).compareTo(referenceValue) >= 0);
        return this;
    }

    public StreamBuilder<T> and(Predicate<T> predicate) {
        stream = stream.filter(predicate);
        return this;
    }

    public <U extends Comparable<?>> StreamBuilder<T> optionalSort(Function<T, U> valueSupplier, Optional<SortDirection> direction) {
        if(direction.isPresent()) {
            sort(valueSupplier, direction.get());
        }
        return this;
    }

    private <U extends Comparable<?>> StreamBuilder<T> sort(Function<T, U> valueSupplier, SortDirection sortDirection) {
        orders.add(new Order.Builder<T, U>()
                .valueSupplier(valueSupplier)
                .sortDirection(sortDirection)
                .build());
        return this;
    }

    private List<Order<T, ?>> orders = new ArrayList<>();

    public StreamBuilder<T> page(Page page) {
        optionalPage(Optional.of(page));
        return this;
    }

    public StreamBuilder<T> optionalPage(Optional<Page> page) {
        this.page = page;
        return this;
    }

    private Optional<Page> page = Optional.empty();

    public Stream<T> build() {
        requireNonNull(page);

        orderStream();
        pageStream();
        return stream;
    }

    @SuppressWarnings({ "rawtypes"})
    private void orderStream() {
        for(Order<T, ? extends Comparable> order : orders) {
            if(order.sortDirection() == SortDirection.ASC) {
                stream = stream.sorted(ascendingComparator(order.valueSupplier()));
            } else {
                stream = stream.sorted(descendingComparator(order.valueSupplier()));
            }
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private <U extends Comparable> Comparator<T> ascendingComparator(Function<T, U> valueSupplier) {
        return (data1, data2) -> {
            Comparable c1 = valueSupplier.apply(data1);
            Comparable c2 = valueSupplier.apply(data2);
            return c1.compareTo(c2);
        };
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private <U extends Comparable> Comparator<T> descendingComparator(Function<T, U> valueSupplier) {
        return (data1, data2) -> {
            Comparable c1 = valueSupplier.apply(data1);
            Comparable c2 = valueSupplier.apply(data2);
            return c2.compareTo(c1);
        };
    }

    private void pageStream() {
        if(page.isPresent()) {
            stream = stream.skip(page.get().offset());
            stream = stream.limit(page.get().size());
        }
    }

    public List<T> buildAndCollect() {
        return build().collect(toList());
    }
}
