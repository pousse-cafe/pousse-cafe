package poussecafe.source;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import poussecafe.source.model.Model;

public class Scanner {

    public Model model() {
        return model;
    }

    private Model model = new Model();

    public void includeTree(Path sourceDirectory) throws IOException {
        Files.walkFileTree(sourceDirectory, new JavaSourceFileVisitor(this));
    }

    public void includeFile(Path sourceFilePath) throws IOException {
        if(!sourceFilePath.toFile().isFile()) {
            throw new IllegalArgumentException(sourceFilePath + " does not point to a file");
        }

        ASTParser parser = ASTParser.newParser(AST.JLS14);
        parser.setSource(readAllChars(sourceFilePath));

        var options = JavaCore.getOptions(); // NOSONAR
        JavaCore.setComplianceOptions(JavaCore.VERSION_11, options);
        parser.setCompilerOptions(options);

        CompilationUnit unit = (CompilationUnit) parser.createAST(null);
        if(unit.getMessages().length > 0) {
            logger.warn("Skipping {} because of compilation issues", sourceFilePath);
            for(int i = 0; i < unit.getMessages().length; ++i) {
                Message message = unit.getMessages()[i];
                logger.warn("Line {}: {}", unit.getLineNumber(message.getStartPosition()), message.getMessage());
            }
        } else {
            unit.accept(compilationUnitVisitor(unit, sourceFilePath));
        }
    }

    private Logger logger = LoggerFactory.getLogger(getClass());

    private char[] readAllChars(Path sourceFilePath) throws IOException {
        byte[] bytes = Files.readAllBytes(sourceFilePath);
        String content = new String(bytes);
        return content.toCharArray();
    }

    private ASTVisitor compilationUnitVisitor(CompilationUnit unit, Path sourceFilePath) {
        return new CompilationUnitVisitor.Builder()
                .compilationUnit(unit)
                .sourcePath(sourceFilePath)
                .model(model)
                .build();
    }

    public static class Builder {

        private Scanner scanner = new Scanner();

        public Scanner build() {
            return scanner;
        }
    }

    private Scanner() {

    }
}