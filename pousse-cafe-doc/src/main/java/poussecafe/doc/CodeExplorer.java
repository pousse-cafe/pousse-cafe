package poussecafe.doc;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.lang.model.element.TypeElement;
import poussecafe.doc.model.DocletServices;

public class CodeExplorer {

    public static class Builder {

        private CodeExplorer codeExplorer = new CodeExplorer();

        public Builder rootClassDoc(TypeElement classDoc) {
            codeExplorer.rootClassDoc = classDoc;
            return this;
        }

        public Builder basePackage(String basePackage) {
            codeExplorer.basePackage = basePackage;
            return this;
        }

        public Builder classRelationBuilder(RelationBuilder classRelationBuilder) {
            codeExplorer.classRelationBuilder = classRelationBuilder;
            return this;
        }

        public Builder docletServices(DocletServices docletServices) {
            codeExplorer.docletServices = docletServices;
            return this;
        }

        public CodeExplorer build() {
            Objects.requireNonNull(codeExplorer.rootClassDoc);
            Objects.requireNonNull(codeExplorer.basePackage);
            Objects.requireNonNull(codeExplorer.classRelationBuilder);
            Objects.requireNonNull(codeExplorer.docletServices);
            return codeExplorer;
        }
    }

    private CodeExplorer() {

    }

    private TypeElement rootClassDoc;

    public void explore() {
        pathFinder().start();
    }

    private PathFinder pathFinder() {
        return new PathFinder.Builder()
                .start(rootClassDoc)
                .basePackage(basePackage)
                .classMatcher(this::classMatcher)
                .pathHandler(this::pathHandler)
                .docletServices(docletServices)
                .build();
    }

    private DocletServices docletServices;

    public void explore(TypeElement start) {
        pathFinder().explore(start);
    }

    private boolean classMatcher(TypeElement candidateClassDoc) {
        return docletServices.entityDocFactory().isEntityDoc(candidateClassDoc) ||
                docletServices.valueObjectDocFactory().isValueObjectDoc(candidateClassDoc);
    }

    private void pathHandler(TypeElement componentFrom, TypeElement componentTo) {
        String relation = componentFrom.getQualifiedName().toString() + "_" + componentTo.getQualifiedName().toString();
        if(!alreadyMatched.contains(relation)) {
            alreadyMatched.add(relation);
            classRelationBuilder.classRelationBuilder(componentFrom, componentTo);
        }
    }

    private String basePackage;

    private RelationBuilder classRelationBuilder;

    private Set<String> alreadyMatched = new HashSet<>();
}
