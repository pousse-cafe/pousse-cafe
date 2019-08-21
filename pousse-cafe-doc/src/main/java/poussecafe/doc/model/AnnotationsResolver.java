package poussecafe.doc.model;

import com.sun.source.doctree.DocCommentTree;
import com.sun.source.doctree.DocTree;
import com.sun.source.doctree.UnknownBlockTagTree;
import java.util.List;
import java.util.Optional;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.PackageElement;
import jdk.javadoc.doclet.DocletEnvironment;
import poussecafe.doc.ProcessDescription;
import poussecafe.doc.TagContentStringBuilder;
import poussecafe.doc.Tags;
import poussecafe.domain.Service;
import poussecafe.exception.PousseCafeException;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

public class AnnotationsResolver implements Service {

    public boolean isIgnored(Element doc) {
        return hasTag(doc, Tags.IGNORE);
    }

    private boolean hasTag(Element doc, String tag) {
        return !tags(doc, tag).isEmpty();
    }

    private List<UnknownBlockTagTree> tags(Element doc, String tag) {
        DocCommentTree docCommentTree = docletEnvironment.getDocTrees().getDocCommentTree(doc);
        if(docCommentTree == null) {
            return emptyList();
        } else {
            return docCommentTree.getBlockTags().stream()
                .filter(blockTag -> blockTag instanceof UnknownBlockTagTree)
                .map(blockTag -> (UnknownBlockTagTree) blockTag)
                .filter(unknownBlockTag -> unknownBlockTag.getTagName().equals(tag))
                .collect(toList());
        }
    }

    private DocletEnvironment docletEnvironment;

    public boolean isStep(ExecutableElement methodDoc) {
        return hasTag(methodDoc, Tags.STEP);
    }

    public List<String> step(ExecutableElement methodDoc) {
        return tags(methodDoc, Tags.STEP).stream()
                .map(this::render)
                .collect(toList());
    }

    private String render(UnknownBlockTagTree tagTree) {
        return render(tagTree.getContent());
    }

    public String render(DocCommentTree docCommentTree) {
        return render(docCommentTree.getFullBody());
    }

    public String renderCommentBody(Element element) {
        DocCommentTree docCommentTree = docletEnvironment.getDocTrees().getDocCommentTree(element);
        if(docCommentTree == null) {
            return "";
        } else {
            return render(docCommentTree);
        }
    }

    private String render(List<? extends DocTree> tagTree) {
        TagContentStringBuilder renderer = new TagContentStringBuilder();
        StringBuilder builder = new StringBuilder();
        renderer.scan(tagTree, builder);
        return builder.toString();
    }

    private Optional<String> optionalTag(Element doc,
            String tagName) {
        List<UnknownBlockTagTree> tags = tags(doc, tagName);
        if(tags.size() > 1) {
            throw new IllegalArgumentException("Expected a single tag " + tagName + ", got " + tags.size());
        }
        if(tags.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(render(tags.get(0)));
        }
    }

    public List<String> toExternal(ExecutableElement methodDoc) {
        return tags(methodDoc, Tags.TO_EXTERNAL).stream()
                .map(this::render)
                .collect(toList());
    }

    public List<String> fromExternal(ExecutableElement methodDoc) {
        return tags(methodDoc, Tags.FROM_EXTERNAL).stream()
                .map(this::render)
                .collect(toList());
    }

    public boolean isTrivial(Element doc) {
        return hasTag(doc, Tags.TRIVIAL);
    }

    public Optional<String> shortDescription(Element doc) {
        return optionalTag(doc, Tags.SHORT);
    }

    public List<String> event(ExecutableElement methodDoc) {
        return tags(methodDoc, Tags.EVENT).stream()
                .map(this::render)
                .collect(toList());
    }

    public boolean isModule(PackageElement packageDoc) {
        return hasTag(packageDoc, Tags.MODULE);
    }

    public String module(PackageElement packageDoc) {
        return optionalTag(packageDoc, Tags.MODULE).orElseThrow(PousseCafeException::new);
    }

    public List<String> process(ExecutableElement methodDoc) {
        return tags(methodDoc, Tags.PROCESS).stream()
                .map(this::render)
                .collect(toList());
    }

    public List<ProcessDescription> processDescription(ExecutableElement doc) {
        return tags(doc, Tags.PROCESS_DESCRIPTION).stream()
                .map(this::render)
                .map(ProcessDescription::parse)
                .collect(toList());
    }
}
