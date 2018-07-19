package poussecafe.doc;

import com.sun.javadoc.RootDoc;
import java.io.File;
import poussecafe.context.MetaApplicationContext;

public class DDDDoclet {

    public static boolean start(RootDoc root) {
        root.printNotice("Starting DDD doclet...");

        MetaApplicationContext context = new MetaApplicationContext();
        context.addBundle(new PousseCafeDoc());
        context.start();

        RootDocWrapper rootDocWrapper = new RootDocWrapper(root);

        BoundedContextCodeAnalyzer boundedContextCodeAnalyzer = new BoundedContextCodeAnalyzer.Builder()
                .rootDocWrapper(rootDocWrapper)
                .build();
        context.injectDependencies(boundedContextCodeAnalyzer);
        boundedContextCodeAnalyzer.analyzeCode();

        AggregateCodeAnalyzer aggregateCodeAnalyzer = new AggregateCodeAnalyzer.Builder()
                .rootDocWrapper(rootDocWrapper)
                .build();
        context.injectDependencies(aggregateCodeAnalyzer);
        aggregateCodeAnalyzer.analyzeCode();

        File outputDirectory = new File(rootDocWrapper.outputPath());
        outputDirectory.mkdirs();

        GraphsWriter graphsWriter = new GraphsWriter(rootDocWrapper);
        context.injectDependencies(graphsWriter);
        graphsWriter.writeGraphs();

        HtmlWriter htmlWriter = new HtmlWriter(rootDocWrapper);
        context.injectDependencies(htmlWriter);
        htmlWriter.writeHtml();

        PdfWriter pdfWriter = new PdfWriter(rootDocWrapper);
        context.injectDependencies(pdfWriter);
        pdfWriter.writePdf();

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
