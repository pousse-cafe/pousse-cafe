package poussecafe.maven;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;
import poussecafe.doc.PousseCafeDocletConfiguration;
import poussecafe.doc.PousseCafeDocletExecutor;

import static java.util.Collections.emptyList;

@Mojo(
    name = "generate-doc",
    requiresDependencyResolution = ResolutionScope.COMPILE_PLUS_RUNTIME,
    requiresDependencyCollection = ResolutionScope.COMPILE_PLUS_RUNTIME
)
public class GenerateDocMojo extends AbstractMojo {

    @Override
    public void execute()
            throws MojoExecutionException,
            MojoFailureException {

        List<String> sourcePath = getSourcePath();
        List<String> classPath = getClassPath();

        PousseCafeDocletConfiguration configuration = new PousseCafeDocletConfiguration.Builder()
                .domainName(domainName)
                .version(version)
                .sourcePath(sourcePath)
                .outputDirectory(outputDirectory.getAbsolutePath())
                .basePackage(basePackage)
                .classPath(classPath)
                .build();

        new PousseCafeDocletExecutor(configuration).execute();
    }

    private List<String> getSourcePath() {
        List<String> sourcePath = new ArrayList<>();
        sourcePath.addAll(project.getCompileSourceRoots());
        sourcePath.addAll(sourceDependenciesFiles());
        return sourcePath;
    }

    private List<String> sourceDependenciesFiles() {
        List<String> sourceDependenciesFiles = new ArrayList<>();
        for(Artifact artifact : project.getArtifacts()) {
            if(artifact.hasClassifier() && artifact.getClassifier().equals("sources")) {
                sourceDependenciesFiles.add(artifact.getFile().getAbsolutePath());
            }
        }
        return sourceDependenciesFiles;
    }

    private List<String> getClassPath() {
        List<String> classPath;
        try {
            classPath = project.getCompileClasspathElements();
        } catch (DependencyResolutionRequiredException e) {
            classPath = emptyList();
        }
        return classPath;
    }

    @Parameter(property = "domainName", required = true)
    private String domainName;

    @Parameter(defaultValue = "${project.version}", property = "version", required = true)
    private String version;

    @Parameter(defaultValue = "${basedir}/target/ddd-doc/", property = "outputDirectory", required = true)
    private File outputDirectory;

    @Parameter(property = "basePackage", required = true)
    private String basePackage;

    @Parameter(defaultValue = "${project}", readonly = true, required = true)
    private MavenProject project;
}
