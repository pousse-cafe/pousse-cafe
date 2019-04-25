package poussecafe.doc.options;

import java.util.List;
import java.util.Objects;
import jdk.javadoc.doclet.Doclet.Option;
import poussecafe.doc.PousseCafeDocletConfiguration;
import poussecafe.doc.SystemDependentInformation;

import static java.util.Arrays.asList;

public class SourcePathOption implements Option {

    public SourcePathOption(PousseCafeDocletConfiguration.Builder configBuilder) {
        Objects.requireNonNull(configBuilder);
        this.configBuilder = configBuilder;
    }

    private PousseCafeDocletConfiguration.Builder configBuilder;

    @Override
    public int getArgumentCount() {
        return 1;
    }

    @Override
    public String getDescription() {
        return "Source path";
    }

    @Override
    public Kind getKind() {
        return Kind.STANDARD;
    }

    @Override
    public List<String> getNames() {
        return asList("-sourcepath");
    }

    @Override
    public String getParameters() {
        return "";
    }

    @Override
    public boolean process(String option,
            List<String> arguments) {
        configBuilder.sourcePath(asList(arguments.get(0).split(SystemDependentInformation.pathElementSeparatorRegEx())));
        return true;
    }

}
