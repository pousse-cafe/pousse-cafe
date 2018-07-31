package poussecafe.doc;

public class Logger {

    public static void setRootDoc(RootDocWrapper rootDoc) {
        Logger.rootDoc = rootDoc;
    }

    private static RootDocWrapper rootDoc;

    public static void debug(String message) {
        rootDoc.debug("[DEBUG] " + message);
    }

    public static void warn(String message) {
        rootDoc.info("[WARNING] " + message);
    }

    public static void error(String message) {
        rootDoc.info("[ERROR] " + message);
    }

    public static void info(String message) {
        rootDoc.info("[INFO] " + message);
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
