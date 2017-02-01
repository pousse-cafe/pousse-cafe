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

public abstract class KeyTest<K> {

    protected K referenceKey;

    protected List<Object> otherKeys;

    private List<Boolean> equalities;

    private int referenceHashCode;

    private List<Integer> otherHashCodes;

    private String referenceStringValue;

    private List<String> otherStringValues;

    @Test
    public void sameKeyInstanceIsEqual() {
        givenSameKeyInstance();
        whenTestingEquality();
        thenEqualityIs(true);
    }

    private void givenSameKeyInstance() {
        referenceKey = referenceKey();
        otherKeys = asList(referenceKey);
    }

    protected abstract K referenceKey();

    private void whenTestingEquality() {
        equalities = otherKeys.stream().map(key2 -> referenceKey.equals(key2)).collect(toList());
    }

    private void thenEqualityIs(boolean expectedEquality) {
        for (Boolean equality : equalities) {
            assertThat(equality, is(expectedEquality));
        }
    }

    @Test
    public void sameKeyIsEqual() {
        givenSameKeys();
        whenTestingEquality();
        thenEqualityIs(true);
    }

    private void givenSameKeys() {
        referenceKey = referenceKey();
        otherKeys = asList(referenceKey());
    }

    @Test
    public void sameKeyHasSameHashCode() {
        givenSameKeys();
        whenComputingHashCode();
        thenHashCodesAreEqual();
    }

    private void whenComputingHashCode() {
        referenceHashCode = referenceKey.hashCode();
        otherHashCodes = otherKeys.stream().map(key2 -> key2.hashCode()).collect(toList());
    }

    private void thenHashCodesAreEqual() {
        for (Integer hashCode2 : otherHashCodes) {
            assertTrue(referenceHashCode == hashCode2);
        }
    }

    @Test
    public void differentKeysAreNotEqual() {
        givenDifferentKeys();
        whenTestingEquality();
        thenEqualityIs(false);
    }

    private void givenDifferentKeys() {
        referenceKey = referenceKey();
        otherKeys = otherKeys();
    }

    protected abstract List<Object> otherKeys();

    @Test
    public void differentKeysHaveDifferentHashCodes() {
        givenDifferentKeys();
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
        givenDifferentKeys();
        whenComputingStringValue();
        thenStringValueIsAsExpected();
    }

    private void whenComputingStringValue() {
        referenceStringValue = referenceKey.toString();
        otherStringValues = otherKeys.stream().map(key2 -> key2.toString()).collect(toList());
    }

    protected void thenStringValueIsAsExpected() {
        assertThat(referenceStringValue, notNullValue());
        for (String stringValue2 : otherStringValues) {
            assertThat(stringValue2, notNullValue());
            assertTrue(!referenceStringValue.equals(stringValue2));
        }
    }

    @Test
    public void keyIsNotEqualToNull() {
        givenOnlyOneKey();
        whenTestingEquality();
        thenEqualityIs(false);
    }

    protected void givenOnlyOneKey() {
        referenceKey = referenceKey();
        otherKeys = asList((K) null);
    }

    @Test
    public void differentTypeIsNotEqual() {
        givenDifferentTypes();
        whenTestingEquality();
        thenEqualityIs(false);
    }

    private void givenDifferentTypes() {
        referenceKey = referenceKey();
        otherKeys = asList(differentTypeInstance());
    }

    protected Object differentTypeInstance() {
        return Collections.emptyList(); // It is rather unlikely to have a list used as key
    }
}
