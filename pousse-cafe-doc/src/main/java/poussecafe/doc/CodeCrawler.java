package poussecafe.doc;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.FieldDoc;
import com.sun.javadoc.MethodDoc;
import com.sun.javadoc.ParameterizedType;
import com.sun.javadoc.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import static poussecafe.check.Checks.checkThatValue;

public class CodeCrawler {

    public static class Builder {

        private CodeCrawler codeCrawler = new CodeCrawler();

        public Builder rootClassDoc(ClassDoc classDoc) {
            codeCrawler.rootClassDoc = classDoc;
            return this;
        }

        public Builder matcher(Predicate<ClassDoc> matcher) {
            codeCrawler.matcher = matcher;
            return this;
        }

        public Builder maxMatchedDepth(int maxMatchedDepth) {
            codeCrawler.maxMatchedDepth = maxMatchedDepth;
            return this;
        }

        public Builder basePackage(String basePackage) {
            codeCrawler.basePackage = basePackage;
            return this;
        }

        public List<ClassDoc> buildAndCrawlCode() {
            return build().crawlCode();
        }

        private CodeCrawler build() {
            checkThatValue(codeCrawler.rootClassDoc).notNull();
            checkThatValue(codeCrawler.matcher).notNull();
            checkThatValue(codeCrawler.basePackage).notNull();
            checkThatValue(codeCrawler.maxMatchedDepth > 0).is(true);
            return codeCrawler;
        }
    }

    private CodeCrawler() {

    }

    private ClassDoc rootClassDoc;

    private List<ClassDoc> crawlCode() {
        crawl(0, rootClassDoc);
        return matchingClassDocs;
    }

    private void crawl(int currentDepth, ClassDoc classDoc) {
        if(!classDoc.containingPackage().name().startsWith(basePackage)) {
            return;
        }
        for(MethodDoc methodDoc : classDoc.methods()) {
            if(isCrawlableMethod(classDoc, methodDoc)) {
                Type returnType = methodDoc.returnType();
                tryType(currentDepth, returnType);
            }
        }
        for(FieldDoc fieldDoc : classDoc.fields()) {
            if(isCrawlableField(classDoc, fieldDoc)) {
                Type returnType = fieldDoc.type();
                tryType(currentDepth, returnType);
            }
        }
    }

    private String basePackage;

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

    private void tryType(int currentDepth,
            Type type) {
        if(currentDepth >= maxMatchedDepth) {
            return;
        }
        boolean handled = false;
        if(!handled) {
            handled = tryParametrizedType(currentDepth, type);
        }
        if(!handled) {
            handled = tryClassDoc(currentDepth, type);
        }
    }

    private int maxMatchedDepth;

    private boolean tryClassDoc(int currentDepth, Type type) {
        ClassDoc classDoc = type.asClassDoc();
        if(classDoc != null &&
                classDoc.qualifiedTypeName().startsWith(basePackage) &&
                !alreadyCrawled.contains(type.qualifiedTypeName())) {
            alreadyCrawled.add(classDoc.qualifiedTypeName());
            if(matcher.test(classDoc)) {
                matchingClassDocs.add(classDoc);
                crawl(currentDepth + 1, classDoc);
            } else {
                crawl(currentDepth, classDoc);
            }
            return true;
        } else {
            return false;
        }
    }

    private boolean tryParametrizedType(int currentDepth,
            Type returnType) {
        ParameterizedType parametrizedType = returnType.asParameterizedType();
        if(parametrizedType != null) {
            for(Type typeArgument : parametrizedType.typeArguments()) {
                tryType(currentDepth, typeArgument);
            }
            return true;
        } else {
            return false;
        }
    }

    private Predicate<ClassDoc> matcher;

    private Set<String> alreadyCrawled = new HashSet<>();

    private List<ClassDoc> matchingClassDocs = new ArrayList<>();
}
