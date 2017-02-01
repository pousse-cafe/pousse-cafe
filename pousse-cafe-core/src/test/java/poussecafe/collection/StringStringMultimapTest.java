package poussecafe.collection;

import java.util.HashSet;
import java.util.Set;

import static java.util.Arrays.asList;

public class StringStringMultimapTest extends MultimapTest<String, String> {

    @Override
    protected String key() {
        return "key";
    }

    @Override
    protected String value() {
        return "value";
    }

    @Override
    protected Set<String> values() {
        return new HashSet<>(asList("value1", "value2"));
    }

}
