package poussecafe.source;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.ASTParser;

public class PathSource implements Source {

    @Override
    public String id() {
        return path.toString();
    }

    private Path path;

    @Override
    public void configure(ASTParser parser) {
        parser.setSource(readAllChars().toCharArray());
        var options = JavaCore.getOptions(); // NOSONAR
        JavaCore.setComplianceOptions(JavaCore.VERSION_11, options);
        parser.setCompilerOptions(options);
    }

    private String readAllChars() {
        byte[] bytes;
        try {
            bytes = Files.readAllBytes(path);
        } catch (IOException e) {
            throw new IllegalArgumentException("Unable to read path content", e);
        }
        return new String(bytes);
    }

    public PathSource(Path path) {
        this.path = path;
    }
}
