package poussecafe.source;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import static java.util.Objects.requireNonNull;

class JavaSourceVisitor extends SimpleFileVisitor<Path> {

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        if(file.toString().endsWith(".java")) {
            scanner.includeFile(file);
        }
        return FileVisitResult.CONTINUE;
    }

    private Scanner scanner;

    public JavaSourceVisitor(Scanner scanner) {
        requireNonNull(scanner);
        this.scanner = scanner;
    }
}
