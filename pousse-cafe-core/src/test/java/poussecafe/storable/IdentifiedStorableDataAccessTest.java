package poussecafe.storable;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public abstract class IdentifiedStorableDataAccessTest {

    private IdentifiedStorableDataAccess<Data> dataAccess;

    private Data foundData;

    private Data addedData;

    @Test
    public void findReturnsNullWhenNotFound() {
        givenDataAccessWithNoData();
        whenFindingData();
        thenFoundDataIs(null);
    }

    private void givenDataAccessWithNoData() {
        givenDataAccess();
    }

    private void givenDataAccess() {
        dataAccess = newDataAccess();
    }

    protected abstract IdentifiedStorableDataAccess<Data> newDataAccess();

    private void whenFindingData() {
        foundData = dataAccess.findData(key1());
    }

    private void thenFoundDataIs(Data expected) {
        assertThat(foundData, equalTo(expected));
    }

    @Test
    public void findReturnsAddedData() {
        givenDataAccessWithAddedData();
        whenFindingAddedData();
        thenFoundDataIs(addedData);
    }

    private void givenDataAccessWithAddedData() {
        givenDataAccess();
        givenAddedData();
    }

    private void givenAddedData() {
        addData1();
    }

    private void addData1() {
        addedData = data1();
        dataAccess.addData(addedData);
    }

    private Data data1() {
        Data data = data(key1());
        data.setX(10);
        return data;
    }

    private Data data(String key) {
        Data data = newData();
        data.key().set(key);
        return data;
    }

    private String key1() {
        return "key1";
    }

    protected abstract Data newData();

    private void whenFindingAddedData() {
        foundData = dataAccess.findData(addedData.key().get());
    }

    @Test(expected = Exception.class)
    public void addingTwiceThrowsException() {
        givenDataAccessWithNoData();
        whenAddingDataTwice();
    }

    private void whenAddingDataTwice() {
        addData1();
        addData1();
    }

    @Test
    public void updateChangesValue() {
        givenDataAccessWithAddedData();
        whenUpdatingAddedData();
        thenFoundDataIs(data2());
    }

    private void whenUpdatingAddedData() {
        Data data = data2();
        dataAccess.updateData(data);
        foundData = dataAccess.findData(data.key().get());
    }

    private Data data2() {
        Data data = data(key1());
        data.setX(12);
        return data;
    }

    @Test
    public void deleteRemovesData() {
        givenDataAccessWithAddedData();
        whenDeletingAddedData();
        thenFoundDataIs(null);
    }

    private void whenDeletingAddedData() {
        dataAccess.deleteData(addedData.key().get());
        foundData = dataAccess.findData(addedData.key().get());
    }

    public static interface Data extends StorableData {

        Property<String> key();

        void setX(int x);
    }
}
