package poussecafe.source.generation;

import java.io.InputStream;
import java.nio.file.Path;
import java.util.Map;
import poussecafe.source.analysis.Name;
import poussecafe.source.generation.tools.CodeFormatterOptionsBuilder;
import poussecafe.source.generation.tools.CompilationUnitEditor;

public abstract class AbstractCodeGenerator {

    protected CompilationUnitEditor compilationUnitEditor(Name className) {
        return new CompilationUnitEditor.Builder()
                .sourceDirectory(sourceDirectory)
                .packageName(className.getQualifier().toString())
                .className(className.getIdentifier().toString())
                .formatterOptions(formatterOptions)
                .build();
    }

    protected Path sourceDirectory;

    protected Map<String, String> formatterOptions;

    protected void loadProfileFromFile(Path profilesFile) {
        var optionsBuilder = new CodeFormatterOptionsBuilder();
        optionsBuilder.withProfile(profilesFile);
        formatterOptions = optionsBuilder.build();
    }

    protected void loadProfileFromFile(InputStream profilesStream) {
        var optionsBuilder = new CodeFormatterOptionsBuilder();
        optionsBuilder.withProfile(profilesStream);
        formatterOptions = optionsBuilder.build();
    }
}
