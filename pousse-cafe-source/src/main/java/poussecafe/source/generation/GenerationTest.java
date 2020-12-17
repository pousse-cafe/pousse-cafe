package poussecafe.source.generation;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import org.junit.Before;
import poussecafe.files.Difference;
import poussecafe.files.DifferenceType;
import poussecafe.files.Tree;
import poussecafe.source.analysis.ClassLoaderClassResolver;
import poussecafe.source.model.Aggregate;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public abstract class GenerationTest {

    @Before
    public void clearExistingCode() throws IOException {
        Tree.delete(sourceDirectory);
    }

    protected abstract void givenStorageGenerator();

    protected void whenGeneratingCode() {
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
        pathSegments[2] = "resources";
        System.arraycopy(packageSegments, 0, pathSegments, 3, packageSegments.length);
        return Tree.path(pathSegments);
    }

    private Aggregate aggregate;

    protected abstract String[] packageNameSegments();

    protected Aggregate aggregate() {
        return aggregate;
    }

    protected void givenCoreGenerator() {
        generator = new CoreCodeGenerator.Builder()
                .sourceDirectory(sourceDirectory())
                .codeFormatterProfile(getClass().getResourceAsStream("/CodeFormatterProfileSample.xml"))
                .classResolver(new ClassLoaderClassResolver())
                .build();
    }

    private CoreCodeGenerator generator;

    protected CoreCodeGenerator generator() {
        return generator;
    }

    protected abstract void whenGeneratingCoreCode();

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

    protected void givenExisingCode() {
        whenGeneratingCode();
    }
}
