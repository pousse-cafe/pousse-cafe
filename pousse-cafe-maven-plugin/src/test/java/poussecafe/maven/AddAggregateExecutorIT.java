package poussecafe.maven;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collections;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.shared.invoker.MavenInvocationException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static poussecafe.collection.Collections.asSet;

public class AddAggregateExecutorIT extends GoatTest {

    @Before
    public void setUp() {
        clearOldFiles();
    }

    @After
    public void tearDown() {
        clearOldFiles();
    }

    private void clearOldFiles() {
        File sourceDirectory = sourceDirectory();
        if(sourceDirectory.exists()) {
             try {
                 Files.walk(sourceDirectory.toPath())
                     .sorted((path1, path2) -> -path1.compareTo(path2))
                     .forEach(path -> path.toFile().delete());
             } catch (IOException e) {
                 e.printStackTrace();
             }
         }
        sourceDirectory.mkdirs();
    }

    private File sourceDirectory() {
        return new File(addAggregateTestProjectDirectory(), "src/main/java");
    }

    private File addAggregateTestProjectDirectory() {
        String buildDirectoryPath = System.getProperty("project.build.directory");
        File projectDirectory = new File(buildDirectoryPath, "src/test/resources/add_aggregate_test_project");
        return projectDirectory;
    }

    @Test
    public void generatedFilesCompile() throws MojoExecutionException, IOException, MavenInvocationException {
        givenInvoker();
        givenSourceFolder();
        whenGeneratingSourceFiles();
        thenGenerationSuccessful();
        thenGeneratedFilesCompile();
    }

    private void givenSourceFolder() throws IOException {
        givenProjectDirectory(addAggregateTestProjectDirectory());
    }

    private void whenGeneratingSourceFiles() throws MojoExecutionException, MavenInvocationException {
        whenExecutingMojo(new AddAggregateExecutor.Builder()
                .sourceDirectory(sourceDirectory())
                .packageName("sample")
                .name("Sample")
                .storageAdapters(asSet("internal", "spring-mongo"))
                .missingAdaptersOnly(false)
                .build());
    }

    private void thenGenerationSuccessful() {
        thenMojoExecutionSuccess(true);
    }

    private void thenGeneratedFilesCompile() throws MojoExecutionException, MavenInvocationException {
        whenExecutingMavenGoals(Collections.singletonList("compile"));
        thenMavenGoalsExecutionSuccess(true);
    }
}
