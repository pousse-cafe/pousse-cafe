package poussecafe.storable;

public abstract class ActiveStorable<K, D extends StorableData<K>> extends Storable<K, D> {

    private UnitOfConsequence unitOfConsequence;

    void setUnitOfConsequence(UnitOfConsequence unitOfConsequence) {
        this.unitOfConsequence = unitOfConsequence;
    }

    protected UnitOfConsequence getUnitOfConsequence() {
        return unitOfConsequence;
    }
}
