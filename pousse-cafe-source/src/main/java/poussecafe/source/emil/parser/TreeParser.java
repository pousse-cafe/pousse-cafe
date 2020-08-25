package poussecafe.source.emil.parser;

import java.io.IOException;
import java.io.InputStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ConsoleErrorListener;

public class TreeParser {

    public static Tree parseString(String emil) {
        var stream = CharStreams.fromString(emil);
        return parse(stream);
    }

    private static Tree parse(CharStream stream) {
        var builder = new Tree.Builder();
        EmilLexer lexer = new EmilLexer(stream);
        lexer.removeErrorListener(ConsoleErrorListener.INSTANCE);
        lexer.addErrorListener(builder);

        if(!builder.hasErrors()) {
            CommonTokenStream tokenStream = new CommonTokenStream(lexer);
            EmilParser parser = new EmilParser(tokenStream);
            parser.removeErrorListener(ConsoleErrorListener.INSTANCE);
            parser.addErrorListener(builder);
            builder.processContext(parser.process());
        }

        return builder.build();
    }

    public static Tree parseInputStream(InputStream inputStream) throws IOException {
        var stream = CharStreams.fromStream(inputStream);
        return parse(stream);
    }

    private TreeParser() {

    }
}
