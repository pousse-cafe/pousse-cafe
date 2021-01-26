package poussecafe.source;

import java.io.IOException;
import java.lang.ref.WeakReference;
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
        parser.setSource(content().toCharArray());
        var options = JavaCore.getOptions(); // NOSONAR
        JavaCore.setComplianceOptions(JavaCore.VERSION_11, options);
        parser.setCompilerOptions(options);
    }

    @Override
    public String content() {
        if(content.get() == null) {
            content = new WeakReference<>(readAllChars());
        }
        return content.get();
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

    private WeakReference<String> content = new WeakReference<>(null);

    public PathSource(Path path) {
        this.path = path;
    }
}
