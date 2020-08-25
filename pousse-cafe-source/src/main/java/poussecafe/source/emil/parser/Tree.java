package poussecafe.source.emil.parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import poussecafe.source.emil.parser.EmilParser.ProcessContext;

public class Tree {

    private List<String> errors = new ArrayList<>();

    public List<String> errors() {
        return Collections.unmodifiableList(errors);
    }

    private ProcessContext processContext;

    public ProcessContext processContext() {
        if(!isValid()) {
            throw new IllegalStateException("Parse failed, see errors");
        }
        return processContext;
    }

    public boolean isValid() {
        return processContext != null && errors.isEmpty();
    }

    public static class Builder extends BaseErrorListener {

        private Tree tree = new Tree();

        public Tree build() {
            return tree;
        }

        @Override
        public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
            tree.errors.add("line " + line + ":" + charPositionInLine + " " + msg);
        }

        public Builder processContext(ProcessContext processContext) {
            tree.processContext = processContext;
            return this;
        }

        public boolean hasErrors() {
            return !tree.errors.isEmpty();
        }
    }

    private Tree() {

    }
}
