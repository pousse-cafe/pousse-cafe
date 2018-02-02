package poussecafe.domain;

import poussecafe.storable.ConvertingProperty;
import poussecafe.storable.Property;
import poussecafe.storage.memory.InlineProperty;

public class SimpleAggregateData implements SimpleAggregate.Data {

    @Override
    public Property<SimpleAggregateKey> key() {
        return new ConvertingProperty<String, SimpleAggregateKey>(key) {
            @Override
            protected SimpleAggregateKey convertFrom(String f) {
                return new SimpleAggregateKey(f);
            }

            @Override
            protected String convertTo(SimpleAggregateKey f) {
                return f.getId();
            }
        };
    }

    private InlineProperty<String> key = new InlineProperty<>(String.class);
}
