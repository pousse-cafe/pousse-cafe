package poussecafe.mymodule.domain.myaggregate;
import poussecafe.util.StringId;

/*
 * It is recommended to have specific classes to define aggregate root ids (instead of a generic String, Integer, ...),
 * this helps in writing self-explanatory interfaces.
 */
public class MyAggregateId extends StringId {

    public MyAggregateId(String value) {
        super(value);
    }

}
