package poussecafe.storage;

import poussecafe.storable.DefaultUnitOfConsequence;
import poussecafe.storable.UnitOfConsequence;

public class DirectEmissionPolicy extends ConsequenceEmissionPolicy {

    @Override
    public void considerUnitEmission(UnitOfConsequence unitOfConsequence) {
        emitUnit(unitOfConsequence);
    }

    @Override
    public UnitOfConsequence newUnitOfConsequence() {
        return new DefaultUnitOfConsequence();
    }

}
