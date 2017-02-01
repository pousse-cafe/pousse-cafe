package poussecafe.consequence;

import java.util.UUID;

import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;
import static poussecafe.check.Predicates.emptyOrNullString;
import static poussecafe.check.Predicates.not;

public abstract class Consequence {

    private String id;

    protected Consequence() {
        id = UUID.randomUUID().toString();
    }

    protected Consequence(String id) {
        setId(id);
    }

    public String getId() {
        return id;
    }

    private void setId(String id) {
        checkThat(value(id).verifies(not(emptyOrNullString())).because("ID cannot be null or empty"));
        this.id = id;
    }

}
