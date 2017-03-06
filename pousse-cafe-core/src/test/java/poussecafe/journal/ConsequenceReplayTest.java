package poussecafe.journal;

import org.junit.Before;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import poussecafe.consequence.Consequence;
import poussecafe.consequence.ConsequenceRouter;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public abstract class ConsequenceReplayTest {

    @Mock
    protected ConsumptionFailureRepository consumptionFailureRepository;

    @Mock
    protected ConsequenceRouter consequenceRouter;

    @InjectMocks
    protected ConsequenceReplayer consequenceReplayer;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    protected ConsumptionFailure failureWithConsequence(Consequence consequence) {
        ConsumptionFailure failure = mock(ConsumptionFailure.class);
        when(failure.getConsequence()).thenReturn(consequence);
        return failure;
    }

    protected void thenConsequenceIsRouted(Consequence consequence) {
        verify(consequenceRouter).routeConsequence(consequence);
    }

    protected void thenConsequenceIsNotRouted(Consequence consequence) {
        verify(consequenceRouter, never()).routeConsequence(consequence);
    }

    protected Consequence consequenceWithId(String id) {
        Consequence consequence = mock(Consequence.class);
        when(consequence.getId()).thenReturn(id);
        return consequence;
    }
}
