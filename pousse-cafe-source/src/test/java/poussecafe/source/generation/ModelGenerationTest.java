package poussecafe.source.generation;

import java.io.IOException;
import org.junit.Test;
import poussecafe.source.emil.parser.TreeAnalyzer;
import poussecafe.source.emil.parser.TreeParser;
import poussecafe.source.model.Model;

public class ModelGenerationTest extends GenerationTest {

    @Test
    public void newModel() throws IOException { // NOSONAR - add assert - see parent
        givenCoreGenerator();
        givenStorageGenerator();
        givenModel();
        whenGeneratingCode();
        thenGeneratedCodeMatchesExpected();
    }

    protected void givenModel() throws IOException {
        var tree = TreeParser.parseInputStream(getClass().getResourceAsStream("/Process1.emil"));
        var analyzer = new TreeAnalyzer.Builder()
                .tree(tree)
                .basePackage("poussecafe.source.generation.generatedfull")
                .build();
        analyzer.analyze();
        model = analyzer.model();
    }

    private Model model;

    protected Model model() {
        return model;
    }

    @Override
    protected void whenGeneratingCoreCode() {
        generator().generate(model);
    }

    @Test
    public void updateExistingModel() throws IOException { // NOSONAR - add assert - see parent
        givenCoreGenerator();
        givenStorageGenerator();
        givenModel();
        givenExisingCode();
        whenGeneratingCode();
        thenGeneratedCodeMatchesExpected();
    }

    @Override
    protected void givenStorageGenerator() {

    }

    @Override
    protected void whenGeneratingStorageCode() {

    }

    @Override
    protected String[] packageNameSegments() {
        return new String[] { "poussecafe", "source", "generation", "generatedfull" };
    }
}
