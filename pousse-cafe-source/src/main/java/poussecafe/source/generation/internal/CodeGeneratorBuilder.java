package poussecafe.source.generation.internal;

import java.io.InputStream;
import java.nio.file.Path;
import org.eclipse.core.runtime.preferences.IScopeContext;
import poussecafe.source.generation.AbstractCodeGenerator;

public interface CodeGeneratorBuilder {

    AbstractCodeGenerator build();

    CodeGeneratorBuilder sourceDirectory(Path sourceDirectory);

    CodeGeneratorBuilder codeFormatterProfile(Path profile);

    CodeGeneratorBuilder codeFormatterProfile(InputStream profile);

    CodeGeneratorBuilder preferencesContext(IScopeContext context);
}
