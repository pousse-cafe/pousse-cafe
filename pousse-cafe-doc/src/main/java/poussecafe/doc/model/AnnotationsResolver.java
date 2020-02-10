package poussecafe.doc.model;

import com.sun.source.doctree.DocCommentTree;
import com.sun.source.doctree.DocTree;
import com.sun.source.doctree.UnknownBlockTagTree;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.PackageElement;
import jdk.javadoc.doclet.DocletEnvironment;
import poussecafe.discovery.MessageListener;
import poussecafe.doc.Logger;
import poussecafe.doc.ProcessDescription;
import poussecafe.doc.TagContentStringBuilder;
import poussecafe.doc.Tags;
import poussecafe.doc.annotations.AnnotationUtils;
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
        return hasTag(methodDoc, Tags.STEP) ||
                isAnnotatedWith(methodDoc, MessageListener.class);
    }

    private boolean isAnnotatedWith(ExecutableElement methodDoc,
            Class<MessageListener> annotationClass) {
        return !getAnnotations(methodDoc, annotationClass).isEmpty();
    }

    private <A extends Annotation> List<AnnotationMirror> getAnnotations(ExecutableElement methodDoc,
            Class<A> annotationClass) {
        return AnnotationUtils.annotations(methodDoc, annotationClass);
    }

    public List<String> step(ExecutableElement methodDoc) {
        List<String> customSteps = new ArrayList<>();
        List<String> customStepsByTag = tags(methodDoc, Tags.STEP).stream()
                                 .map(this::render)
                                 .collect(toList());
        if(!customStepsByTag.isEmpty()) {
            Logger.warn("@step tag is deprecated, use @MessageListener annotation and set customStep instead");
            customSteps.addAll(customStepsByTag);
        }
        customSteps.addAll(getAnnotations(methodDoc, MessageListener.class).stream()
                .map(mirror -> AnnotationUtils.value(mirror, "customStep"))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(value -> (String) value.getValue())
                .filter(customStep -> !customStep.isEmpty())
                .collect(toList()));
        return customSteps;
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

    @Deprecated(since = "0.16")
    public List<String> toExternal(ExecutableElement methodDoc) {
        return tags(methodDoc, Tags.TO_EXTERNAL).stream()
                .map(this::render)
                .collect(toList());
    }

    @Deprecated(since = "0.17")
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

    @Deprecated(since = "0.16")
    public List<String> event(ExecutableElement methodDoc) {
        return tags(methodDoc, Tags.EVENT).stream()
                .map(this::render)
                .collect(toList());
    }

    @Deprecated(since = "0.17")
    public boolean isModule(PackageElement packageDoc) {
        return hasTag(packageDoc, Tags.MODULE);
    }

    @Deprecated(since = "0.17")
    public String module(PackageElement packageDoc) {
        return optionalTag(packageDoc, Tags.MODULE).orElseThrow(PousseCafeException::new);
    }

    @Deprecated(since = "0.17")
    public List<String> process(ExecutableElement methodDoc) {
        return tags(methodDoc, Tags.PROCESS).stream()
                .map(this::render)
                .collect(toList());
    }

    @Deprecated(since = "0.17")
    public List<ProcessDescription> processDescription(ExecutableElement doc) {
        return tags(doc, Tags.PROCESS_DESCRIPTION).stream()
                .map(this::render)
                .map(ProcessDescription::parse)
                .collect(toList());
    }
}
