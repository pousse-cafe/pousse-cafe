package poussecafe.source.emil.parser;

import java.io.IOException;
import org.junit.Test;

import static java.util.stream.Collectors.joining;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class EmilParserTest {

    @Test
    public void parseValidString() {
        whenParsingString("process Test Command? -> .");
        thenParseSuccess(1);
    }

    private void whenParsingString(String emil) {
        tree = TreeParser.parseString(emil);
    }

    private Tree tree;

    private void thenParseSuccess(int expectedConsumptions) {
        assertTrue(tree.errors().stream().collect(joining("\n")), tree.isValid());
        assertThat(tree.processContext().consumptions().getChildCount(), equalTo(expectedConsumptions));
    }

    @Test
    public void parseInvalidString() {
        whenParsingString("Command -> .");
        thenParseFailure();
    }

    private void thenParseFailure() {
        assertFalse(tree.isValid());
    }

    @Test
    public void parseValidProcessInputStream() throws IOException {
        whenParsingResource("Process1.emil");
        thenParseSuccess(6);
    }

    private void whenParsingResource(String resourceName) throws IOException {
        tree = TreeParser.parseInputStream(getClass().getResourceAsStream("/" + resourceName));
    }

    @Test
    public void parseInvalidInputStream() throws IOException {
        whenParsingResource("invalid.emil");
        thenParseFailure();
    }

    @Test
    public void parseValidAllInputStream() throws IOException {
        whenParsingResource("all.emil");
        thenParseSuccess(6);
    }
}
