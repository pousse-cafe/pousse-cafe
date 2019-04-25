package poussecafe.doc;

import javax.tools.Diagnostic.Kind;
import jdk.javadoc.doclet.Reporter;

public class Logger {

    private Logger() {

    }

    public static void setRootDoc(Reporter rootDoc) {
        Logger.rootDoc = rootDoc;
    }

    private static Reporter rootDoc;

    public static void debug(String message) {
        rootDoc.print(Kind.OTHER, message);
    }

    public static void warn(String message) {
        rootDoc.print(Kind.WARNING, message);
    }

    public static void error(String message) {
        rootDoc.print(Kind.ERROR, message);
    }

    public static void info(String message) {
        rootDoc.print(Kind.NOTE, message);
    }

    public static void info(String message, Object...args) {
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
        info(builder.toString());
    }
}
