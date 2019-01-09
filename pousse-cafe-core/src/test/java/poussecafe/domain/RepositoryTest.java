package poussecafe.domain;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import poussecafe.util.ReflectionUtils;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public abstract class RepositoryTest<K, S extends AggregateRoot<K, D>, D extends EntityData<K>> {

    protected Class<D> dataClass;

    protected Repository<S, K, D> repository;

    protected EntityDataAccess<K, D> dataAccess;

    private ComponentFactory primitiveFactory = new ComponentFactory();

    protected K key;

    protected S entity;

    protected D foundOrCreatedData;

    @Before
    public void setupRepository() {
        dataClass = dataClass();
        repository = buildRepository();

        dataAccess = mockEntityDataAccess();
        repository.setDataAccess(dataAccess);

        ReflectionUtils.access(repository).set("componentFactory", primitiveFactory);
        registerData(primitiveFactory);
    }

    protected abstract void registerData(ComponentFactory entityFactory);

    @SuppressWarnings("unchecked")
    protected EntityDataAccess<K, D> mockEntityDataAccess() {
        return mock(EntityDataAccess.class);
    }

    protected abstract Class<D> dataClass();

    protected abstract Repository<S, K, D> buildRepository();

    @Test
    public void findWithNoDataReturnsNull() {
        givenNoEntity();
        whenFindingDataWithKey();
        thenFoundEntityMatches(nullValue());
    }

    protected void givenNoEntity() {
        givenKey();
        entity = null;
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
        entity = repository.find(key);
    }

    protected void thenFoundEntityMatches(Matcher<Object> matcher) {
        assertThat(entity, matcher);
    }

    @Test
    public void findWithDataReturnsEntity() {
        givenKey();
        givenDataForKey();
        whenFindingDataWithKey();
        thenFoundEntityHasDataAndMessageCollection();
    }

    protected void givenDataForKey() {
        foundOrCreatedData = mock(dataClass);
        when(dataAccess.findData(key)).thenReturn(foundOrCreatedData);
    }

    protected void thenFoundEntityHasDataAndMessageCollection() {
        assertThat(entity.getData(), is(foundOrCreatedData));
    }

    @Test(expected = DomainException.class)
    public void getWithNoDataThrowsException() {
        givenKey();
        givenNoDataForKey();
        whenGettingDataWithKey();
    }

    protected void whenGettingDataWithKey() {
        entity = repository.get(key);
    }

    @Test
    public void getWithDataReturnsEntity() {
        givenKey();
        givenDataForKey();
        whenGettingDataWithKey();
        thenFoundEntityHasDataAndMessageCollection();
    }

    @Test
    public void addingEntityAddsData() {
        givenProvidedEntity();
        whenAddingEntity();
        thenDataAdded();
    }

    protected void givenProvidedEntity() {
        givenKey();
        givenEntity();
    }

    protected void givenEntity() {
        entity = mockEntity();
        D data = mock(dataClass);
        when(entity.getData()).thenReturn(data);
    }

    protected abstract S mockEntity();

    protected void whenAddingEntity() {
        repository.add(entity);
    }

    protected void thenDataAdded() {
        verify(dataAccess).addData(entity.getData());
    }

    @Test
    public void updatingEntityUpdatesData() {
        givenProvidedEntity();
        whenUpdatingEntity();
        thenDataUpdated();
    }

    protected void whenUpdatingEntity() {
        repository.update(entity);
    }

    protected void thenDataUpdated() {
        verify(dataAccess).updateData(entity.getData());
    }

    @Test
    public void deletingEntityDataDeleted() {
        givenExistingEntity();
        whenDeletingEntity();
        thenDataDeleted();
    }

    protected void givenExistingEntity() {
        givenKey();
        givenDataForKey();
    }

    protected void whenDeletingEntity() {
        repository.delete(key);
    }

    protected void thenDataDeleted() {
        verify(dataAccess).deleteData(key);
    }
}
