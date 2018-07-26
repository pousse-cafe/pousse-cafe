package poussecafe.doc;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.FieldDoc;
import com.sun.javadoc.MethodDoc;
import com.sun.javadoc.ParameterizedType;
import com.sun.javadoc.Type;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

import static poussecafe.check.Checks.checkThatValue;

public class PathFinder {

    public static class Builder {

        private PathFinder pathFinder = new PathFinder();

        public Builder start(ClassDoc start) {
            pathFinder.start = start;
            return this;
        }

        public Builder basePackage(String basePackage) {
            pathFinder.basePackage = basePackage;
            return this;
        }

        public Builder classMatcher(Predicate<ClassDoc> matcher) {
            pathFinder.matcher = matcher;
            return this;
        }

        public Builder pathHandler(PathHandler pathHandler) {
            pathFinder.pathHandler = pathHandler;
            return this;
        }

        public PathFinder build() {
            checkThatValue(pathFinder.start).notNull();
            checkThatValue(pathFinder.basePackage).notNull();
            checkThatValue(pathFinder.matcher).notNull();
            checkThatValue(pathFinder.pathHandler).notNull();
            return pathFinder;
        }
    }

    private PathFinder() {

    }

    private ClassDoc start;

    private Predicate<ClassDoc> matcher;

    public void start() {
        explore(start);
    }

    private void explore(ClassDoc classDoc) {
        if(!classDoc.containingPackage().name().startsWith(basePackage) || alreadyExplored.contains(classDoc)) {
            return;
        }
        alreadyExplored.add(classDoc);

        for(MethodDoc methodDoc : classDoc.methods()) {
            if(isCrawlableMethod(classDoc, methodDoc)) {
                Type returnType = methodDoc.returnType();
                tryType(returnType);
            }
        }
        for(FieldDoc fieldDoc : classDoc.fields()) {
            if(isCrawlableField(classDoc, fieldDoc)) {
                Type returnType = fieldDoc.type();
                tryType(returnType);
            }
        }
    }

    private String basePackage;

    private Set<ClassDoc> alreadyExplored = new HashSet<>();

    private boolean isCrawlableMethod(ClassDoc classDoc, MethodDoc methodDoc) {
        return methodDoc.isPublic() &&
                methodDoc.overriddenMethod() == null &&
                !methodDoc.isSynthetic() &&
                methodDoc.containingClass() == classDoc;
    }

    private boolean isCrawlableField(ClassDoc classDoc, FieldDoc methodDoc) {
        return methodDoc.isPublic() &&
                !methodDoc.isSynthetic() &&
                methodDoc.containingClass() == classDoc;
    }

    private void tryType(Type type) {
        if(!tryParametrizedType(type)) {
            tryClassDoc(type);
        }
    }

    private boolean tryParametrizedType(Type returnType) {
        ParameterizedType parametrizedType = returnType.asParameterizedType();
        if(parametrizedType != null) {
            for(Type typeArgument : parametrizedType.typeArguments()) {
                tryType(typeArgument);
            }
            return true;
        } else {
            return false;
        }
    }

    private boolean tryClassDoc(Type type) {
        ClassDoc classDoc = type.asClassDoc();
        if(classDoc != null && classDoc.qualifiedTypeName().startsWith(basePackage)) {
            if(matcher.test(classDoc)) {
                pathHandler.handle(start, classDoc);
            } else {
                explore(classDoc);
            }
            return true;
        } else {
            return false;
        }
    }

    private PathHandler pathHandler;
}
