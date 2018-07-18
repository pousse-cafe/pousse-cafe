package poussecafe.doc;

import com.sun.javadoc.Doc;
import com.sun.javadoc.PackageDoc;
import com.sun.javadoc.Tag;

public class DDDAnnotationsResolver {

    public static String getBoundedContext(PackageDoc clazz) {
        return getTagText(clazz, DDDTags.BOUNDED_CONTEXT);
    }

    private static String getTagText(Doc clazz, String tagName) {
        Tag[] tags = clazz.tags(tagName);
        if (tags != null && tags.length > 0) {
            return tags[0].text();
        }
        return null;
    }
}
