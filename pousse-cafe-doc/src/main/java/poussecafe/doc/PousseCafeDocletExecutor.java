package poussecafe.doc;

import com.sun.tools.javadoc.Main;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.joining;

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

        Main.execute("javadoc",
                configuration.errorWriter(),
                configuration.warningWriter(),
                configuration.noticeWriter(),
                PousseCafeDoclet.class.getName(),
                javadocArgs.toArray(new String[javadocArgs.size()]));
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
        javadocArgs.add("-domain"); javadocArgs.add(configuration.domainName());
        javadocArgs.add("-version"); javadocArgs.add(configuration.version());
        javadocArgs.add("-basePackage"); javadocArgs.add(configuration.basePackage());
        javadocArgs.add("-includeGeneratedDate"); javadocArgs.add(Boolean.toString(configuration.includeGenerationDate()));
    }
}
