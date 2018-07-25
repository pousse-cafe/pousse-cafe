package poussecafe.doc;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import org.apache.commons.io.IOUtils;
import poussecafe.doc.graph.DirectedGraph;
import poussecafe.doc.graph.Graph;

public class GraphImageWriter {

    public void writeImage(Graph graph, File outputDirectory,
            String graphName) throws IOException {
        File dotFile = new File(outputDirectory, graphName + ".dot");
        PrintStream printStream = new PrintStream(dotFile);
        DotPrinter dotPrinter = new DotPrinter(printStream);
        dotPrinter.print(graph);
        printStream.close();

        String tool = chooseTool(graph);
        Process process = new ProcessBuilder(tool, "-Tpng",
                dotFile.getAbsolutePath()).start();

        File pngFile = new File(outputDirectory, graphName + ".png");
        OutputStream pngOutputStream = new FileOutputStream(pngFile);
        IOUtils.copy(process.getInputStream(), pngOutputStream);
        pngOutputStream.close();
    }

    private String chooseTool(Graph graph) {
        if(graph instanceof DirectedGraph) {
            return "dot";
        } else {
            return "fdp";
        }
    }
}
