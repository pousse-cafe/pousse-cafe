package poussecafe.doc;

import com.sun.javadoc.LanguageVersion;
import com.sun.javadoc.RootDoc;
import java.io.File;
import poussecafe.context.MetaApplicationContext;

import static poussecafe.check.Checks.checkThatValue;

public class PousseCafeDoclet {

    public PousseCafeDoclet(RootDoc rootDoc) {
        checkThatValue(rootDoc).notNull();
        rootDocWrapper = new RootDocWrapper(rootDoc);

        context = new MetaApplicationContext();
        context.addBundle(new PousseCafeDoc());
    }

    private RootDocWrapper rootDocWrapper;

    private MetaApplicationContext context;

    public void start() {
        rootDocWrapper.info("Starting Pousse-Café doclet...");
        context.start();

        analyzeCode();
        createOutputFolder();

        writeGraphs();
        writeHtml();
        writePdf();
    }

    private void analyzeCode() {
        detectBoundedContexts();
        detectBoundedContextComponents();
        detectRelations();
    }

    private void detectBoundedContexts() {
        BoundedContextDocCreator boundedContextCreator = new BoundedContextDocCreator(rootDocWrapper);
        context.injectDependencies(boundedContextCreator);

        ClassesAnalyzer codeAnalyzer = new ClassesAnalyzer.Builder()
                .rootDocWrapper(rootDocWrapper)
                .classDocConsumer(boundedContextCreator)
                .build();
        codeAnalyzer.analyzeCode();
    }

    private void detectBoundedContextComponents() {
        AggregateDocCreator aggregateDocCreator = new AggregateDocCreator(rootDocWrapper);
        context.injectDependencies(aggregateDocCreator);

        ServiceDocCreator serviceDocCreator = new ServiceDocCreator(rootDocWrapper);
        context.injectDependencies(serviceDocCreator);

        EntityDocCreator entityDocCreator = new EntityDocCreator(rootDocWrapper);
        context.injectDependencies(entityDocCreator);

        ValueObjectDocCreator valueObjectDocCreator = new ValueObjectDocCreator(rootDocWrapper);
        context.injectDependencies(valueObjectDocCreator);

        ClassesAnalyzer codeAnalyzer = new ClassesAnalyzer.Builder()
                .rootDocWrapper(rootDocWrapper)
                .classDocConsumer(aggregateDocCreator)
                .classDocConsumer(serviceDocCreator)
                .classDocConsumer(entityDocCreator)
                .classDocConsumer(valueObjectDocCreator)
                .build();
        codeAnalyzer.analyzeCode();
    }

    private void detectRelations() {
        RelationCreator relationCreator = new RelationCreator(rootDocWrapper);
        context.injectDependencies(relationCreator);

        ClassesAnalyzer codeAnalyzer = new ClassesAnalyzer.Builder()
                .rootDocWrapper(rootDocWrapper)
                .classDocConsumer(relationCreator)
                .build();
        codeAnalyzer.analyzeCode();
    }

    private void createOutputFolder() {
        File outputDirectory = new File(rootDocWrapper.outputPath());
        outputDirectory.mkdirs();
    }

    private void writeGraphs() {
        GraphsWriter graphsWriter = new GraphsWriter(rootDocWrapper);
        context.injectDependencies(graphsWriter);
        graphsWriter.writeGraphs();
    }

    private void writeHtml() {
        HtmlWriter htmlWriter = new HtmlWriter(rootDocWrapper);
        context.injectDependencies(htmlWriter);
        htmlWriter.writeHtml();
    }

    private void writePdf() {
        PdfWriter pdfWriter = new PdfWriter(rootDocWrapper);
        context.injectDependencies(pdfWriter);
        pdfWriter.writePdf();
    }

    public static boolean start(RootDoc root) {
        new PousseCafeDoclet(root).start();
        return true;
    }

    public static int optionLength(String option) {
        if ("-output".equals(option)) {
            return 2;
        }
        if ("-debug".equals(option)) {
            return 1;
        }
        if ("-version".equals(option)) {
            return 2;
        }
        if ("-domain".equals(option)) {
            return 2;
        }
        if ("-basePackage".equals(option)) {
            return 2;
        }
        return 0;
    }

    public static LanguageVersion languageVersion() {
        return LanguageVersion.JAVA_1_5;
     }
}
