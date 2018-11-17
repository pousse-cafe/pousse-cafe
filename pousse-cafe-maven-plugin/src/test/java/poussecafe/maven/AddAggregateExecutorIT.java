package poussecafe.maven;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.shared.invoker.MavenInvocationException;
import org.junit.Test;

public class AddAggregateExecutorIT extends GoatTest {

    @Test
    public void generatedFilesCompile() throws MojoExecutionException, IOException, MavenInvocationException {
        givenInvoker();
        givenSourceFolder();
        whenGeneratingSourceFiles();
        thenGenerationSuccessful();
        thenGeneratedFilesCompile();
    }

    private void givenSourceFolder() throws IOException {
        String buildDirectoryPath = System.getProperty("project.build.directory");
        File projectDirectory = new File(buildDirectoryPath, "target/sourceFolder");
        givenProjectDirectory(projectDirectory);
    }

    private void whenGeneratingSourceFiles() throws MojoExecutionException, MavenInvocationException {
        whenExecutingGoals(Collections.singletonList("pousse-cafe:add-aggregate"));
    }

    private void thenGenerationSuccessful() {
        thenInvokationSuccess(true);
    }

    private void thenGeneratedFilesCompile() throws MojoExecutionException, MavenInvocationException {
        whenExecutingGoals(Collections.singletonList("compile"));
        thenInvokationSuccess(true);
    }
}
