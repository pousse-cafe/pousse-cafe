package poussecafe.doc;

import com.sun.javadoc.Doc;
import com.sun.javadoc.MethodDoc;
import com.sun.javadoc.Tag;
import java.util.ArrayList;
import java.util.List;

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

    public static boolean isStep(MethodDoc methodDoc) {
        return hasTag(methodDoc, Tags.STEP);
    }

    public static String step(MethodDoc methodDoc) {
        return singleTag(methodDoc, Tags.STEP);
    }

    private static String singleTag(Doc doc,
            String tagName) {
        Tag[] tags = doc.tags(tagName);
        if(tags.length != 1) {
            throw new IllegalArgumentException("Expected single tag " + tagName + ", got " + tags.length);
        }
        return tags[0].text();
    }

    public static List<String> to(MethodDoc methodDoc) {
        return tags(methodDoc, Tags.TO);
    }

    private static List<String> tags(Doc doc, String tagName) {
        Tag[] tags = doc.tags(tagName);
        List<String> values = new ArrayList<>();
        for(Tag tag : tags) {
            values.add(tag.text());
        }
        return values;
    }

    public static List<String> eventually(MethodDoc methodDoc) {
        return tags(methodDoc, Tags.EVENTUALLY);
    }

    public static List<String> toExternal(MethodDoc methodDoc) {
        return tags(methodDoc, Tags.TO_EXTERNAL);
    }

    public static List<String> fromExternal(MethodDoc methodDoc) {
        return tags(methodDoc, Tags.FROM_EXTERNAL);
    }
}
