package poussecafe.source.emil.parser;

import java.io.IOException;
import org.junit.Test;
import poussecafe.source.ModelAssertions;
import poussecafe.source.model.SourceModel;

public class TreeAnalyzerTest {

    @Test
    public void analyzeProcess1() throws IOException { // NOSONAR - assertions in ModelAssertions
        givenTree("Process1.emil");
        var analyzer = new TreeAnalyzer.Builder()
                .tree(tree)
                .basePackage("poussecafe.source.testmodel")
                .build();
        analyzer.analyze();
        model = analyzer.model();
        new ModelAssertions(model).thenProcess1AggregateListenersFound();
        new ModelAssertions(model).thenProcess1AggregatesFound();
        new ModelAssertions(model).thenProcess1HasListeners();

        new ModelAssertions(model).thenHasEvent("Event1");
        new ModelAssertions(model).thenHasEvent("Event2");
        new ModelAssertions(model).thenHasEvent("Event3");
        new ModelAssertions(model).thenHasEvent("Event4");
        new ModelAssertions(model).thenHasEvent("Event5");
        new ModelAssertions(model).thenHasEvent("Event6");

        new ModelAssertions(model).thenHasCommand("Command1");
        new ModelAssertions(model).thenHasCommand("Command2");
        new ModelAssertions(model).thenHasCommand("Command3");
        new ModelAssertions(model).thenHasCommand("Command4");
    }

    private void givenTree(String resourceName) throws IOException {
        tree = TreeParser.parseInputStream(getClass().getResourceAsStream("/" + resourceName));
    }

    private Tree tree;

    private SourceModel model;
}
