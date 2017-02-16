package poussecafe.storable;

import java.util.List;
import poussecafe.consequence.Consequence;

public interface UnitOfConsequence {

    void addConsequence(Consequence consequence);

    List<Consequence> getConsequences();
}
