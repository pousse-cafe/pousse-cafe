package poussecafe.source.analysis;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.Modifier.ModifierKeyword;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;

@SuppressWarnings("rawtypes")
public class Modifiers {

    public Optional<ResolvedAnnotation> findAnnotation(String annotationClassName) {
        return findAnnotations(annotationClassName).stream().findFirst();
    }

    public List<ResolvedAnnotation> findAnnotations(String annotationClass) {
        return annotations().stream()
                .map(this::resolveAnnotation)
                .filter(annotation -> annotation.isClass(annotationClass))
                .collect(toList());
    }

    @SuppressWarnings("unchecked")
    public List<Annotation> annotations() {
        return (List<Annotation>) modifiersList.stream()
                .filter(this::isAnnotation)
                .collect(toList());
    }

    private List modifiersList;

    private boolean isAnnotation(Object node) {
        return node instanceof Annotation;
    }

    private ResolvedAnnotation resolveAnnotation(Annotation annotation) {
        return new ResolvedAnnotation.Builder()
                .annotation(annotation)
                .resolver(resolver)
                .build();
    }

    private Resolver resolver;

    public boolean isAbstract() {
        for(Modifier modifier : actualModifiers()) {
            if(modifier.isAbstract()) {
                return true;
            }
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    public List<Modifier> actualModifiers() {
        return (List<Modifier>) modifiersList.stream()
                .filter(this::isModifier)
                .collect(toList());
    }

    private boolean isModifier(Object node) {
        return node instanceof Modifier;
    }

    public boolean hasVisibility(Visibility visibility) {
        if(visibility == Visibility.PACKAGE) {
            return actualModifiers().stream()
                    .noneMatch(Modifiers::isVisibilityKeyword);
        } else {
            var keyword = keyword(visibility);
            return actualModifiers().stream()
                    .anyMatch(modifier -> modifier.getKeyword().equals(keyword));
        }
    }

    public static boolean isVisibilityKeyword(Modifier modifier) {
        return VISIBILITY_KEYWORDS.contains(modifier.getKeyword());
    }

    public static final Set<ModifierKeyword> VISIBILITY_KEYWORDS = visibilityKeywords(); // NOSONAR - IS immutable
    private static Set<ModifierKeyword> visibilityKeywords() {
        var modifiers = new HashSet<ModifierKeyword>();
        modifiers.add(ModifierKeyword.PRIVATE_KEYWORD);
        modifiers.add(ModifierKeyword.PROTECTED_KEYWORD);
        modifiers.add(ModifierKeyword.PUBLIC_KEYWORD);
        return Collections.unmodifiableSet(modifiers);
    }

    public static ModifierKeyword keyword(Visibility visibility) {
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

    public Optional<Modifier> visibilityModifier() {
        for(Object modifierObject : modifiersList) {
            if(modifierObject instanceof Modifier) {
                Modifier modifier = (Modifier) modifierObject;
                if(Modifiers.isVisibilityKeyword(modifier)) {
                    return Optional.of(modifier);
                }
            }
        }
        return Optional.empty();
    }

    public List<Annotation> findUnresolvedAnnotationsByIdentifier(ClassName annotationClass) {
        var annotations = new ArrayList<Annotation>();
        for(Annotation annotation : annotations()) {
            ClassName annotationTypeName = new ClassName(annotation.getTypeName());
            if(annotationTypeName.getIdentifier().equals(annotationClass.getIdentifier())) {
                annotations.add(annotation);
            }
        }
        return annotations;
    }

    public static class Builder {

        public Modifiers build() {
            requireNonNull(modifiers.modifiersList);
            return modifiers;
        }

        private Modifiers modifiers = new Modifiers();

        public Builder modifiers(List modifiersList) {
            modifiers.modifiersList = modifiersList;
            return this;
        }

        public Builder resolver(Resolver resolver) {
            modifiers.resolver = resolver;
            return this;
        }
    }

    private Modifiers() {

    }
}
