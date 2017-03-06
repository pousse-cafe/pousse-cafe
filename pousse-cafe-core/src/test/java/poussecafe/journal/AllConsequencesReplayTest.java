package poussecafe.journal;

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import poussecafe.consequence.Consequence;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AllConsequencesReplayTest extends ConsequenceReplayTest {

    private List<ConsumptionFailure> failures;

    @Test
    public void allFailedReplayedRouted() {
        givenFailedConsequences();
        whenReplayAll();
        thenFailedReplayed();
    }

    private void givenFailedConsequences() {
        failures = new ArrayList<>();
        failures.add(failureWithConsequence("id1"));
        failures.add(failureWithConsequence("id2"));
        failures.add(failureWithConsequence("id3"));
        when(consumptionFailureRepository.findAllConsumptionFailures()).thenReturn(failures);
    }

    protected ConsumptionFailure failureWithConsequence(String consequenceId) {
        Consequence consequence = consequenceWithId(consequenceId);
        ConsumptionFailure entry = failureWithConsequence(consequence);
        return entry;
    }

    private void whenReplayAll() {
        consequenceReplayer.replayAllFailedConsequences();
    }

    private void thenFailedReplayed() {
        for (ConsumptionFailure failedEntry : failures) {
            verify(consequenceRouter).routeConsequence(failedEntry.getConsequence());
        }
    }
}
