package poussecafe.source.generation;

import java.io.InputStream;
import java.nio.file.Path;
import java.util.Map;
import org.eclipse.core.runtime.preferences.IScopeContext;
import poussecafe.source.analysis.ClassName;
import poussecafe.source.generation.tools.CodeFormatterOptionsBuilder;
import poussecafe.source.generation.tools.CompilationUnitEditor;

import static java.util.Collections.emptyMap;

public abstract class AbstractCodeGenerator {

    protected CompilationUnitEditor compilationUnitEditor(ClassName className) {
        return new CompilationUnitEditor.Builder()
                .sourceDirectory(sourceDirectory)
                .packageName(className.getQualifier().toString())
                .className(className.getIdentifier().toString())
                .formatterOptions(formatterOptions)
                .build();
    }

    protected Path sourceDirectory;

    protected Map<String, String> formatterOptions = emptyMap();

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

    protected void loadPreferencesFromContext(IScopeContext context) {
        var optionsBuilder = new CodeFormatterOptionsBuilder();
        optionsBuilder.withContext(context);
        formatterOptions = optionsBuilder.build();
    }
}
