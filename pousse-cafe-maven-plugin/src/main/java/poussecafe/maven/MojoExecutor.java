package poussecafe.maven;

import org.apache.maven.plugin.MojoExecutionException;

public interface MojoExecutor {

    void execute() throws MojoExecutionException;
}
