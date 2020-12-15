package poussecafe.source;

import java.io.IOException;
import java.nio.file.Path;

public interface SourceConsumer {

    void includeFile(Path sourceFilePath) throws IOException;

    void includeTree(Path sourceDirectory) throws IOException;
}
