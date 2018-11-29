package poussecafe.doc;

import com.sun.javadoc.Doc;
import com.sun.javadoc.MethodDoc;
import com.sun.javadoc.PackageDoc;
import com.sun.javadoc.Tag;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import poussecafe.exception.PousseCafeException;

public class AnnotationsResolver {

    public static boolean isIgnored(Doc doc) {
        return hasTag(doc, Tags.IGNORE);
    }

    private static boolean hasTag(Doc doc, String tag) {
        return doc.tags(tag).length > 0;
    }

    public static boolean isStep(MethodDoc methodDoc) {
        return hasTag(methodDoc, Tags.STEP);
    }

    public static List<String> step(MethodDoc methodDoc) {
        return tags(methodDoc, Tags.STEP);
    }

    private static Optional<String> optionalTag(Doc doc,
            String tagName) {
        Tag[] tags = doc.tags(tagName);
        if(tags.length > 1) {
            throw new IllegalArgumentException("Expected a single tag " + tagName + ", got " + tags.length);
        }
        if(tags.length == 0) {
            return Optional.empty();
        } else {
            return Optional.of(tags[0].text());
        }
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

    public static boolean isTrivial(Doc doc) {
        return hasTag(doc, Tags.TRIVIAL);
    }

    public static Optional<String> shortDescription(Doc doc) {
        return optionalTag(doc, Tags.SHORT);
    }

    public static List<String> event(MethodDoc methodDoc) {
        return tags(methodDoc, Tags.EVENT);
    }

    public static boolean isBoundedContext(PackageDoc packageDoc) {
        return hasTag(packageDoc, Tags.BOUNDED_CONTEXT);
    }

    public static String boundedContext(PackageDoc packageDoc) {
        return optionalTag(packageDoc, Tags.BOUNDED_CONTEXT).orElseThrow(PousseCafeException::new);
    }
}
