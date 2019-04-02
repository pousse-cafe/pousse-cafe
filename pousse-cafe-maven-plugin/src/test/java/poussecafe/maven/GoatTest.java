package poussecafe.maven;

import java.io.File;
import java.io.IOException;
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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

public abstract class GoatTest {

    protected void givenProjectDirectory(File projectDirectory) throws IOException {
        this.projectDirectory = projectDirectory;
        pomFile = new File(projectDirectory, "pom.xml");
    }

    private File projectDirectory;

    protected File projectDirectory() {
        return projectDirectory;
    }

    private File pomFile;

    protected File pomFile() {
        return pomFile;
    }

    protected void givenInvoker() {
        invoker = new DefaultInvoker();
    }

    private Invoker invoker;

    protected void whenExecutingMavenGoals(List<String> goals) throws MojoExecutionException, MavenInvocationException {
        InvocationRequest request = new DefaultInvocationRequest();
        request.setBaseDirectory(projectDirectory);
        request.setGoals(goals);
        request.setBatchMode(true);
        result = invoker.execute(request);
    }

    private InvocationResult result;

    protected void thenMavenGoalsExecutionSuccess(boolean success) {
        if(success) {
            assertThat(result.getExitCode(), is(0));
        } else {
            assertThat(result.getExitCode(), not(is(0)));
        }
    }

    protected void whenExecutingMojo(MojoExecutor executor) {
        try {
            mojoExecutionException = null;
            executor.execute();
        } catch (MojoExecutionException e) {
            mojoExecutionException = e;
        }
    }

    private MojoExecutionException mojoExecutionException;

    protected void thenMojoExecutionSuccess(boolean success) {
        if(success) {
            if(mojoExecutionException != null) {
                mojoExecutionException.printStackTrace();
            }
            assertNull(mojoExecutionException);
        } else {
            assertNotNull(mojoExecutionException);
        }
    }
}
