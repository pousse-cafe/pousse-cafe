package poussecafe.doc;

public class Logger {

    public static void setRootDoc(RootDocWrapper rootDoc) {
        Logger.rootDoc = rootDoc;
    }

    private static RootDocWrapper rootDoc;

    public static void debug(String message) {
        rootDoc.debug(message);
    }
}
