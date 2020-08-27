package poussecafe.source.generation;

import java.nio.file.Path;
import poussecafe.source.analysis.Name;
import poussecafe.source.generation.tools.CompilationUnitEditor;

public abstract class AbstractCodeGenerator {

    protected CompilationUnitEditor compilationUnitEditor(Name className) {
        return new CompilationUnitEditor.Builder()
                .sourceDirectory(sourceDirectory)
                .packageName(className.getQualifier().toString())
                .className(className.getIdentifier().toString())
                .build();
    }

    protected Path sourceDirectory;
}
