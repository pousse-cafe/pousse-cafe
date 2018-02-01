package poussecafe.storable;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import poussecafe.domain.DomainException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public abstract class IdentifiedStorableRepositoryTest<K, S extends IdentifiedStorable<K, D>, D extends IdentifiedStorableData<K>> {

    protected Class<D> dataClass;

    protected IdentifiedStorableRepository<S, K, D> repository;

    protected IdentifiedStorableDataAccess<K, D> dataAccess;

    private PrimitiveFactory primitiveFactory = new PrimitiveFactory();

    protected K key;

    protected S storable;

    protected D foundOrCreatedData;

    @Before
    public void setupRepository() {
        dataClass = dataClass();
        repository = buildRepository();

        dataAccess = mockStorableDataAccess();
        repository.setDataAccess(dataAccess);

        repository.setPrimitiveFactory(primitiveFactory);
        registerData(primitiveFactory);
    }

    protected abstract void registerData(PrimitiveFactory storableFactory);

    @SuppressWarnings("unchecked")
    protected IdentifiedStorableDataAccess<K, D> mockStorableDataAccess() {
        return mock(IdentifiedStorableDataAccess.class);
    }

    protected abstract Class<D> dataClass();

    protected abstract IdentifiedStorableRepository<S, K, D> buildRepository();

    @Test
    public void findWithNoDataReturnsNull() {
        givenNoStorable();
        whenFindingDataWithKey();
        thenFoundStorableMatches(nullValue());
    }

    protected void givenNoStorable() {
        givenKey();
        storable = null;
        when(dataAccess.findData(key)).thenReturn(null);
    }

    protected void givenKey() {
        key = buildKey();
    }

    protected abstract K buildKey();

    protected void givenNoDataForKey() {
        when(dataAccess.findData(key)).thenReturn(null);
    }

    protected void whenFindingDataWithKey() {
        storable = repository.find(key);
    }

    protected void thenFoundStorableMatches(Matcher<Object> matcher) {
        assertThat(storable, matcher);
    }

    @Test
    public void findWithDataReturnsStorable() {
        givenKey();
        givenDataForKey();
        whenFindingDataWithKey();
        thenFoundStorableHasDataAndMessageCollection();
    }

    protected void givenDataForKey() {
        foundOrCreatedData = mock(dataClass);
        when(dataAccess.findData(key)).thenReturn(foundOrCreatedData);
    }

    protected void thenFoundStorableHasDataAndMessageCollection() {
        assertThat(storable.getData(), is(foundOrCreatedData));
    }

    @Test(expected = DomainException.class)
    public void getWithNoDataThrowsException() {
        givenKey();
        givenNoDataForKey();
        whenGettingDataWithKey();
    }

    protected void whenGettingDataWithKey() {
        storable = repository.get(key);
    }

    @Test
    public void getWithDataReturnsStorable() {
        givenKey();
        givenDataForKey();
        whenGettingDataWithKey();
        thenFoundStorableHasDataAndMessageCollection();
    }

    @Test
    public void addingStorableAddsData() {
        givenProvidedStorable();
        whenAddingStorable();
        thenDataAdded();
    }

    protected void givenProvidedStorable() {
        givenKey();
        givenStorable();
    }

    protected void givenStorable() {
        storable = mockStorable();
        D data = mock(dataClass);
        when(storable.getData()).thenReturn(data);
    }

    protected abstract S mockStorable();

    protected void whenAddingStorable() {
        repository.add(storable);
    }

    protected void thenDataAdded() {
        verify(dataAccess).addData(storable.getData());
    }

    @Test
    public void updatingStorableUpdatesData() {
        givenProvidedStorable();
        whenUpdatingStorable();
        thenDataUpdated();
    }

    protected void whenUpdatingStorable() {
        repository.update(storable);
    }

    protected void thenDataUpdated() {
        verify(dataAccess).updateData(storable.getData());
    }

    @Test
    public void deletingStorableDataDeleted() {
        givenExistingStorable();
        whenDeletingStorable();
        thenDataDeleted();
    }

    protected void givenExistingStorable() {
        givenKey();
        givenDataForKey();
    }

    protected void whenDeletingStorable() {
        repository.delete(key);
    }

    protected void thenDataDeleted() {
        verify(dataAccess).deleteData(key);
    }
}
