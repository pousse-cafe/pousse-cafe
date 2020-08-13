package poussecafe.source.generation;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import poussecafe.files.Difference;
import poussecafe.files.DifferenceType;
import poussecafe.files.Tree;
import poussecafe.source.model.Aggregate;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class CodeGeneratorTest {

    @Before
    public void clearExistingCode() throws IOException {
        Tree.delete(sourceDirectory);
    }

    @Test
    public void newAggregate() {
        givenCodeGenerator();
        givenAggregate();
        whenGeneratingCodeOfAggregate();
        thenGeneratedCodeMatchesExpected();
    }

    private void givenCodeGenerator() {
        generator = new CodeGenerator.Builder()
                .sourceDirectory(sourceDirectory)
                .build();
    }

    private CodeGenerator generator;

    private void givenAggregate() {
        aggregate = new Aggregate.Builder()
                .name("MyAggregate")
                .packageName("poussecafe.source.generation.generated")
                .build();
    }

    private Aggregate aggregate;

    private void whenGeneratingCodeOfAggregate() {
        generator.addAggregate(aggregate);
    }

    private Path sourceDirectory = Path.of(System.getProperty("java.io.tmpdir"), "pousse-cafe-test-generator");

    private void thenGeneratedCodeMatchesExpected() {
        Path targetDirectory = sourceDirectory.resolve(Path.of("poussecafe", "source", "generation", "generated"));
        Path expectedDocDirectory = Paths.get(System.getProperty("user.dir"), "src", "test", "java", "poussecafe", "source", "generation", "generated");
        try {
            assertDifferences(poussecafe.files.Tree.compareTrees(targetDirectory, expectedDocDirectory));
        } catch (IOException e) {
            fail();
        }
    }

    private void assertDifferences(List<Difference> differences) {
        for(Difference difference : differences) {
            if(difference.type() == DifferenceType.TARGET_DOES_NOT_EXIST) {
                assertTrue("File " + difference.relativePath() + " does not exist", false);
            } else if(difference.type() == DifferenceType.CONTENT_DOES_NOT_MATCH) {
                assertTrue("File content of " + difference.relativePath() + " does not match", false);
            }
        }
    }

    @Test
    public void updateExistingAggregate() {
        givenCodeGenerator();
        givenAggregate();
        givenExisingCode();
        whenGeneratingCodeOfAggregate();
        thenGeneratedCodeMatchesExpected();
    }

    private void givenExisingCode() {
        whenGeneratingCodeOfAggregate();
    }
}
