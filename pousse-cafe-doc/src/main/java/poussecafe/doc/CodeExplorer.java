package poussecafe.doc;

import com.sun.javadoc.ClassDoc;
import java.util.HashSet;
import java.util.Set;
import poussecafe.doc.model.entitydoc.EntityDocFactory;
import poussecafe.doc.model.vodoc.ValueObjectDocFactory;

import java.util.Objects;

public class CodeExplorer {

    public static class Builder {

        private CodeExplorer codeExplorer = new CodeExplorer();

        public Builder rootClassDoc(ClassDoc classDoc) {
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

        public CodeExplorer build() {
            Objects.requireNonNull(codeExplorer.rootClassDoc);
            Objects.requireNonNull(codeExplorer.basePackage);
            Objects.requireNonNull(codeExplorer.classRelationBuilder);
            return codeExplorer;
        }
    }

    private CodeExplorer() {

    }

    private ClassDoc rootClassDoc;

    public void explore() {
        PathFinder finder = new PathFinder.Builder()
                .start(rootClassDoc)
                .basePackage(basePackage)
                .classMatcher(this::classMatcher)
                .pathHandler(this::pathHandler)
                .build();
        finder.start();
    }

    private boolean classMatcher(ClassDoc candidateClassDoc) {
        return EntityDocFactory.isEntityDoc(candidateClassDoc) ||
                ValueObjectDocFactory.isValueObjectDoc(candidateClassDoc);
    }

    private void pathHandler(ClassDoc componentFrom, ClassDoc componentTo) {
        String relation = componentFrom.name() + "_" + componentTo.name();
        if(!alreadyMatched.contains(relation)) {
            alreadyMatched.add(relation);
            classRelationBuilder.classRelationBuilder(componentFrom, componentTo);
        }
    }

    private String basePackage;

    private RelationBuilder classRelationBuilder;

    private Set<String> alreadyMatched = new HashSet<>();
}
