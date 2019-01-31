package poussecafe.doc;

import com.sun.javadoc.ClassDoc;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public class ClassesAnalyzer {

    public static class Builder {

        private ClassesAnalyzer analyzer = new ClassesAnalyzer();

        public Builder rootDocWrapper(RootDocWrapper rootDocWrapper) {
            analyzer.rootDocWrapper = rootDocWrapper;
            return this;
        }

        public Builder classDocConsumer(Consumer<ClassDoc> classDocConsumer) {
            Objects.requireNonNull(classDocConsumer);
            analyzer.classDocConsumers.add(classDocConsumer);
            return this;
        }

        public ClassesAnalyzer build() {
            Objects.requireNonNull(analyzer.rootDocWrapper);
            return analyzer;
        }
    }

    private ClassesAnalyzer() {

    }

    private RootDocWrapper rootDocWrapper;

    private List<Consumer<ClassDoc>> classDocConsumers = new ArrayList<>();

    public void analyzeCode() {
        ClassDoc[] classes = rootDocWrapper.rootDoc().classes();
        for (ClassDoc classDoc : classes) {
            if (!AnnotationsResolver.isIgnored(classDoc)) {
                processClassDoc(classDoc);
            }
        }
    }

    private void processClassDoc(ClassDoc classDoc) {
        for(Consumer<ClassDoc> consumer : classDocConsumers) {
            consumer.accept(classDoc);
        }
    }
}
