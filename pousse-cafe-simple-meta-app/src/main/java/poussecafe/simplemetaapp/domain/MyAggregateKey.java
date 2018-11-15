package poussecafe.simplemetaapp.domain;
import poussecafe.util.StringKey;

/*
 * It is recommended to have specific classes to define aggregate root keys, this helps in writing self-explanatory
 * interfaces.
 */
public class MyAggregateKey extends StringKey {

    public MyAggregateKey(String value) {
        super(value);
    }

}
