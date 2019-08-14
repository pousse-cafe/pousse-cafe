package poussecafe.doc;

import java.io.File;
import java.io.FileOutputStream;
import org.xhtmlrenderer.pdf.ITextRenderer;
import org.xhtmlrenderer.util.XRLog;

public class PdfWriter {

    public void writePdf() {
        try {
            if (configuration.isDebug()) {
                System.getProperties().setProperty("xr.util-logging.loggingEnabled", "true");
                XRLog.setLoggingEnabled(true);
            }

            Logger.debug("Writing PDF...");
            ITextRenderer pdfRenderer = new ITextRenderer();
            pdfRenderer.setDocument(new File(configuration.outputDirectory(), "index.html"));
            pdfRenderer.layout();
            File pdfFile = new File(configuration.outputDirectory(), configuration.pdfFileName());
            FileOutputStream pdfOutputStream = new FileOutputStream(pdfFile);
            pdfRenderer.createPDF(pdfOutputStream);
            pdfRenderer.finishPDF();
            pdfOutputStream.close();
        } catch (Exception e) {
            throw new RuntimeException("Error while writing PDF", e);
        }
    }

    private PousseCafeDocletConfiguration configuration;
}
