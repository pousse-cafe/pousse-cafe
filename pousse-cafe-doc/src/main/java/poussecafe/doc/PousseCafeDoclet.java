package poussecafe.doc;

import com.sun.javadoc.LanguageVersion;
import com.sun.javadoc.RootDoc;
import java.io.File;
import java.util.Objects;
import poussecafe.context.MetaApplicationContext;

public class PousseCafeDoclet {

    public PousseCafeDoclet(RootDoc rootDoc) {
        Objects.requireNonNull(rootDoc);
        rootDocWrapper = new RootDocWrapper(rootDoc);
        Logger.setRootDoc(rootDocWrapper);

        context = new MetaApplicationContext();
        context.addBoundedContext(PousseCafeDoc.configure()
                .defineAndImplementDefault()
                .build());
    }

    private RootDocWrapper rootDocWrapper;

    private MetaApplicationContext context;

    public void start() {
        Logger.info("Starting Pousse-Café doclet...");
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
        detectDomainProcesses();
        detectRelations();
    }

    private void detectBoundedContexts() {
        BoundedContextDocCreator boundedContextCreator = new BoundedContextDocCreator(rootDocWrapper);
        context.injectDependencies(boundedContextCreator);

        PackagesAnalyzer codeAnalyzer = new PackagesAnalyzer.Builder()
                .rootDocWrapper(rootDocWrapper)
                .packageDocConsumer(boundedContextCreator)
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

        FactoryDocCreator factoryDocCreator = new FactoryDocCreator(rootDocWrapper);
        context.injectDependencies(factoryDocCreator);

        ClassesAnalyzer codeAnalyzer = new ClassesAnalyzer.Builder()
                .rootDocWrapper(rootDocWrapper)
                .classDocConsumer(aggregateDocCreator)
                .classDocConsumer(serviceDocCreator)
                .classDocConsumer(entityDocCreator)
                .classDocConsumer(valueObjectDocCreator)
                .classDocConsumer(factoryDocCreator)
                .build();
        codeAnalyzer.analyzeCode();
    }

    private void detectDomainProcesses() {
        DomainProcessDocCreator domainProcessDocCreator = new DomainProcessDocCreator(rootDocWrapper);
        context.injectDependencies(domainProcessDocCreator);

        ClassesAnalyzer codeAnalyzer = new ClassesAnalyzer.Builder()
                .rootDocWrapper(rootDocWrapper)
                .classDocConsumer(domainProcessDocCreator)
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
        GraphImagesWriter graphsWriter = new GraphImagesWriter(rootDocWrapper);
        context.injectDependencies(graphsWriter);
        graphsWriter.writeImages();
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
