package poussecafe.entity;

import org.junit.Test;
import poussecafe.domain.EntityAttributes;
import poussecafe.domain.EntityDataAccess;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public abstract class EntityDataAccessTest {

    private EntityDataAccess<String, Attributes> dataAccess;

    private Attributes foundData;

    private Attributes addedData;

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

    protected abstract EntityDataAccess<String, Attributes> newDataAccess();

    private void whenFindingData() {
        foundData = dataAccess.findData(key1());
    }

    private void thenFoundDataIs(Attributes expected) {
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

    private Attributes data1() {
        Attributes data = data(key1());
        data.setX(10);
        return data;
    }

    private Attributes data(String key) {
        Attributes data = newData();
        data.key().value(key);
        return data;
    }

    private String key1() {
        return "key1";
    }

    protected abstract Attributes newData();

    private void whenFindingAddedData() {
        foundData = dataAccess.findData(addedData.key().value());
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
        Attributes data = data2();
        dataAccess.updateData(data);
        foundData = dataAccess.findData(data.key().value());
    }

    private Attributes data2() {
        Attributes data = data(key1());
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
        dataAccess.deleteData(addedData.key().value());
        foundData = dataAccess.findData(addedData.key().value());
    }

    public static interface Attributes extends EntityAttributes<String> {

        void setX(int x);
    }
}
