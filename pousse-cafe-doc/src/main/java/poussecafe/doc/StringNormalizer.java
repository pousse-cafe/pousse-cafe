package poussecafe.doc;

public class StringNormalizer {

    public static String normalizeString(String string) {
        return string.toLowerCase().replaceAll("\\s+", "-");
    }
}
