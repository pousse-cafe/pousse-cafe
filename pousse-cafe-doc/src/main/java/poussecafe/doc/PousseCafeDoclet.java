package poussecafe.doc;

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
        BoundedContextDocCreator boundedContextCreator = new BoundedContextDocCreator(rootDocWrapper);
        context.injectDependencies(boundedContextCreator);

        AggregateDocCreator aggregateDocCreator = new AggregateDocCreator(rootDocWrapper);
        context.injectDependencies(aggregateDocCreator);

        ClassesAnalyzer analyzer = new ClassesAnalyzer.Builder()
                .rootDocWrapper(rootDocWrapper)
                .classDocConsumer(boundedContextCreator)
                .classDocConsumer(aggregateDocCreator)
                .build();
        analyzer.analyzeCode();
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
        return 0;
    }
}
