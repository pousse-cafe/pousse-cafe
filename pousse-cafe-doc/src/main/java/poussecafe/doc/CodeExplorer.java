package poussecafe.doc;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.ProgramElementDoc;
import java.util.HashSet;
import java.util.Set;
import poussecafe.doc.model.entitydoc.EntityDocFactory;
import poussecafe.doc.model.vodoc.ValueObjectDocFactory;

import static poussecafe.check.Checks.checkThatValue;

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

        public Builder programElementRelationBuilder(ProgramElementRelationBuilder programElementRelationBuilder) {
            codeExplorer.programElementRelationBuilder = programElementRelationBuilder;
            return this;
        }

        public CodeExplorer build() {
            checkThatValue(codeExplorer.rootClassDoc).notNull();
            checkThatValue(codeExplorer.basePackage).notNull();
            checkThatValue(codeExplorer.classRelationBuilder).notNull();
            checkThatValue(codeExplorer.programElementRelationBuilder).notNull();
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
                .programElementMatcher(this::programElementMatcher)
                .programElementHandler(this::programElementHandler)
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

    private boolean programElementMatcher(ProgramElementDoc candidateDoc) {
        return ValueObjectDocFactory.isValueObjectDoc(candidateDoc);
    }

    private void programElementHandler(ProgramElementDoc componentTo) {
        String relation = rootClassDoc.name() + "_" + componentTo.name();
        if(!alreadyMatched.contains(relation)) {
            alreadyMatched.add(relation);
            programElementRelationBuilder.programElementRelationBuilder(componentTo);
        }
    }

    private String basePackage;

    private RelationBuilder classRelationBuilder;

    private ProgramElementRelationBuilder programElementRelationBuilder;

    private Set<String> alreadyMatched = new HashSet<>();
}
