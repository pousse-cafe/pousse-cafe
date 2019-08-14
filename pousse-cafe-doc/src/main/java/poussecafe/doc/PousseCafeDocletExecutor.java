package poussecafe.doc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.tools.DocumentationTool;
import javax.tools.DocumentationTool.DocumentationTask;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.JavaFileObject.Kind;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;

import static java.util.stream.Collectors.joining;
import static poussecafe.collection.Collections.asSet;

public class PousseCafeDocletExecutor {

    public PousseCafeDocletExecutor(PousseCafeDocletConfiguration configuration) {
        Objects.requireNonNull(configuration);
        this.configuration = configuration;
    }

    private PousseCafeDocletConfiguration configuration;

    public void execute() {
        List<String> javadocArgs = new ArrayList<>();
        addStandardOptionsTo(javadocArgs);
        addDocletOptionsTo(javadocArgs);

        DocumentationTool documentationTool = ToolProvider.getSystemDocumentationTool();
        JavaFileManager fileManager = documentationTool.getStandardFileManager(null, null, null);
        try {
            Iterable<JavaFileObject> compilationUnits = fileManager.list(StandardLocation.SOURCE_PATH, configuration.basePackage(), asSet(Kind.SOURCE), true);
            DocumentationTask task = documentationTool.getTask(configuration.errorWriter(), fileManager, null, PousseCafeDoclet.class, javadocArgs, compilationUnits);
            task.call();
        } catch (IOException e) {
            throw new IllegalArgumentException("Unable to generate documentation", e);
        }
    }

    private void addStandardOptionsTo(List<String> javadocArgs) {
        String pathElementSeparator = SystemDependentInformation.pathElementSeparator();
        javadocArgs.add("-sourcepath"); javadocArgs.add(configuration.sourceDirectory().stream().collect(joining(pathElementSeparator)));
        javadocArgs.add("-subpackages"); javadocArgs.add(configuration.basePackage());
        if(!configuration.classPath().isEmpty()) {
            javadocArgs.add("-classpath"); javadocArgs.add(configuration.classPath().stream().collect(joining(pathElementSeparator)));
        }
    }

    private void addDocletOptionsTo(List<String> javadocArgs) {
        javadocArgs.add("-output"); javadocArgs.add(configuration.outputDirectory());
        javadocArgs.add("-pdfFile"); javadocArgs.add(configuration.pdfFileName());
        javadocArgs.add("-domain"); javadocArgs.add(configuration.domainName());
        javadocArgs.add("-version"); javadocArgs.add(configuration.version());
        javadocArgs.add("-basePackage"); javadocArgs.add(configuration.basePackage());
        if(configuration.includeGenerationDate()) {
            javadocArgs.add("-includeGeneratedDate");
        }
        if(configuration.customDotExecutable().isPresent()) {
            javadocArgs.add("-customDotExecutable"); javadocArgs.add(configuration.customDotExecutable().orElseThrow());
        }
        if(configuration.customFdpExecutable().isPresent()) {
            javadocArgs.add("-customFdpExecutable"); javadocArgs.add(configuration.customFdpExecutable().orElseThrow());
        }
    }
}
