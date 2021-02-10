package poussecafe.source.generation;

import java.io.IOException;
import org.junit.Test;
import poussecafe.source.emil.parser.TreeAnalyzer;
import poussecafe.source.emil.parser.TreeParser;
import poussecafe.source.model.SourceModel;

public class ModelGenerationTest extends GenerationTest {

    @Test
    public void newModel() throws IOException { // NOSONAR - add assert - see parent
        givenCoreGenerator();
        givenStorageGenerator();
        givenModel("Process1.emil");
        whenGeneratingCode();
        thenGeneratedCodeMatchesExpected();
    }

    protected void givenModel() throws IOException {
        givenModel("Process1.emil");
    }

    protected void givenModel(String emilFile) throws IOException {
        var tree = TreeParser.parseInputStream(getClass().getResourceAsStream("/" + emilFile));
        var analyzer = new TreeAnalyzer.Builder()
                .tree(tree)
                .basePackage("poussecafe.source.generation.generatedfull")
                .build();
        analyzer.analyze();
        model = analyzer.model();
    }

    private SourceModel model;

    protected SourceModel model() {
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
        givenModel("Process1.emil");
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

    @Test
    public void modelUpdate() throws IOException { // NOSONAR - add assert - see parent
        givenCoreGenerator();
        givenStorageGenerator();
        givenCodeFragment();
        givenModel();
        whenGeneratingCode();
        thenGeneratedCodeMatchesExpected();
    }

    private void givenCodeFragment() throws IOException {
        givenModel("Process1Fragment.emil");
        whenGeneratingCode();
    }
}
