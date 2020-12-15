package poussecafe.source;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import static java.util.Objects.requireNonNull;

class JavaSourceFileVisitor extends SimpleFileVisitor<Path> {

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        if(file.toFile().isFile()
                && file.toString().endsWith(".java")) {
            scanner.includeFile(file);
        }
        return FileVisitResult.CONTINUE;
    }

    private SourceScanner scanner;

    public JavaSourceFileVisitor(SourceScanner scanner) {
        requireNonNull(scanner);
        this.scanner = scanner;
    }
}
