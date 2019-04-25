package poussecafe.doc;

import com.sun.source.doctree.EndElementTree;
import com.sun.source.doctree.StartElementTree;
import com.sun.source.doctree.TextTree;
import com.sun.source.util.DocTreeScanner;

public class TagContentStringBuilder extends DocTreeScanner<StringBuilder, StringBuilder> {

    @Override
    public StringBuilder visitText(TextTree node,
            StringBuilder p) {
        return p.append(node.getBody());
    }

    @Override
    public StringBuilder visitStartElement(StartElementTree node,
            StringBuilder p) {
        return p.append(node.toString());
    }

    @Override
    public StringBuilder visitEndElement(EndElementTree node,
            StringBuilder p) {
        return p.append(node.toString());
    }
}
