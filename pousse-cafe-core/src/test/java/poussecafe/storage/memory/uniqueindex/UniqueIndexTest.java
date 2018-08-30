package poussecafe.storage.memory.uniqueindex;

import java.util.Optional;
import org.hamcrest.Matcher;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

public class UniqueIndexTest {

    @Test
    public void simpleAddition() {
        givenUniqueIndex();
        givenDataHasValue1();
        whenAddingData();
        thenThrownExceptionIs(nullValue());
    }

    private void givenUniqueIndex() {
        index = new UniqueIndex.Builder<Data>()
                .name("test")
                .uniqueDataProducer(data -> data.uniqueField)
                .build();
    }

    private UniqueIndex<Data> index;

    private static class Data {

        public String uniqueField;
    }

    private void givenDataHasValue1() {
        data = value1();
    }

    private Data value1() {
        Data data = new Data();
        data.uniqueField = "value";
        return data;
    }

    private Data data;

    private void whenAddingData() {
        try {
            AdditionPlan plan = index.prepareAddition(data);
            plan.commit();
        } catch (UniqueIndexException e) {
            thrownException = e;
        }
    }

    private UniqueIndexException thrownException;

    private void thenThrownExceptionIs(Matcher<Object> matcher) {
        assertThat(thrownException, matcher);
    }

    @Test
    public void duplicatesDetected() {
        givenUniqueIndex();
        givenDataHasValue1();
        whenAddingDataTwice();
        thenThrownExceptionIs(notNullValue());
    }

    private void whenAddingDataTwice() {
        whenAddingData();
        whenAddingData();
    }

    @Test
    public void simpleUpdate() {
        givenUniqueIndex();
        givenNoOldData();
        givenDataHasValue1();
        whenUpdatingData();
        thenThrownExceptionIs(nullValue());
    }

    private void givenNoOldData() {
        oldData = Optional.empty();
    }

    private Optional<Data> oldData;

    private void whenUpdatingData() {
        try {
            UpdatePlan plan = index.prepareUpdate(oldData, data);
            plan.commit();
        } catch (UniqueIndexException e) {
            thrownException = e;
        }
    }

    @Test
    public void updateWithSameData() {
        givenUniqueIndex();
        givenDataHasValue1();
        givenSameOldData();
        whenUpdatingData();
        thenThrownExceptionIs(nullValue());
    }

    private void givenSameOldData() {
        oldData = Optional.of(data);
    }

    @Test
    public void updateWithDifferentData() {
        givenUniqueIndex();
        givenDataHasValue1();
        givenOldDataHasValue2();
        whenUpdatingData();
        thenThrownExceptionIs(nullValue());
    }

    private void givenOldDataHasValue2() {
        oldData = Optional.of(value2());
    }

    private Data value2() {
        Data oldData = new Data();
        oldData.uniqueField = "differentValue";
        return oldData;
    }

    @Test
    public void updateWithDuplicateData() {
        givenUniqueIndex();
        givenDataHasValue1();
        whenAddingData();
        givenNoOldData();
        givenDataHasValue1();
        whenUpdatingData();
        thenThrownExceptionIs(notNullValue());
    }
}
