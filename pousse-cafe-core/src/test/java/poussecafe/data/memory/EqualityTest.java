package poussecafe.data.memory;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class EqualityTest {

    private Data reference;

    private Data other;

    private boolean equalsResult;

    private int referenceHashCodeResult;

    private int otherHashCodeResult;

    @Test
    public void equalsFalseIfNull() {
        givenOtherNull();
        whenTestingEquality();
        thenEqualsResultIs(false);
    }

    private void givenOtherNull() {
        reference = newDataWithValueSet1();
        other = null;
    }

    private Data newDataWithValueSet1() {
        Data data = InMemoryDataUtils.newDataImplementation(Data.class);
        data.setX(10);
        data.setY(2.4);
        data.setZ("test1");
        return data;
    }

    @Test
    public void equalsTrueIfSameValues() {
        givenDataWithSameValues();
        whenTestingEquality();
        thenEqualsResultIs(true);
    }

    private void givenDataWithSameValues() {
        reference = newDataWithValueSet1();
        other = newDataWithValueSet1();
    }

    private void whenTestingEquality() {
        equalsResult = reference.equals(other);
        referenceHashCodeResult = reference.hashCode();
        if (other != null) {
            otherHashCodeResult = other.hashCode();
        } else {
            otherHashCodeResult = 0;
        }
    }

    private void thenEqualsResultIs(boolean expected) {
        assertThat(equalsResult, is(expected));
        assertThat(referenceHashCodeResult == otherHashCodeResult, is(expected));
    }

    @Test
    public void equalsFalseIfDifferentValues() {
        givenDataWithDifferentValues();
        whenTestingEquality();
        thenEqualsResultIs(false);
    }

    private void givenDataWithDifferentValues() {
        reference = newDataWithValueSet1();
        other = newDataWithValueSet2();
    }

    private Data newDataWithValueSet2() {
        Data data = InMemoryDataUtils.newDataImplementation(Data.class);
        data.setX(12);
        data.setY(3.1);
        data.setZ("test2");
        return data;
    }

    public static interface Data {

        void setX(int x);

        void setY(double y);

        void setZ(String z);
    }
}
