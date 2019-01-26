package poussecafe.doc;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Objects;
import org.xhtmlrenderer.pdf.ITextRenderer;
import org.xhtmlrenderer.util.XRLog;

public class PdfWriter {

    public PdfWriter(RootDocWrapper rootDocWrapper) {
        Objects.requireNonNull(rootDocWrapper);
        this.rootDocWrapper = rootDocWrapper;
    }

    private RootDocWrapper rootDocWrapper;

    public void writePdf() {
        try {
            if (rootDocWrapper.isDebug()) {
                System.getProperties().setProperty("xr.util-logging.loggingEnabled", "true");
                XRLog.setLoggingEnabled(true);
            }

            Logger.debug("Writing PDF...");
            ITextRenderer pdfRenderer = new ITextRenderer();
            pdfRenderer.setDocument(new File(rootDocWrapper.outputPath(), "index.html"));
            pdfRenderer.layout();
            File pdfFile = new File(rootDocWrapper.outputPath(), "domain.pdf");
            FileOutputStream pdfOutputStream = new FileOutputStream(pdfFile);
            pdfRenderer.createPDF(pdfOutputStream);
            pdfRenderer.finishPDF();
            pdfOutputStream.close();
        } catch (Exception e) {
            throw new RuntimeException("Error while writing PDF", e);
        }
    }
}
