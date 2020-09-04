package poussecafe.source.generation.tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.eclipse.jdt.core.ToolFactory;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IRegion;
import org.eclipse.text.edits.TextEdit;

import static java.util.Objects.requireNonNull;

public class CodeFormatter {

    public void formatCode() throws BadLocationException {
        var codeFormatter = ToolFactory.createCodeFormatter(options);
        String code = document.get();
        TextEdit codeFormatEdit;
        if(isDocumentNew
                || edits == null) {
            codeFormatEdit = codeFormatter.format(org.eclipse.jdt.core.formatter.CodeFormatter.K_COMPILATION_UNIT, code, 0, code.length(), 0, null);
        } else {
            IRegion[] regions = extractRegions(edits);
            int indentationLevel = 0;
            codeFormatEdit = codeFormatter.format(org.eclipse.jdt.core.formatter.CodeFormatter.K_UNKNOWN, code, regions, indentationLevel, null);
        }
        codeFormatEdit.apply(document);
    }

    private Map<String, String> options = new HashMap<>();

    private TextEdit edits;

    private Document document;

    private boolean isDocumentNew;

    private IRegion[] extractRegions(TextEdit edits) {
        var regions = new ArrayList<IRegion>();
        extractRegions(edits, regions);
        return regions.toArray(new IRegion[0]);
    }

    private void extractRegions(TextEdit edits, ArrayList<IRegion> regions) {
        int childrenSize = edits.getChildrenSize();
        if(childrenSize == 0 && edits.getOffset() != -1) {
            regions.add(edits.getRegion());
        } else {
            for(TextEdit child : edits.getChildren()) {
                if(child.getOffset() != -1) {
                    extractRegions(child, regions);
                }
            }
        }
    }

    public static class Builder {

        private CodeFormatter formatter = new CodeFormatter();

        public CodeFormatter build() {
            requireNonNull(formatter.options);
            requireNonNull(formatter.document);
            return formatter;
        }

        public Builder options(Map<String, String> options) {
            formatter.options.putAll(options);
            return this;
        }

        public Builder document(Document document) {
            formatter.document = document;
            return this;
        }

        public Builder isDocumentNew(boolean isDocumentNew) {
            formatter.isDocumentNew = isDocumentNew;
            return this;
        }

        public Builder edits(TextEdit edits) {
            formatter.edits = edits;
            return this;
        }
    }

    private CodeFormatter() {

    }
}
