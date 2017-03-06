package poussecafe.journal;

import java.util.List;
import poussecafe.consequence.Consequence;
import poussecafe.consequence.ConsequenceRouter;

import static java.util.stream.Collectors.toList;
import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;

public class ConsequenceReplayer {

    private ConsumptionFailureRepository consumptionFailureRepository;

    private ConsequenceRouter consequenceRouter;

    public void replayConsequence(String consequenceId) {
        List<ConsumptionFailure> entries = consumptionFailureRepository.findConsumptionFailures(consequenceId);
        replayFailedConsumptions(entries);
    }

    private void replayFailedConsumptions(List<ConsumptionFailure> entries) {
        replayConsequences(entries.stream().map(ConsumptionFailure::getConsequence).collect(toList()));
    }

    private void replayConsequences(List<Consequence> consequences) {
        for (Consequence consequence : consequences) {
            consequenceRouter.routeConsequence(consequence);
        }
    }

    public void replayAllFailedConsequences() {
        List<ConsumptionFailure> entries = consumptionFailureRepository.findAllConsumptionFailures();
        replayFailedConsumptions(entries);
    }

    public void setConsumptionFailureRepository(ConsumptionFailureRepository consumptionFailureRepository) {
        checkThat(
                value(consumptionFailureRepository).notNull().because("Consumption failure repository cannot be null"));
        this.consumptionFailureRepository = consumptionFailureRepository;
    }

    public void setConsequenceRouter(ConsequenceRouter consequenceRouter) {
        checkThat(value(consequenceRouter).notNull().because("Consequence router cannot be null"));
        this.consequenceRouter = consequenceRouter;
    }
}
