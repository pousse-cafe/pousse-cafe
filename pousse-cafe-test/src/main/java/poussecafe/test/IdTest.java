package poussecafe.test;

import java.util.Collections;
import java.util.List;
import org.junit.Test;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public abstract class IdTest<K> {

    protected K referenceId;

    protected List<Object> otherIds;

    private List<Boolean> equalities;

    private int referenceHashCode;

    private List<Integer> otherHashCodes;

    private String referenceStringValue;

    private List<String> otherStringValues;

    @Test
    public void sameIdInstanceIsEqual() {
        givenSameIdInstance();
        whenTestingEquality();
        thenEqualityIs(true);
    }

    private void givenSameIdInstance() {
        referenceId = referenceId();
        otherIds = asList(referenceId);
    }

    protected abstract K referenceId();

    private void whenTestingEquality() {
        equalities = otherIds.stream().map(id2 -> referenceId.equals(id2)).collect(toList());
    }

    private void thenEqualityIs(boolean expectedEquality) {
        for (Boolean equality : equalities) {
            assertThat(equality, is(expectedEquality));
        }
    }

    @Test
    public void sameIdIsEqual() {
        givenSameIds();
        whenTestingEquality();
        thenEqualityIs(true);
    }

    private void givenSameIds() {
        referenceId = referenceId();
        otherIds = asList(referenceId());
    }

    @Test
    public void sameIdHasSameHashCode() {
        givenSameIds();
        whenComputingHashCode();
        thenHashCodesAreEqual();
    }

    private void whenComputingHashCode() {
        referenceHashCode = referenceId.hashCode();
        otherHashCodes = otherIds.stream().map(id2 -> id2.hashCode()).collect(toList());
    }

    private void thenHashCodesAreEqual() {
        for (Integer hashCode2 : otherHashCodes) {
            assertTrue(referenceHashCode == hashCode2);
        }
    }

    @Test
    public void differentIdsAreNotEqual() {
        givenDifferentIds();
        whenTestingEquality();
        thenEqualityIs(false);
    }

    private void givenDifferentIds() {
        referenceId = referenceId();
        otherIds = otherIds();
    }

    protected abstract List<Object> otherIds();

    @Test
    public void differentIdsHaveDifferentHashCodes() {
        givenDifferentIds();
        whenComputingHashCode();
        thenHashCodesAreDifferent();
    }

    private void thenHashCodesAreDifferent() {
        for (Integer hashCode2 : otherHashCodes) {
            assertTrue(referenceHashCode != hashCode2);
        }
    }

    @Test
    public void toStringWorks() {
        givenDifferentIds();
        whenComputingStringValue();
        thenStringValueIsAsExpected();
    }

    private void whenComputingStringValue() {
        referenceStringValue = referenceId.toString();
        otherStringValues = otherIds.stream().map(id2 -> id2.toString()).collect(toList());
    }

    protected void thenStringValueIsAsExpected() {
        assertThat(referenceStringValue, notNullValue());
        for (String stringValue2 : otherStringValues) {
            assertThat(stringValue2, notNullValue());
            assertTrue(!referenceStringValue.equals(stringValue2));
        }
    }

    @Test
    public void idIsNotEqualToNull() {
        givenOnlyOneId();
        whenTestingEquality();
        thenEqualityIs(false);
    }

    protected void givenOnlyOneId() {
        referenceId = referenceId();
        otherIds = asList((K) null);
    }

    @Test
    public void differentTypeIsNotEqual() {
        givenDifferentTypes();
        whenTestingEquality();
        thenEqualityIs(false);
    }

    private void givenDifferentTypes() {
        referenceId = referenceId();
        otherIds = asList(differentTypeInstance());
    }

    protected Object differentTypeInstance() {
        return Collections.emptyList(); // It is rather unlikely to have a list used as id
    }
}
