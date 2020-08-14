package poussecafe.source.generation;

import java.nio.file.Path;
import poussecafe.source.analysis.Name;
import poussecafe.source.generation.tools.ComilationUnitEditor;

public abstract class AbstractCodeGenerator {

    protected ComilationUnitEditor compilationUnitEditor(Name className) {
        return new ComilationUnitEditor.Builder()
                .sourceDirectory(sourceDirectory)
                .packageName(className.getQualifier().toString())
                .className(className.getIdentifier().toString())
                .build();
    }

    protected Path sourceDirectory;
}
