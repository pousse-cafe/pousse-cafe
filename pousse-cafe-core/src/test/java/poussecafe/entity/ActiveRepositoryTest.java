package poussecafe.entity;

import org.junit.Test;
import poussecafe.domain.AggregateRoot;
import poussecafe.domain.EntityData;
import poussecafe.domain.MessageCollection;
import poussecafe.domain.RepositoryTest;
import poussecafe.storage.MessageSendingPolicy;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public abstract class ActiveRepositoryTest<K, S extends AggregateRoot<K, D>, D extends EntityData<K>>
        extends RepositoryTest<K, S, D> {

    private MessageSendingPolicy messageSendingPolicy;

    private MessageCollection newMessageCollection;

    @Test
    public void foundDataHasMessageCollection() {
        givenKey();
        givenDataForKey();
        givenMessageSendingPolicyBuildsCollection();
        whenFindingDataWithKey();
        thenFoundEntityHasMessageCollection();
    }

    private void givenMessageSendingPolicyBuildsCollection() {
        newMessageCollection = mock(MessageCollection.class);
        when(messageSendingPolicy.newMessageCollection()).thenReturn(newMessageCollection);
    }

    private void thenFoundEntityHasMessageCollection() {
        assertThat(entity.messageCollection(), is(newMessageCollection));
    }

    @Test
    public void gotDataHasMessageCollection() {
        givenKey();
        givenMessageSendingPolicyBuildsCollection();
        givenDataForKey();
        whenGettingDataWithKey();
        thenFoundEntityHasMessageCollection();
    }

    @Override
    protected void givenEntity() {
        super.givenEntity();
        MessageCollection messageCollection = mock(MessageCollection.class);
        when(entity.messageCollection()).thenReturn(messageCollection);
    }

    @Override
    protected abstract S mockEntity();

    @Test
    public void addingEntityEmitsUnit() {
        givenProvidedEntity();
        whenAddingEntity();
        thenUnitEmitted();
    }

    private void thenUnitEmitted() {
        verify(messageSendingPolicy).considerSending(entity.messageCollection());
    }

    @Test
    public void updatingEntityEmitsUnit() {
        givenProvidedEntity();
        whenUpdatingEntity();
        thenUnitEmitted();
    }

    @Test
    public void deletingNoEntityDoesNothing() {
        givenNoEntity();
        whenDeletingEntity();
        thenNothingHappens();
    }

    private void thenNothingHappens() {
        verifyZeroInteractions(messageSendingPolicy);
    }
}
