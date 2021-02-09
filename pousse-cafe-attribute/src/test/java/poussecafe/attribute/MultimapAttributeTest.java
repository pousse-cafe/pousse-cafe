package poussecafe.attribute;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.junit.Test;
import poussecafe.attribute.adapters.DataAdapters;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

public class MultimapAttributeTest {

    @Test
    public void multimapAttributeWorks() {
        givenMultimapAttribute();
        whenPutting(LocalDate.now(), asList(new BigDecimal("1"), new BigDecimal("2"), new BigDecimal("3")));
        thenValueEquals(LocalDate.now(), asList(new BigDecimal("1"), new BigDecimal("2"), new BigDecimal("3")));
    }

    private void givenMultimapAttribute() {
        attribute = AttributeBuilder.map(LocalDate.class, AttributeBuilder.parametrizedListClass(BigDecimal.class))
                .usingEntryDataAdapters(DataAdapters.stringLocalDate(), DataAdapters
                        .listWithAdapter(DataAdapters.stringBigDecimal()))
                .withMap(countsPerDay)
                .build();
    }

    private MapAttribute<LocalDate, List<BigDecimal>> attribute;

    private HashMap<String, ArrayList<String>> countsPerDay = new HashMap<>();

    private void whenPutting(LocalDate key, List<BigDecimal> value) {
        attribute.value().put(key, value);
    }

    private void thenValueEquals(LocalDate key, List<BigDecimal> value) {
        assertEquals(value, attribute.value().get(key));
    }
}
