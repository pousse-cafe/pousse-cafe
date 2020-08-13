package poussecafe.source.generation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
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

import static java.util.Collections.singleton;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;

public class ModifiersEditor {

    public void setVisibility(Visibility visibility) {
        setModifier(
                keyword(visibility),
                VISIBILITY_KEYWORDS,
                visibility != Visibility.PACKAGE,
                this::visibilityPivot,
                this::visibilityInsertionModeProvider,
                DefaultInsertionMode.FIRST);
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

    private InsertionMode visibilityInsertionModeProvider(ASTNode pivot) {
        if(pivot instanceof Annotation) {
            return InsertionMode.AFTER;
        } else {
            return InsertionMode.BEFORE;
        }
    }

    public void setStatic(boolean enabled) {
        setModifier(
                ModifierKeyword.STATIC_KEYWORD,
                singleton(ModifierKeyword.STATIC_KEYWORD),
                enabled,
                this::staticPivot,
                this::staticInsertionModeProvider,
                DefaultInsertionMode.LAST);
    }

    private void setModifier(
            ModifierKeyword keyword,
            Set<ModifierKeyword> toReplace,
            boolean enabled,
            Supplier<Optional<ASTNode>> pivotProvider,
            Function<ASTNode, InsertionMode> pivotInsertionModeProvider,
            DefaultInsertionMode defaultInsertionMode) {
        List<Modifier> modifiers = findModifier(toReplace);
        if(enabled && modifiers.isEmpty()) {
            Optional<ASTNode> firstModifier = pivotProvider.get();
            var newModifier = rewrite.ast().newModifier(keyword);
            if(firstModifier.isPresent()) {
                var pivotInsertionMode = pivotInsertionModeProvider.apply(firstModifier.get());
                if(pivotInsertionMode == InsertionMode.AFTER) {
                    listRewrite().insertAfter(newModifier, firstModifier.get(), null);
                } else if(pivotInsertionMode == InsertionMode.BEFORE) {
                    listRewrite().insertBefore(newModifier, firstModifier.get(), null);
                }
            } else {
                if(defaultInsertionMode == DefaultInsertionMode.FIRST) {
                    listRewrite().insertFirst(newModifier, null);
                } else if(defaultInsertionMode == DefaultInsertionMode.LAST) {
                    listRewrite().insertLast(newModifier, null);
                }
            }
        } else if(!enabled && !modifiers.isEmpty()) {
            for(Modifier modifier : modifiers) {
                listRewrite().remove(modifier, null);
            }
        }
    }

    private enum InsertionMode {
        AFTER,
        BEFORE
    }

    private enum DefaultInsertionMode {
        FIRST,
        LAST
    }

    public List<Modifier> findModifier(Set<ModifierKeyword> keywords) {
        var modifiers = new ArrayList<Modifier>();
        for(Object modifierObject : modifiers()) {
            if(modifierObject instanceof Modifier) {
                Modifier modifier = (Modifier) modifierObject;
                if(keywords.contains(modifier.getKeyword())) {
                    modifiers.add(modifier);
                }
            }
        }
        return modifiers;
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
                if(VISIBILITY_MODIFIERS.contains(modifier.getKeyword().toFlagValue())) {
                    return Optional.of(modifier);
                }
            }
        }
        return Optional.empty();
    }

    private static final Set<Integer> VISIBILITY_MODIFIERS = visibilityModifiers();
    private static Set<Integer> visibilityModifiers() {
        var modifiers = new HashSet<Integer>();
        modifiers.add(Modifier.PRIVATE);
        modifiers.add(Modifier.PROTECTED);
        modifiers.add(Modifier.PUBLIC);
        return modifiers;
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

    private InsertionMode staticInsertionModeProvider(ASTNode pivot) {
        return InsertionMode.AFTER;
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

    public List<Modifier> findModifier(ModifierKeyword keyword) {
        var modifiers = new ArrayList<Modifier>();
        for(Object modifierObject : modifiers()) {
            if(modifierObject instanceof Modifier) {
                Modifier modifier = (Modifier) modifierObject;
                if(modifier.getKeyword().equals(keyword)) {
                    modifiers.add(modifier);
                }
            }
        }
        return modifiers;
    }

    @SuppressWarnings("rawtypes")
    private List modifiers() {
        return listRewrite().getRewrittenList();
    }

    private ListRewrite listRewrite() {
        return rewrite.listRewrite(property);
    }

    public List<NormalAnnotationEditor> normalAnnotation(Class<? extends java.lang.annotation.Annotation> annotationClass) {
        return annotations(annotationClass,
                this::newNormalAnnotation,
                this::normalAnnotationEditor,
                Annotation::isNormalAnnotation);
    }

    private <E extends AnnotationEditor> List<E> annotations(
            Class<? extends java.lang.annotation.Annotation> annotationClass,
            Function<Class<? extends java.lang.annotation.Annotation>, Annotation> factory,
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

    private NormalAnnotation newNormalAnnotation(Class<? extends java.lang.annotation.Annotation> annotationClass) {
        var newNormalAnnotation = rewrite.ast().newNormalAnnotation();
        newNormalAnnotation.setTypeName(rewrite.ast().newSimpleName(annotationClass.getSimpleName()));
        return newNormalAnnotation;
    }

    private NormalAnnotationEditor normalAnnotationEditor(Annotation a) {
        return new NormalAnnotationEditor(new NodeRewrite(rewrite.rewrite(), a));
    }

    private List<Annotation> findAnnotation(Class<? extends java.lang.annotation.Annotation> annotationClass) {
        var annotations = new ArrayList<Annotation>();
        for(Object annotationObject : modifiers()) {
            if(annotationObject instanceof Annotation) {
                Annotation annotation = (Annotation) annotationObject;
                Name annotationTypeName = new Name(annotation.getTypeName());
                if(annotationTypeName.getIdentifier().toString().equals(annotationClass.getSimpleName())) {
                    annotations.add(annotation);
                }
            }
        }
        return annotations;
    }

    public List<SingleMemberAnnotationEditor> singleMemberAnnotation(Class<? extends java.lang.annotation.Annotation> annotationClass) {
        return annotations(annotationClass,
                this::newSingleMemberAnnotation,
                this::singleMemberAnnotationEditor,
                Annotation::isSingleMemberAnnotation);
    }

    private SingleMemberAnnotation newSingleMemberAnnotation(Class<? extends java.lang.annotation.Annotation> annotationClass) {
        var newNormalAnnotation = rewrite.ast().newSingleMemberAnnotation();
        newNormalAnnotation.setTypeName(rewrite.ast().newSimpleName(annotationClass.getSimpleName()));
        return newNormalAnnotation;
    }

    private SingleMemberAnnotationEditor singleMemberAnnotationEditor(Annotation annotation) {
        return new SingleMemberAnnotationEditor(new NodeRewrite(rewrite.rewrite(), annotation));
    }

    public List<MarkerAnnotationEditor> markerAnnotation(Class<? extends java.lang.annotation.Annotation> annotationClass) {
        return annotations(annotationClass,
                this::newMarkerAnnotation,
                this::markerAnnotationEditor,
                Annotation::isMarkerAnnotation);
    }

    private MarkerAnnotation newMarkerAnnotation(Class<? extends java.lang.annotation.Annotation> annotationClass) {
        var newNormalAnnotation = rewrite.ast().newMarkerAnnotation();
        newNormalAnnotation.setTypeName(rewrite.ast().newSimpleName(annotationClass.getSimpleName()));
        return newNormalAnnotation;
    }

    private MarkerAnnotationEditor markerAnnotationEditor(Annotation annotation) {
        return new MarkerAnnotationEditor(new NodeRewrite(rewrite.rewrite(), annotation));
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
