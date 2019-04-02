package poussecafe.doc;

public class SystemDependentInformation {

    private SystemDependentInformation() {

    }

    public static String pathElementSeparator() {
        if(isWindows()) {
            return ";";
        } else {
            return ":";
        }
    }

    private static boolean isWindows() {
        return OPERATING_SYSTEM_NAME.startsWith("Windows");
    }

    private static final String OPERATING_SYSTEM_NAME = System.getProperty("os.name");

    public static String pathElementSeparatorRegEx() {
        if(isWindows()) {
            return ";";
        } else {
            return "\\:";
        }
    }
}
