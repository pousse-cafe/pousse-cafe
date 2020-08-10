package poussecafe.source.analysis;

import java.util.Optional;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.MemberValuePair;
import org.eclipse.jdt.core.dom.NormalAnnotation;
import org.eclipse.jdt.core.dom.SingleMemberAnnotation;

import static java.util.Objects.requireNonNull;

public class ResolvedAnnotation {

    public boolean isClass(Class<? extends java.lang.annotation.Annotation> annotationClass) {
        return resolver.resolve(new Name(annotation.getTypeName())).isClass(annotationClass);
    }

    public Optional<AnnotationAttribute> attribute(String attributeName) {
        if(annotation.isNormalAnnotation()) {
            NormalAnnotation normalAnnotation = (NormalAnnotation) annotation;
            for(Object object : normalAnnotation.values()) {
                MemberValuePair pair = (MemberValuePair) object;
                if(pair.getName().getIdentifier().equals(attributeName)) {
                    return Optional.of(new AnnotationAttribute.Builder()
                            .resolver(resolver)
                            .value(pair.getValue())
                            .build());
                }
            }
        } else if(annotation.isSingleMemberAnnotation()
                && attributeName.equals("value")) {
            SingleMemberAnnotation singleMemberAnnotation = (SingleMemberAnnotation) annotation;
            return Optional.of(new AnnotationAttribute.Builder()
                    .resolver(resolver)
                    .value(singleMemberAnnotation.getValue())
                    .build());
        }
        return Optional.empty();
    }

    private Resolver resolver;

    private Annotation annotation;

    public static class Builder {

        private ResolvedAnnotation annotation = new ResolvedAnnotation();

        public ResolvedAnnotation build() {
            requireNonNull(annotation.resolver);
            requireNonNull(annotation.annotation);
            return annotation;
        }

        public Builder resolver(Resolver resolver) {
            annotation.resolver = resolver;
            return this;
        }

        public Builder annotation(Annotation annotation) {
            this.annotation.annotation = annotation;
            return this;
        }
    }

    private ResolvedAnnotation() {

    }
}
