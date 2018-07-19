package poussecafe.doc;

import com.sun.javadoc.Doc;

public class AnnotationsResolver {

    public static boolean isIgnored(Doc clazz) {
        return clazz.tags(Tags.IGNORE).length > 0;
    }
}
