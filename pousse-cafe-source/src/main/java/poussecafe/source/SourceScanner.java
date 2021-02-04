package poussecafe.source;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashSet;
import java.util.Set;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import poussecafe.source.analysis.TypeResolvingCompilationUnitVisitor;

public class SourceScanner implements SourceConsumer {

    @Override
    public void includeFile(Path sourceFilePath) throws IOException {
        if(!sourceFilePath.toFile().isFile()) {
            throw new IllegalArgumentException(sourceFilePath + " does not point to a file");
        }
        includeSource(new PathSource(sourceFilePath));
    }

    @Override
    public void includeSource(Source source) {
        if(includedSources.contains(source.id())) {
            forget(source.id());
        }
        includedSources.add(source.id());
        ASTParser parser = ASTParser.newParser(AST.JLS14);
        source.configure(parser);
        var sourceId = source.id();
        CompilationUnit unit = (CompilationUnit) parser.createAST(null);
        if(unit.getMessages().length > 0) {
            logger.warn("Skipping {} because of compilation issues", sourceId);
            for(int i = 0; i < unit.getMessages().length; ++i) {
                Message message = unit.getMessages()[i];
                logger.warn("Line {}: {}", unit.getLineNumber(message.getStartPosition()), message.getMessage());
            }
        } else if(unit.types().size() != 1) {
            logger.debug("Skipping {} because it does not contain a single type", sourceId);
        } else {
            var sourceFile = new SourceFile.Builder()
                    .tree(unit)
                    .source(source)
                    .build();
            typeResolvingVisitor.visit(sourceFile);
            if(!typeResolvingVisitor.errors().isEmpty()) {
                logger.error("Visitor errors:");
                for(Exception e : typeResolvingVisitor.errors()) {
                    logger.error("-", e);
                }
                throw new IllegalStateException("Error while scanning source " + source.id());
            }
        }
    }

    public void forget(String sourceId) {
        typeResolvingVisitor.forget(sourceId);
    }

    @Override
    public boolean isIncluded(String sourceId) {
        return includedSources.contains(sourceId);
    }

    private Set<String> includedSources = new HashSet<>();

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void includeTree(Path sourceDirectory) throws IOException {
        Files.walkFileTree(sourceDirectory, new JavaSourceFileVisitor());
    }

    private class JavaSourceFileVisitor extends SimpleFileVisitor<Path> {

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
            if(file.toFile().isFile()
                    && file.toString().endsWith(".java")) {
                includeFile(file);
            }
            return FileVisitResult.CONTINUE;
        }
    }

    public SourceScanner(TypeResolvingCompilationUnitVisitor typeResolvingVisitor) {
        this.typeResolvingVisitor = typeResolvingVisitor;
    }

    private TypeResolvingCompilationUnitVisitor typeResolvingVisitor;
}
