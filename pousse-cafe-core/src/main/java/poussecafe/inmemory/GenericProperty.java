package poussecafe.inmemory;

import java.util.HashMap;
import java.util.Map;

import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;
import static poussecafe.check.Predicates.emptyOrNullString;
import static poussecafe.check.Predicates.not;

public class GenericProperty<T> extends BaseProperty<T> {

    public GenericProperty(Map<String, Object> data, String name, Class<T> valueClass) {
        super(valueClass);
        setData(data);
        setName(name);
    }

    private void setData(Map<String, Object> data) {
        checkThat(value(data).notNull());
        this.data = data;
    }

    private Map<String, Object> data = new HashMap<>();

    private void setName(String name) {
        checkThat(value(name).verifies(not(emptyOrNullString())));
        this.name = name;
    }

    private String name;

    @SuppressWarnings("unchecked")
    @Override
    protected T getValue() {
        return (T) data.get(name);
    }

    @Override
    public void setValue(T value) {
        data.put(name, value);
    }

}
