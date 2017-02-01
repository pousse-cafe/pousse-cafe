package poussecafe.storable;

import org.junit.Test;
import poussecafe.storage.ConsequenceEmissionPolicy;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public abstract class ActiveStorableRepositoryTest<K, S extends ActiveStorable<K, D>, D extends StorableData<K>>
extends StorableRepositoryTest<K, S, D> {

    private ConsequenceEmissionPolicy consequenceEmissionPolicy;

    private UnitOfConsequence newUnitOfConsequence;

    @Override
    public void setupRepository() {
        super.setupRepository();

        consequenceEmissionPolicy = mock(ConsequenceEmissionPolicy.class);
        repository().setConsequenceEmissionPolicy(consequenceEmissionPolicy);
    }

    private ActiveStorableRepository<S, K, D> repository() {
        return (ActiveStorableRepository<S, K, D>) repository;
    }

    @Test
    public void foundDataHasUnitOfConsequence() {
        givenKey();
        givenDataForKey();
        givenEventEmissionPolicyBuildsUnitOfConsequence();
        whenFindingDataWithKey();
        thenFoundStorableHasUnitOfConsequence();
    }

    private void givenEventEmissionPolicyBuildsUnitOfConsequence() {
        newUnitOfConsequence = mock(UnitOfConsequence.class);
        when(consequenceEmissionPolicy.newUnitOfConsequence()).thenReturn(newUnitOfConsequence);
    }

    private void thenFoundStorableHasUnitOfConsequence() {
        assertThat(storable.getUnitOfConsequence(), is(newUnitOfConsequence));
    }

    @Test
    public void gotDataHasUnitOfConsequence() {
        givenKey();
        givenEventEmissionPolicyBuildsUnitOfConsequence();
        givenDataForKey();
        whenGettingDataWithKey();
        thenFoundStorableHasUnitOfConsequence();
    }

    @Override
    protected void givenStorable() {
        super.givenStorable();
        UnitOfConsequence unitOfConsequence = mock(UnitOfConsequence.class);
        when(storable.getUnitOfConsequence()).thenReturn(unitOfConsequence);
    }

    @Override
    protected abstract S mockStorable();

    @Test
    public void addingStorableEmitsUnit() {
        givenProvidedStorable();
        whenAddingStorable();
        thenUnitEmitted();
    }

    private void thenUnitEmitted() {
        verify(consequenceEmissionPolicy).considerUnitEmission(storable.getUnitOfConsequence());
    }

    @Test
    public void updatingStorableEmitsUnit() {
        givenProvidedStorable();
        whenUpdatingStorable();
        thenUnitEmitted();
    }

    @Test
    public void deletingNoStorableDoesNothing() {
        givenNoStorable();
        whenDeletingStorable();
        thenNothingHappens();
    }

    private void thenNothingHappens() {
        verifyZeroInteractions(consequenceEmissionPolicy);
    }
}
