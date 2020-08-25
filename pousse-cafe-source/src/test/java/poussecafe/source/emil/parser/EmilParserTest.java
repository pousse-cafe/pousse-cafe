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
        givenParser();
        whenParsingString("process Test Command? -> .");
        thenParseSuccess(1);
    }

    private void givenParser() {
        parser = new TreeParser();
    }

    private TreeParser parser;

    private void whenParsingString(String emil) {
        parser.parseString(emil);
    }

    private void thenParseSuccess(int expectedConsumptions) {
        assertTrue(parser.errors().stream().collect(joining("\n")), parser.success());
        assertThat(parser.consumptions(), equalTo(expectedConsumptions));
    }

    @Test
    public void parseInvalidString() {
        givenParser();
        whenParsingString("Command -> .");
        thenParseFailure();
    }

    private void thenParseFailure() {
        assertFalse(parser.success());
    }

    @Test
    public void parseValidProcessInputStream() throws IOException {
        givenParser();
        whenParsingResource("Process1.emil");
        thenParseSuccess(6);
    }

    private void whenParsingResource(String resourceName) throws IOException {
        parser.parseInputStream(getClass().getResourceAsStream("/" + resourceName));
    }

    @Test
    public void parseInvalidInputStream() throws IOException {
        givenParser();
        whenParsingResource("invalid.emil");
        thenParseFailure();
    }

    @Test
    public void parseValidAllInputStream() throws IOException {
        givenParser();
        whenParsingResource("all.emil");
        thenParseSuccess(6);
    }
}
