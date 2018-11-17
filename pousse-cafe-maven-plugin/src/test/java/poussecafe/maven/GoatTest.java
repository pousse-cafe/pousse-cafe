package poussecafe.maven;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.shared.invoker.DefaultInvocationRequest;
import org.apache.maven.shared.invoker.DefaultInvoker;
import org.apache.maven.shared.invoker.InvocationRequest;
import org.apache.maven.shared.invoker.InvocationResult;
import org.apache.maven.shared.invoker.Invoker;
import org.apache.maven.shared.invoker.MavenInvocationException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

public abstract class GoatTest {

    protected void givenProjectDirectory(File projectDirectory) throws IOException {
        this.projectDirectory = projectDirectory;
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

    protected File projectDirectory() {
        return projectDirectory;
    }

    private File sourceDirectory;

    protected File sourceDirectory() {
        return sourceDirectory;
    }

    protected void givenInvoker() {
        invoker = new DefaultInvoker();
    }

    private Invoker invoker;

    protected void whenExecutingGoals(List<String> goals) throws MojoExecutionException, MavenInvocationException {
        InvocationRequest request = new DefaultInvocationRequest();
        request.setBaseDirectory(projectDirectory);
        request.setGoals(goals);
        request.setBatchMode(true);
        result = invoker.execute(request);
    }

    private InvocationResult result;

    protected void thenInvokationSuccess(boolean success) {
        if(success) {
            assertThat(result.getExitCode(), is(0));
        } else {
            assertThat(result.getExitCode(), not(is(0)));
        }
    }
}
