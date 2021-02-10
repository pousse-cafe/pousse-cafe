package poussecafe.source.generation.tools;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.ChildListPropertyDescriptor;
import org.eclipse.jdt.core.dom.MarkerAnnotation;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.Modifier.ModifierKeyword;
import org.eclipse.jdt.core.dom.NormalAnnotation;
import org.eclipse.jdt.core.dom.SingleMemberAnnotation;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;
import poussecafe.source.analysis.Modifiers;
import poussecafe.source.analysis.ClassName;
import poussecafe.source.analysis.Visibility;

import static java.util.Collections.singleton;
import static java.util.Objects.requireNonNull;

public class ModifiersEditor {

    public void setVisibility(Visibility visibility) {
        var visibilityInsertor = modifierInsertorBuilder()
                .toReplace(Modifiers.VISIBILITY_KEYWORDS)
                .pivotProvider(this::visibilityPivot)
                .pivotInsertionModeProvider(this::visibilityInsertionModeProvider)
                .defaultInsertionMode(DefaultInsertionMode.FIRST)
                .build();
        visibilityInsertor.set(keyword(visibility), visibility != Visibility.PACKAGE);
    }

    private ModifierInsertor.Builder modifierInsertorBuilder() {
        return new ModifierInsertor.Builder()
                .listRewrite(listRewrite())
                .rewrite(rewrite);
    }

    private Optional<ASTNode> visibilityPivot() {
        var annotations = modifiers.annotations();
        if(annotations.isEmpty()) {
            return modifiers.actualModifiers().stream().findFirst().map(modifier -> (ASTNode) modifier);
        } else {
            return Optional.of(annotations.get(annotations.size() - 1));
        }
    }

    private InsertionMode visibilityInsertionModeProvider(ASTNode pivot) {
        if(pivot instanceof Annotation) {
            return InsertionMode.AFTER;
        } else {
            return InsertionMode.BEFORE;
        }
    }

    private ModifierKeyword keyword(Visibility visibility) {
        if(visibility == Visibility.PRIVATE) {
            return ModifierKeyword.PRIVATE_KEYWORD;
        } else if(visibility == Visibility.PROTECTED) {
            return ModifierKeyword.PROTECTED_KEYWORD;
        } else if(visibility == Visibility.PUBLIC) {
            return ModifierKeyword.PUBLIC_KEYWORD;
        } else {
            return null;
        }
    }

    public void setStatic(boolean enabled) {
        var staticInsertor = modifierInsertorBuilder()
                .toReplace(singleton(ModifierKeyword.STATIC_KEYWORD))
                .pivotProvider(this::staticPivot)
                .pivotInsertionModeProvider(this::staticInsertionModeProvider)
                .defaultInsertionMode(DefaultInsertionMode.LAST)
                .build();
        staticInsertor.set(ModifierKeyword.STATIC_KEYWORD, enabled);
    }

    private Optional<ASTNode> staticPivot() {
        var visibilityModifier = modifiers.visibilityModifier();
        if(visibilityModifier.isPresent()) {
            return visibilityModifier.map(modifier -> (ASTNode) modifier);
        } else {
            var annotations = modifiers.annotations();
            if(annotations.isEmpty()) {
                return Optional.empty();
            } else {
                return Optional.of(annotations.get(annotations.size() - 1));
            }
        }
    }

    private InsertionMode staticInsertionModeProvider(ASTNode pivot) {
        return InsertionMode.AFTER;
    }

    private Modifiers modifiers;

    private ListRewrite listRewrite() {
        return rewrite.listRewrite(property);
    }

    public List<NormalAnnotationEditor> normalAnnotation(Class<? extends java.lang.annotation.Annotation> annotationClass) {
        return annotations(new ClassName(annotationClass.getCanonicalName()),
                this::newNormalAnnotation,
                this::normalAnnotationEditor,
                Annotation::isNormalAnnotation);
    }

    private <E extends AnnotationEditor> List<E> annotations(
            ClassName annotationClass,
            Function<ClassName, Annotation> factory,
            Function<Annotation, E> editorFactory,
            Predicate<Annotation> isExpectedType) {
        var annotations = modifiers.findUnresolvedAnnotationsByIdentifier(annotationClass);
        if(annotations.isEmpty()) {
            Optional<Modifier> firstModifier = modifiers.actualModifiers().stream().findFirst();

            var newNormalAnnotation = factory.apply(annotationClass);
            if(firstModifier.isPresent()) {
                listRewrite().insertBefore(newNormalAnnotation, firstModifier.get(), null);
            } else {
                listRewrite().insertFirst(newNormalAnnotation, null);
            }
            annotations.add(newNormalAnnotation);
        }

        var editors = new ArrayList<E>();
        for(Annotation annotation : annotations) {
            if(isExpectedType.test(annotation)) {
                var editor = editorFactory.apply(annotation);
                editors.add(editor);
            } else {
                var newAnnotation = factory.apply(annotationClass);
                rewrite.rewrite().replace(annotation, newAnnotation, null);

                var editor = editorFactory.apply(newAnnotation);
                editors.add(editor);
            }
        }

        return editors;
    }

    public NormalAnnotationEditor insertNewNormalAnnotationFirst(ClassName annotationClass) {
        var newNormalAnnotation = newNormalAnnotation(annotationClass);
        listRewrite().insertFirst(newNormalAnnotation, null);
        return normalAnnotationEditor(newNormalAnnotation);
    }

    public NormalAnnotationEditor insertNewNormalAnnotationLast(ClassName annotationClass) {
        var newNormalAnnotation = newNormalAnnotation(annotationClass);
        insertLast(newNormalAnnotation);
        return normalAnnotationEditor(newNormalAnnotation);
    }

    private void insertLast(Annotation annotation) {
        var annotations = modifiers.annotations();
        if(annotations.isEmpty()) {
            var actualModifiers = modifiers.actualModifiers();
            if(actualModifiers.isEmpty()) {
                listRewrite().insertFirst(annotation, null);
            } else {
                listRewrite().insertBefore(annotation, actualModifiers.get(0), null);
            }
        } else {
            listRewrite().insertAfter(annotation, annotations.get(annotations.size() -1), null);
        }
    }

    private NormalAnnotation newNormalAnnotation(ClassName annotationClass) {
        var newNormalAnnotation = rewrite.ast().newNormalAnnotation();
        newNormalAnnotation.setTypeName(rewrite.ast().newSimpleName(annotationClass.getIdentifier().toString()));
        return newNormalAnnotation;
    }

    public NormalAnnotationEditor normalAnnotationEditor(Annotation annotation) {
        if(!(annotation instanceof NormalAnnotation)) {
            throw unexpectedAnnotation();
        }
        return new NormalAnnotationEditor(new NodeRewrite(rewrite.rewrite(), annotation));
    }

    private IllegalArgumentException unexpectedAnnotation() {
        return new IllegalArgumentException("Unexpected annotation type");
    }

    public List<Annotation> findAnnotations(Class<? extends java.lang.annotation.Annotation> annotationClass) {
        return modifiers.findUnresolvedAnnotationsByIdentifier(new ClassName(annotationClass.getCanonicalName()));
    }

    public boolean hasAnnotation(Class<? extends java.lang.annotation.Annotation> annotationClass) {
        return !findAnnotations(annotationClass).isEmpty();
    }

    public List<SingleMemberAnnotationEditor> singleMemberAnnotation(Class<? extends java.lang.annotation.Annotation> annotationClass) {
        return singleMemberAnnotation(new ClassName(annotationClass.getCanonicalName()));
    }

    public List<SingleMemberAnnotationEditor> singleMemberAnnotation(ClassName annotationClass) {
        return annotations(annotationClass,
                this::newSingleMemberAnnotation,
                this::singleMemberAnnotationEditor,
                Annotation::isSingleMemberAnnotation);
    }

    private SingleMemberAnnotation newSingleMemberAnnotation(ClassName annotationClass) {
        var newNormalAnnotation = rewrite.ast().newSingleMemberAnnotation();
        newNormalAnnotation.setTypeName(rewrite.ast().newSimpleName(annotationClass.getIdentifier().toString()));
        return newNormalAnnotation;
    }

    public SingleMemberAnnotationEditor singleMemberAnnotationEditor(Annotation annotation) {
        if(!(annotation instanceof SingleMemberAnnotation)) {
            throw unexpectedAnnotation();
        }
        return new SingleMemberAnnotationEditor(new NodeRewrite(rewrite.rewrite(), annotation));
    }

    public List<MarkerAnnotationEditor> markerAnnotation(Class<? extends java.lang.annotation.Annotation> annotationClass) {
        return markerAnnotation(new ClassName(annotationClass.getCanonicalName()));
    }

    public List<MarkerAnnotationEditor> markerAnnotation(ClassName annotationClass) {
        return annotations(annotationClass,
                this::newMarkerAnnotation,
                this::markerAnnotationEditor,
                Annotation::isMarkerAnnotation);
    }

    private MarkerAnnotation newMarkerAnnotation(ClassName annotationClass) {
        var newNormalAnnotation = rewrite.ast().newMarkerAnnotation();
        newNormalAnnotation.setTypeName(rewrite.ast().newSimpleName(annotationClass.getIdentifier().toString()));
        return newNormalAnnotation;
    }

    public MarkerAnnotationEditor markerAnnotationEditor(Annotation annotation) {
        if(!(annotation instanceof MarkerAnnotation)) {
            throw unexpectedAnnotation();
        }
        return new MarkerAnnotationEditor(new NodeRewrite(rewrite.rewrite(), annotation));
    }

    public void removeAnnotations(Class<? extends java.lang.annotation.Annotation> annotationClass) {
        removeAnnotations(new ClassName(annotationClass.getCanonicalName()));
    }

    public void removeAnnotations(ClassName annotationClass) {
        var nodes = modifiers.findUnresolvedAnnotationsByIdentifier(annotationClass);
        ListRewrite listRewrite = listRewrite();
        nodes.forEach(node -> listRewrite.remove(node, null));
    }

    public void removeAnnotation(Annotation annotation) {
        ListRewrite listRewrite = listRewrite();
        listRewrite.remove(annotation, null);
    }

    public SingleMemberAnnotationEditor insertNewSingleMemberAnnotationLast(ClassName annotationClass) {
        var newAnnotation = newSingleMemberAnnotation(annotationClass);
        insertLast(newAnnotation);
        return singleMemberAnnotationEditor(newAnnotation);
    }

    public ModifiersEditor(NodeRewrite rewrite, ChildListPropertyDescriptor property) {
        requireNonNull(rewrite);
        this.rewrite = rewrite;

        requireNonNull(property);
        this.property = property;

        modifiers = new Modifiers.Builder().modifiers(listRewrite().getRewrittenList()).build();
    }

    private NodeRewrite rewrite;

    private ChildListPropertyDescriptor property;
}
