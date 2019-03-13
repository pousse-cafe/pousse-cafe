package poussecafe.myboundedcontext.domain.myaggregate;
import poussecafe.util.StringKey;

/*
 * It is recommended to have specific classes to define aggregate root keys (instead of a generic String, Integer, ...),
 * this helps in writing self-explanatory interfaces.
 */
public class MyAggregateKey extends StringKey {

    public MyAggregateKey(String value) {
        super(value);
    }

}
