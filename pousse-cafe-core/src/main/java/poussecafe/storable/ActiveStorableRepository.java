package poussecafe.storable;

import poussecafe.storage.ConsequenceEmissionPolicy;

import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;

public abstract class ActiveStorableRepository<A extends ActiveStorable<K, D>, K, D extends StorableData<K>>
extends StorableRepository<A, K, D> {

    protected ConsequenceEmissionPolicy consequenceEmissionPolicy;

    public void setConsequenceEmissionPolicy(ConsequenceEmissionPolicy consequenceEmissionPolicy) {
        checkThat(value(consequenceEmissionPolicy)
                .notNull()
                .because("Consequence emission policy cannot be null"));
        this.consequenceEmissionPolicy = consequenceEmissionPolicy;
    }

    @Override
    protected A newStorableWithData(D data) {
        A storable = super.newStorableWithData(data);
        storable.setUnitOfConsequence(consequenceEmissionPolicy.newUnitOfConsequence());
        return storable;
    }

    @Override
    protected void addData(A storable) {
        UnitOfConsequence unitOfConsequence = storable.getUnitOfConsequence();
        super.addData(storable);
        consequenceEmissionPolicy.considerUnitEmission(unitOfConsequence);
    }

    @Override
    protected void updateData(A storable) {
        UnitOfConsequence unitOfConsequence = storable.getUnitOfConsequence();
        super.updateData(storable);
        consequenceEmissionPolicy.considerUnitEmission(unitOfConsequence);
    }

}
