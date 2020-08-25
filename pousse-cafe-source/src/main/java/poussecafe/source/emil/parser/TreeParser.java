package poussecafe.source.emil.parser;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ConsoleErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import poussecafe.source.emil.parser.EmilParser.ProcessContext;

public class TreeParser {

    public void parseString(String emil) {
        var stream = CharStreams.fromString(emil);
        parse(stream);
    }

    private void parse(CharStream stream) {
        EmilLexer lexer = new EmilLexer(stream);
        lexer.removeErrorListener(ConsoleErrorListener.INSTANCE);
        lexer.addErrorListener(new ErrorListener());

        if(errors.isEmpty()) {
            CommonTokenStream tokenStream = new CommonTokenStream(lexer);
            EmilParser parser = new EmilParser(tokenStream);
            parser.removeErrorListener(ConsoleErrorListener.INSTANCE);
            parser.addErrorListener(new ErrorListener());
            processContext = parser.process();
        }
    }

    private ProcessContext processContext;

    private class ErrorListener extends BaseErrorListener {

        @Override
        public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
            errors.add("line " + line + ":" + charPositionInLine + " " + msg);
        }
    }

    private List<String> errors = new ArrayList<>();

    public int consumptions() {
        return validProcessContext().consumptions().getChildCount();
    }

    private ProcessContext validProcessContext() {
        if(processContext == null) {
            throw new IllegalStateException("No AST available, parsing was either not executed, either failed");
        }
        return processContext;
    }

    public boolean success() {
        return errors.isEmpty();
    }

    public List<String> errors() {
        return Collections.unmodifiableList(errors);
    }

    public void parseInputStream(InputStream inputStream) throws IOException {
        var stream = CharStreams.fromStream(inputStream);
        parse(stream);
    }
}
