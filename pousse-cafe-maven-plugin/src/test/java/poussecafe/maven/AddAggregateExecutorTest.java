package poussecafe.maven;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collections;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.shared.invoker.DefaultInvocationRequest;
import org.apache.maven.shared.invoker.DefaultInvoker;
import org.apache.maven.shared.invoker.InvocationRequest;
import org.apache.maven.shared.invoker.InvocationResult;
import org.apache.maven.shared.invoker.Invoker;
import org.apache.maven.shared.invoker.MavenInvocationException;
import org.junit.Test;
import poussecafe.spring.mongo.storage.SpringMongoDbStorage;
import poussecafe.storage.internal.InternalStorage;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;
import static poussecafe.collection.Collections.asSet;

public class AddAggregateExecutorTest {

    @Test
    public void generatedFilesCompile() throws MojoExecutionException, IOException, MavenInvocationException {
        givenInvoker();
        givenSourceFolder();
        whenGeneratingSourceFiles();
        thenGeneratedFilesCompile();
    }

    private void givenSourceFolder() throws IOException {
        String buildDirectoryPath = System.getProperty("project.build.directory");
        projectDirectory = new File(buildDirectoryPath, "target/sourceFolder");
        sourceDirectory = new File(projectDirectory, "src/main/java");
        if(projectDirectory.exists()) {
            try {
                Files.walk(projectDirectory.toPath())
                    .sorted((path1, path2) -> -path1.compareTo(path2))
                    .forEach(path -> path.toFile().delete());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        sourceDirectory.mkdirs();
        Files.copy(getClass().getResourceAsStream("/pom_test.xml"), new File(projectDirectory, "pom.xml").toPath());
    }

    private File projectDirectory;

    private File sourceDirectory;

    private void givenInvoker() {
        invoker = new DefaultInvoker();
    }

    private Invoker invoker;

    private void whenGeneratingSourceFiles() throws MojoExecutionException, MavenInvocationException {
        AddAggregateExecutor executor = new AddAggregateExecutor.Builder()
                .sourceDirectory(sourceDirectory)
                .packageName("sample")
                .name("Sample")
                .storageAdapters(asSet(InternalStorage.NAME, SpringMongoDbStorage.NAME))
                .build();
        executor.execute();
    }

    private void thenGeneratedFilesCompile() throws IOException, MavenInvocationException {
        InvocationRequest request = new DefaultInvocationRequest();
        request.setBaseDirectory(projectDirectory);
        request.setGoals(Collections.singletonList("compile"));
        request.setBatchMode(true);
        result = invoker.execute(request);

        thenInvokationSuccess(true);
    }

    private InvocationResult result;

    private void thenInvokationSuccess(boolean success) {
        if(success) {
            assertThat(result.getExitCode(), is(0));
        } else {
            assertThat(result.getExitCode(), not(is(0)));
        }
    }
}
