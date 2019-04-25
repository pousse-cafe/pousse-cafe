package poussecafe.doc;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import javax.lang.model.element.TypeElement;
import poussecafe.doc.model.AnnotationsResolver;
import poussecafe.doc.model.DocletAccess;

public class ClassesAnalyzer {

    public static class Builder {

        private ClassesAnalyzer analyzer = new ClassesAnalyzer();

        public Builder classDocConsumer(Consumer<TypeElement> classDocConsumer) {
            Objects.requireNonNull(classDocConsumer);
            analyzer.classDocConsumers.add(classDocConsumer);
            return this;
        }

        public ClassesAnalyzer build() {
            return analyzer;
        }
    }

    private ClassesAnalyzer() {

    }

    private List<Consumer<TypeElement>> classDocConsumers = new ArrayList<>();

    public void analyzeCode() {
        for (TypeElement classDoc : docletAccess.typeElements()) {
            if (!annotationsResolver.isIgnored(classDoc)) {
                processClassDoc(classDoc);
            }
        }
    }

    private DocletAccess docletAccess;

    private AnnotationsResolver annotationsResolver;

    private void processClassDoc(TypeElement classDoc) {
        for(Consumer<TypeElement> consumer : classDocConsumers) {
            consumer.accept(classDoc);
        }
    }
}
