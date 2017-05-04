package poussecafe.storable;

import org.junit.Test;
import poussecafe.storage.MessageSendingPolicy;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public abstract class ActiveStorableRepositoryTest<K, S extends ActiveStorable<K, D>, D extends StorableData<K>>
extends StorableRepositoryTest<K, S, D> {

    private MessageSendingPolicy messageSendingPolicy;

    private MessageCollection newMessageCollection;

    @Override
    public void setupRepository() {
        super.setupRepository();

        messageSendingPolicy = mock(MessageSendingPolicy.class);
        repository().setMessageSendingPolicy(messageSendingPolicy);
    }

    private ActiveStorableRepository<S, K, D> repository() {
        return (ActiveStorableRepository<S, K, D>) repository;
    }

    @Test
    public void foundDataHasMessageCollection() {
        givenKey();
        givenDataForKey();
        givenMessageSendingPolicyBuildsCollection();
        whenFindingDataWithKey();
        thenFoundStorableHasMessageCollection();
    }

    private void givenMessageSendingPolicyBuildsCollection() {
        newMessageCollection = mock(MessageCollection.class);
        when(messageSendingPolicy.newMessageCollection()).thenReturn(newMessageCollection);
    }

    private void thenFoundStorableHasMessageCollection() {
        assertThat(storable.getMessageCollection(), is(newMessageCollection));
    }

    @Test
    public void gotDataHasMessageCollection() {
        givenKey();
        givenMessageSendingPolicyBuildsCollection();
        givenDataForKey();
        whenGettingDataWithKey();
        thenFoundStorableHasMessageCollection();
    }

    @Override
    protected void givenStorable() {
        super.givenStorable();
        MessageCollection messageCollection = mock(MessageCollection.class);
        when(storable.getMessageCollection()).thenReturn(messageCollection);
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
        verify(messageSendingPolicy).considerSending(storable.getMessageCollection());
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
        verifyZeroInteractions(messageSendingPolicy);
    }
}
