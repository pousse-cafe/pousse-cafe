package poussecafe.source;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import poussecafe.source.analysis.ResolutionException;

import static java.util.Objects.requireNonNull;

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
            try {
                fileVisitor.visitFile(new SourceFile.Builder()
                        .id(sourceId)
                        .tree(unit)
                        .build());
            } catch (ResolutionException e) {
                logger.error("Unable to analyze file {}, try to compile the project first", sourceId);
                throw e;
            }
        }
    }

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void includeTree(Path sourceDirectory) throws IOException {
        Files.walkFileTree(sourceDirectory, new JavaSourceFileVisitor(this));
    }

    public SourceScanner(SourceFileVisitor fileVisitor) {
        requireNonNull(fileVisitor);
        this.fileVisitor = fileVisitor;
    }

    private SourceFileVisitor fileVisitor;
}
