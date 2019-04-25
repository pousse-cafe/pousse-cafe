package poussecafe.doc;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import jdk.javadoc.doclet.DocletEnvironment;
import poussecafe.doc.model.DocletAccess;
import poussecafe.doc.model.DocletServices;

public class PathFinder {

    public static class Builder {

        private PathFinder pathFinder = new PathFinder();

        public Builder start(TypeElement start) {
            pathFinder.start = start;
            return this;
        }

        public Builder basePackage(String basePackage) {
            pathFinder.basePackage = basePackage;
            return this;
        }

        public Builder classMatcher(Predicate<TypeElement> matcher) {
            pathFinder.matcher = matcher;
            return this;
        }

        public Builder pathHandler(PathHandler pathHandler) {
            pathFinder.pathHandler = pathHandler;
            return this;
        }

        public Builder docletServices(DocletServices docletServices) {
            pathFinder.docletServices = docletServices;
            return this;
        }

        public PathFinder build() {
            Objects.requireNonNull(pathFinder.start);
            Objects.requireNonNull(pathFinder.basePackage);
            Objects.requireNonNull(pathFinder.matcher);
            Objects.requireNonNull(pathFinder.pathHandler);
            Objects.requireNonNull(pathFinder.docletServices);
            return pathFinder;
        }
    }

    private PathFinder() {

    }

    private TypeElement start;

    private Predicate<TypeElement> matcher;

    public void start() {
        explore(start);
    }

    public void explore(TypeElement classDoc) {
        DocletAccess docletAccess = docletServices.docletAccess();
        PackageElement packageElement = docletAccess.packageElement(classDoc);
        if(!packageElement.getQualifiedName().toString().startsWith(basePackage) ||
                alreadyExplored.contains(classDoc.getQualifiedName().toString())) {
            return;
        }
        alreadyExplored.add(classDoc.getQualifiedName().toString());

        for(ExecutableElement methodDoc : docletAccess.methods(classDoc)) {
            if(isCrawlableMethod(methodDoc)) {
                TypeMirror returnType = methodDoc.getReturnType();
                tryType(returnType);
            }
        }
        for(VariableElement fieldDoc : docletAccess.fields(classDoc)) {
            if(isCrawlableField(fieldDoc)) {
                TypeMirror returnType = fieldDoc.asType();
                tryType(returnType);
            }
        }
    }

    DocletServices docletServices;

    private String basePackage;

    private Set<String> alreadyExplored = new HashSet<>();

    private boolean isCrawlableMethod(ExecutableElement methodDoc) {
        return methodDoc.getModifiers().contains(Modifier.PUBLIC) &&
                !docletServices.docletAccess().isOverride(methodDoc) &&
                !docletServices.annotationsResolver().isIgnored(methodDoc);
    }

    private boolean isCrawlableField(VariableElement methodDoc) {
        return methodDoc.getModifiers().contains(Modifier.PUBLIC) &&
                !docletServices.annotationsResolver().isIgnored(methodDoc);
    }

    private void tryType(TypeMirror type) {
        if(!tryParametrizedType(type)) {
            tryClassDoc(type);
        }
    }

    private boolean tryParametrizedType(TypeMirror returnType) {
        if(returnType instanceof DeclaredType) {
            DeclaredType declaredType = (DeclaredType) returnType;
            if(declaredType.getTypeArguments().isEmpty()) {
                return false;
            } else {
                for(TypeMirror typeArgument : declaredType.getTypeArguments()) {
                    tryType(typeArgument);
                }
                return true;
            }
        } else {
            return false;
        }
    }

    private boolean tryClassDoc(TypeMirror type) {
        DocletEnvironment docletEnvironment = docletServices.docletEnvironment();
        Element element = docletEnvironment.getTypeUtils().asElement(type);
        if(element instanceof TypeElement) {
            TypeElement typeElement = (TypeElement) element;
            if(docletEnvironment.getElementUtils().getPackageOf(typeElement).getQualifiedName().toString().startsWith(basePackage) &&
                    matcher.test(typeElement)) {
                pathHandler.handle(start, typeElement);
            } else {
                explore(typeElement);
            }
            return true;
        } else {
            return false;
        }
    }

    private PathHandler pathHandler;
}
