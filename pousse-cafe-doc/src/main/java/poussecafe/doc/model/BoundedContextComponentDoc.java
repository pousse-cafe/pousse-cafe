package poussecafe.doc.model;

import poussecafe.doc.model.boundedcontextdoc.BoundedContextDocId;
import poussecafe.domain.ValueObject;

public class BoundedContextComponentDoc implements ValueObject {

    public static class Builder {

        private BoundedContextComponentDoc doc = new BoundedContextComponentDoc();

        public Builder componentDoc(ComponentDoc componentDoc) {
            doc.componentDoc = componentDoc;
            return this;
        }

        public Builder boundedContextDocId(BoundedContextDocId boundedContextDocId) {
            doc.boundedContextDocId = boundedContextDocId;
            return this;
        }

        public BoundedContextComponentDoc build() {
            return doc;
        }
    }

    private BoundedContextComponentDoc() {

    }

    private ComponentDoc componentDoc;

    public ComponentDoc componentDoc() {
        return componentDoc;
    }

    private BoundedContextDocId boundedContextDocId;

    public BoundedContextDocId boundedContextDocId() {
        return boundedContextDocId;
    }
}
