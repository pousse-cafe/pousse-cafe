package poussecafe.doc.model;

import poussecafe.doc.model.boundedcontextdoc.BoundedContextDocKey;

public class BoundedContextComponentDoc {

    public static class Builder {

        private BoundedContextComponentDoc doc = new BoundedContextComponentDoc();

        public Builder componentDoc(ComponentDoc componentDoc) {
            doc.componentDoc = componentDoc;
            return this;
        }

        public Builder boundedContextDocKey(BoundedContextDocKey boundedContextDocKey) {
            doc.boundedContextDocKey = boundedContextDocKey;
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

    private BoundedContextDocKey boundedContextDocKey;

    public BoundedContextDocKey boundedContextDocKey() {
        return boundedContextDocKey;
    }
}
