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
        considerUnitEmissionAfterAdd(storable, unitOfConsequence);
    }

    protected void considerUnitEmissionAfterAdd(A storable,
            UnitOfConsequence unitOfConsequence) {
        consequenceEmissionPolicy.considerUnitEmission(unitOfConsequence);
    }

    @Override
    protected void updateData(A storable) {
        UnitOfConsequence unitOfConsequence = storable.getUnitOfConsequence();
        super.updateData(storable);
        considerUnitEmissionAfterUpdate(storable, unitOfConsequence);
    }

    protected void considerUnitEmissionAfterUpdate(A storable,
            UnitOfConsequence unitOfConsequence) {
        consequenceEmissionPolicy.considerUnitEmission(unitOfConsequence);
    }

    @Override
    protected void deleteData(A storable) {
        UnitOfConsequence unitOfConsequence = storable.getUnitOfConsequence();
        super.deleteData(storable);
        considerUnitEmissionAfterDelete(storable, unitOfConsequence);
    }

    protected void considerUnitEmissionAfterDelete(A storable,
            UnitOfConsequence unitOfConsequence) {
        consequenceEmissionPolicy.considerUnitEmission(unitOfConsequence);
    }

}
