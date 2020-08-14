package poussecafe.source.generation.tools;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
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
import poussecafe.source.analysis.Name;
import poussecafe.source.generation.tools.ModifierInsertor.DefaultInsertionMode;
import poussecafe.source.generation.tools.ModifierInsertor.InsertionMode;

import static java.util.Collections.singleton;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;

public class ModifiersEditor {

    public void setVisibility(Visibility visibility) {
        var visibilityInsertor = modifierInsertorBuilder()
                .toReplace(VISIBILITY_KEYWORDS)
                .pivotProvider(this::visibilityPivot)
                .pivotInsertionModeProvider(this::visibilityInsertionModeProvider)
                .defaultInsertionMode(DefaultInsertionMode.FIRST)
                .build();
        visibilityInsertor.set(keyword(visibility), visibility != Visibility.PACKAGE);
    }

    private ModifierInsertor.Builder modifierInsertorBuilder() {
        return new ModifierInsertor.Builder()
                .modifiers(modifiers())
                .listRewrite(listRewrite())
                .rewrite(rewrite);
    }

    private static final Set<ModifierKeyword> VISIBILITY_KEYWORDS = visibilityKeywords();
    private static Set<ModifierKeyword> visibilityKeywords() {
        var modifiers = new HashSet<ModifierKeyword>();
        modifiers.add(ModifierKeyword.PRIVATE_KEYWORD);
        modifiers.add(ModifierKeyword.PROTECTED_KEYWORD);
        modifiers.add(ModifierKeyword.PUBLIC_KEYWORD);
        return modifiers;
    }

    private Optional<ASTNode> visibilityPivot() {
        var annotations = annotations();
        if(annotations.isEmpty()) {
            return actualModifiers().stream().findFirst().map(modifier -> (ASTNode) modifier);
        } else {
            return Optional.of(annotations.get(annotations.size() - 1));
        }
    }

    @SuppressWarnings("unchecked")
    private List<Annotation> annotations() {
        return (List<Annotation>) modifiers().stream()
                .filter(this::isAnnotation)
                .collect(toList());
    }

    private boolean isAnnotation(Object node) {
        return node instanceof Annotation;
    }

    @SuppressWarnings("unchecked")
    private List<Modifier> actualModifiers() {
        return (List<Modifier>) modifiers().stream()
                .filter(this::isModifier)
                .collect(toList());
    }

    private boolean isModifier(Object node) {
        return node instanceof Modifier;
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
        var visibilityModifier = visibilityModifier();
        if(visibilityModifier.isPresent()) {
            return visibilityModifier;
        } else {
            var annotations = annotations();
            if(annotations.isEmpty()) {
                return Optional.empty();
            } else {
                return Optional.of(annotations.get(annotations.size() - 1));
            }
        }
    }

    private Optional<ASTNode> visibilityModifier() {
        for(Object modifierObject : modifiers()) {
            if(modifierObject instanceof Modifier) {
                Modifier modifier = (Modifier) modifierObject;
                if(VISIBILITY_KEYWORDS.contains(modifier.getKeyword())) {
                    return Optional.of(modifier);
                }
            }
        }
        return Optional.empty();
    }

    private InsertionMode staticInsertionModeProvider(ASTNode pivot) {
        return InsertionMode.AFTER;
    }

    @SuppressWarnings("rawtypes")
    private List modifiers() {
        return listRewrite().getRewrittenList();
    }

    private ListRewrite listRewrite() {
        return rewrite.listRewrite(property);
    }

    public List<NormalAnnotationEditor> normalAnnotation(Class<? extends java.lang.annotation.Annotation> annotationClass) {
        return annotations(new Name(annotationClass.getCanonicalName()),
                this::newNormalAnnotation,
                this::normalAnnotationEditor,
                Annotation::isNormalAnnotation);
    }

    private <E extends AnnotationEditor> List<E> annotations(
            Name annotationClass,
            Function<Name, Annotation> factory,
            Function<Annotation, E> editorFactory,
            Predicate<Annotation> isExpectedType) {
        var annotations = findAnnotation(annotationClass);
        if(annotations.isEmpty()) {
            Optional<Modifier> firstModifier = actualModifiers().stream().findFirst();

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

                var editor = editorFactory.apply(annotation);
                editors.add(editor);
            }
        }

        return editors;
    }

    private NormalAnnotation newNormalAnnotation(Name annotationClass) {
        var newNormalAnnotation = rewrite.ast().newNormalAnnotation();
        newNormalAnnotation.setTypeName(rewrite.ast().newSimpleName(annotationClass.getIdentifier().toString()));
        return newNormalAnnotation;
    }

    private NormalAnnotationEditor normalAnnotationEditor(Annotation a) {
        return new NormalAnnotationEditor(new NodeRewrite(rewrite.rewrite(), a));
    }

    private List<Annotation> findAnnotation(Name annotationClass) {
        var annotations = new ArrayList<Annotation>();
        for(Object annotationObject : modifiers()) {
            if(annotationObject instanceof Annotation) {
                Annotation annotation = (Annotation) annotationObject;
                Name annotationTypeName = new Name(annotation.getTypeName());
                if(annotationTypeName.getIdentifier().equals(annotationClass.getIdentifier())) {
                    annotations.add(annotation);
                }
            }
        }
        return annotations;
    }

    public List<SingleMemberAnnotationEditor> singleMemberAnnotation(Class<? extends java.lang.annotation.Annotation> annotationClass) {
        return singleMemberAnnotation(new Name(annotationClass.getCanonicalName()));
    }

    public List<SingleMemberAnnotationEditor> singleMemberAnnotation(Name annotationClass) {
        return annotations(annotationClass,
                this::newSingleMemberAnnotation,
                this::singleMemberAnnotationEditor,
                Annotation::isSingleMemberAnnotation);
    }

    private SingleMemberAnnotation newSingleMemberAnnotation(Name annotationClass) {
        var newNormalAnnotation = rewrite.ast().newSingleMemberAnnotation();
        newNormalAnnotation.setTypeName(rewrite.ast().newSimpleName(annotationClass.getIdentifier().toString()));
        return newNormalAnnotation;
    }

    private SingleMemberAnnotationEditor singleMemberAnnotationEditor(Annotation annotation) {
        return new SingleMemberAnnotationEditor(new NodeRewrite(rewrite.rewrite(), annotation));
    }

    public List<MarkerAnnotationEditor> markerAnnotation(Class<? extends java.lang.annotation.Annotation> annotationClass) {
        return markerAnnotation(new Name(annotationClass.getCanonicalName()));
    }

    public List<MarkerAnnotationEditor> markerAnnotation(Name annotationClass) {
        return annotations(annotationClass,
                this::newMarkerAnnotation,
                this::markerAnnotationEditor,
                Annotation::isMarkerAnnotation);
    }

    private MarkerAnnotation newMarkerAnnotation(Name annotationClass) {
        var newNormalAnnotation = rewrite.ast().newMarkerAnnotation();
        newNormalAnnotation.setTypeName(rewrite.ast().newSimpleName(annotationClass.getIdentifier().toString()));
        return newNormalAnnotation;
    }

    private MarkerAnnotationEditor markerAnnotationEditor(Annotation annotation) {
        return new MarkerAnnotationEditor(new NodeRewrite(rewrite.rewrite(), annotation));
    }

    public void removeAnnotations(Class<? extends java.lang.annotation.Annotation> annotationClass) {
        removeAnnotations(new Name(annotationClass.getCanonicalName()));
    }

    public void removeAnnotations(Name annotationClass) {
        var nodes = findAnnotation(annotationClass);
        ListRewrite listRewrite = listRewrite();
        nodes.forEach(node -> listRewrite.remove(node, null));
    }

    public ModifiersEditor(NodeRewrite rewrite, ChildListPropertyDescriptor property) {
        requireNonNull(rewrite);
        this.rewrite = rewrite;

        requireNonNull(property);
        this.property = property;
    }

    private NodeRewrite rewrite;

    private ChildListPropertyDescriptor property;
}
