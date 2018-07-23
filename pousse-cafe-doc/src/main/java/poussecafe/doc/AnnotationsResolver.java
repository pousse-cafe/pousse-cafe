package poussecafe.doc;

import com.sun.javadoc.Doc;

public class AnnotationsResolver {

    public static boolean isIgnored(Doc doc) {
        return hasTag(doc, Tags.IGNORE);
    }

    private static boolean hasTag(Doc doc, String tag) {
        return doc.tags(tag).length > 0;
    }

    public static boolean isVo(Doc doc) {
        return hasTag(doc, Tags.VALUE_OBJECT);
    }
}
