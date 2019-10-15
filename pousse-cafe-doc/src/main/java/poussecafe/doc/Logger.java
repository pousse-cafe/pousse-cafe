package poussecafe.doc;

import java.util.function.Consumer;
import javax.tools.Diagnostic.Kind;
import jdk.javadoc.doclet.Reporter;

public class Logger {

    private Logger() {

    }

    public static void setRootDoc(Reporter rootDoc) {
        Logger.rootDoc = rootDoc;
    }

    private static Reporter rootDoc;

    public static void debug(String message, Object... args) {
        log(message, text -> rootDoc.print(Kind.OTHER, text), args);
    }

    public static void warn(String message, Object... args) {
        log(message, text -> rootDoc.print(Kind.WARNING, text), args);
    }

    public static void error(String message, Object... args) {
        log(message, text -> rootDoc.print(Kind.ERROR, text), args);
    }

    public static void info(String message, Object...args) {
        log(message, text -> rootDoc.print(Kind.NOTE, text), args);
    }

    private static void log(String message, Consumer<String> logger, Object...args) {
        String[] parts = message.split("\\{\\}");
        StringBuilder builder = new StringBuilder();
        int minPartsArgs = Math.min(args.length, parts.length);
        for(int i = 0; i < minPartsArgs; ++i) {
            builder.append(parts[i]);
            builder.append(args[i]);
        }
        for(int i = args.length; i < parts.length; ++i) {
            builder.append(parts[i]);
        }
        logger.accept(builder.toString());
    }
}
