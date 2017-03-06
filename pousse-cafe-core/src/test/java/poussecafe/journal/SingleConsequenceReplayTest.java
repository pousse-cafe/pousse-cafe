package poussecafe.journal;

import org.junit.Test;
import poussecafe.consequence.Consequence;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SingleConsequenceReplayTest extends ConsequenceReplayTest {

    private Consequence consequence;

    @Test
    public void failedConsequenceIsRoutedOnReplay() {
        givenFailedConsequence();
        whenReplayingConsequence();
        thenConsequenceIsRouted();
    }

    private void givenFailedConsequence() {
        givenConsequence();
        givenFailedConsumption();
    }

    private void givenConsequence() {
        consequence = consequenceWithId("consequenceId");
    }

    private void givenFailedConsumption() {
        ConsumptionFailure failure = failureWithConsequence(consequence);
        when(consumptionFailureRepository.findConsumptionFailures(consequence.getId())).thenReturn(asList(failure));
    }

    private void whenReplayingConsequence() {
        consequenceReplayer.replayConsequence(consequence.getId());
    }

    private void thenConsequenceIsRouted() {
        verify(consequenceRouter).routeConsequence(consequence);
    }

    @Test
    public void successConsequenceIsNotRoutedOnReplay() {
        givenNoFailedConsequence();
        whenReplayingConsequence();
        thenConsequenceIsNotRouted();
    }

    private void givenNoFailedConsequence() {
        givenConsequence();
        givenNoFailedConsumption();
    }

    private void givenNoFailedConsumption() {
        when(consumptionFailureRepository.findConsumptionFailures(consequence.getId())).thenReturn(emptyList());
    }

    private void thenConsequenceIsNotRouted() {
        thenConsequenceIsNotRouted(consequence);
    }
}
