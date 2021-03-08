package poussecafe.attribute;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.Test;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static poussecafe.collection.Collections.asSet;

public class AttributesWithAutoAdapterTest {

    @Test
    public void singleAttributeWithAutoAdapter() {
        givenSingleAttribute();
        whenWritingSingle(new AdaptedData("test"));
        thenReadingExpectedSingle(new AdaptedData("test"));
    }

    private void givenSingleAttribute() {
        singleAttribute = AttributeBuilder.single(AdaptedData.class)
                .usingAutoAdapter(MyAutoAdapter.class)
                .read(() -> single)
                .write(value -> single = value)
                .build();
    }

    private Attribute<AdaptedData> singleAttribute;

    private MyAutoAdapter single;

    private void whenWritingSingle(AdaptedData adaptedData) {
        singleAttribute.value(adaptedData);
    }

    private void thenReadingExpectedSingle(AdaptedData expected) {
        assertThat(singleAttribute.value(), equalTo(expected));
    }

    @Test
    public void optionalAttributeWithAutoAdapter() {
        givenOptionalAttribute();
        whenWritingOptional(Optional.of(new AdaptedData("test")));
        thenReadingExpectedOptional(Optional.of(new AdaptedData("test")));
    }

    private void givenOptionalAttribute() {
        optionalAttribute = AttributeBuilder.optional(AdaptedData.class)
                .usingAutoAdapter(MyAutoAdapter.class)
                .read(() -> single)
                .write(value -> single = value)
                .build();
    }

    private OptionalAttribute<AdaptedData> optionalAttribute;

    private void whenWritingOptional(Optional<AdaptedData> of) {
        optionalAttribute.value(of);
    }

    private void thenReadingExpectedOptional(Optional<AdaptedData> expected) {
        assertThat(optionalAttribute.value(), equalTo(expected));
    }

    @Test
    public void listAttributeWithAutoAdapter() {
        givenListAttribute();
        whenWritingList(asList(new AdaptedData("test")));
        thenReadingExpectedList(asList(new AdaptedData("test")));
    }

    private void givenListAttribute() {
        listAttribute = AttributeBuilder.list(AdaptedData.class)
                .usingItemAutoAdapter(MyAutoAdapter.class)
                .withList(list)
                .build();
    }

    private ListAttribute<AdaptedData> listAttribute;

    private List<MyAutoAdapter> list = new ArrayList<>();

    private void whenWritingList(List<AdaptedData> list) {
        listAttribute.value(list);
    }

    private void thenReadingExpectedList(List<AdaptedData> expected) {
        assertThat(listAttribute.value(), equalTo(expected));
    }

    @Test
    public void setAttributeWithAutoAdapter() {
        givenSetAttribute();
        whenWritingSet(asSet(new AdaptedData("test")));
        thenReadingExpectedSet(asSet(new AdaptedData("test")));
    }

    private void givenSetAttribute() {
        setAttribute = AttributeBuilder.set(AdaptedData.class)
                .usingItemAutoAdapter(MyAutoAdapter.class)
                .withSet(set)
                .build();
    }

    private SetAttribute<AdaptedData> setAttribute;

    private Set<MyAutoAdapter> set = new HashSet<>();

    private void whenWritingSet(Set<AdaptedData> set) {
        setAttribute.value(set);
    }

    private void thenReadingExpectedSet(Set<AdaptedData> expected) {
        assertThat(setAttribute.value(), equalTo(expected));
    }
}
