package poussecafe.storage;

import poussecafe.consequence.Consequence;
import poussecafe.consequence.ConsequenceRouter;
import poussecafe.storable.UnitOfConsequence;

import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;

public abstract class ConsequenceEmissionPolicy {

    private ConsequenceRouter consequenceRouter;

    public abstract void considerUnitEmission(UnitOfConsequence unitOfConsequence);

    public abstract UnitOfConsequence newUnitOfConsequence();

    protected void emitUnit(UnitOfConsequence unitOfConsequence) {
        for (Consequence consequence : unitOfConsequence.getConsequences()) {
            consequenceRouter.routeConsequence(consequence);
        }
    }

    public void setConsequenceRouter(ConsequenceRouter consequenceRouter) {
        checkThat(value(consequenceRouter).notNull().because("Consequence router cannot be null"));
        this.consequenceRouter = consequenceRouter;
    }

}
