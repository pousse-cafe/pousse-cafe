package poussecafe.source.generation;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import poussecafe.files.Difference;
import poussecafe.files.DifferenceType;
import poussecafe.files.Tree;
import poussecafe.source.model.Aggregate;

import static java.util.stream.Collectors.joining;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public abstract class GeneratorTest {

    @Before
    public void clearExistingCode() throws IOException {
        Tree.delete(sourceDirectory);
    }

    @Test
    public void newAggregate() { // NOSONAR - add assert - see parent
        givenCoreGenerator();
        givenStorageGenerator();
        givenAggregate();
        whenGeneratingCode();
        thenGeneratedCodeMatchesExpected();
    }

    protected abstract void givenStorageGenerator();

    private void whenGeneratingCode() {
        whenGeneratingCoreCode();
        whenGeneratingStorageCode();
    }

    protected abstract void whenGeneratingStorageCode();

    private Path sourceDirectory = Path.of(System.getProperty("java.io.tmpdir"), tempDirectoryName());

    private String tempDirectoryName() {
        return getClass().getSimpleName();
    }

    protected Path sourceDirectory() {
        return sourceDirectory;
    }

    protected Path expectedTreeDirectory() {
        var packageSegments = packageNameSegments();
        var pathSegments = new String[packageSegments.length + 3];
        pathSegments[0] = "src";
        pathSegments[1] = "test";
        pathSegments[2] = "java";
        System.arraycopy(packageSegments, 0, pathSegments, 3, packageSegments.length);
        return Tree.path(pathSegments);
    }

    protected void givenAggregate() {
        aggregate = new Aggregate.Builder()
                .name("MyAggregate")
                .packageName(Arrays.stream(packageNameSegments()).collect(joining(".")))
                .build();
    }

    private Aggregate aggregate;

    protected abstract String[] packageNameSegments();

    protected Aggregate aggregate() {
        return aggregate;
    }

    protected void givenCoreGenerator() {
        generator = new CoreCodeGenerator.Builder()
                .sourceDirectory(sourceDirectory())
                .build();
    }

    private CoreCodeGenerator generator;

    protected void whenGeneratingCoreCode() {
        generator.generate(aggregate());
    }

    protected void thenGeneratedCodeMatchesExpected() {
        try {
            assertDifferences(poussecafe.files.Tree.compareTrees(subjectTreeDirectory(), expectedTreeDirectory()));
        } catch (IOException e) {
            fail();
        }
    }

    private Path subjectTreeDirectory() {
        var segments = packageNameSegments();
        var firstSegment = segments[0];
        var nextSegments = new String[segments.length - 1];
        System.arraycopy(segments, 1, nextSegments, 0, nextSegments.length);
        return sourceDirectory.resolve(Path.of(firstSegment, nextSegments));
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
    public void updateExistingAggregate() { // NOSONAR - add assert - see parent
        givenCoreGenerator();
        givenStorageGenerator();
        givenAggregate();
        givenExisingCode();
        whenGeneratingCode();
        thenGeneratedCodeMatchesExpected();
    }

    private void givenExisingCode() {
        whenGeneratingCode();
    }
}
