package poussecafe.doc;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ProcessDescriptionTest {

    @Test
    public void processDescriptionParsesAsExpected() {
        givenProcessDescriptionString("ProcessName A small description.");
        whenParsing();
        thenProcessDescriptionIs(new ProcessDescription.Builder()
                .name("ProcessName")
                .description("A small description.")
                .build());
    }

    private void givenProcessDescriptionString(String processDescriptionString) {
        this.processDescriptionString = processDescriptionString;
    }

    private String processDescriptionString;

    private void whenParsing() {
        processDescription = ProcessDescription.parse(processDescriptionString);
    }

    private ProcessDescription processDescription;

    private void thenProcessDescriptionIs(ProcessDescription description) {
        assertThat(processDescription, is(description));
    }
}
