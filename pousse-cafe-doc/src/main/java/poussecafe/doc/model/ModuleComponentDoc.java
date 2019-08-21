package poussecafe.doc.model;

import poussecafe.doc.model.moduledoc.ModuleDocId;
import poussecafe.domain.ValueObject;

public class ModuleComponentDoc implements ValueObject {

    public static class Builder {

        private ModuleComponentDoc doc = new ModuleComponentDoc();

        public Builder componentDoc(ComponentDoc componentDoc) {
            doc.componentDoc = componentDoc;
            return this;
        }

        public Builder moduleDocId(ModuleDocId moduleDocId) {
            doc.moduleDocId = moduleDocId;
            return this;
        }

        public ModuleComponentDoc build() {
            return doc;
        }
    }

    private ModuleComponentDoc() {

    }

    private ComponentDoc componentDoc;

    public ComponentDoc componentDoc() {
        return componentDoc;
    }

    private ModuleDocId moduleDocId;

    public ModuleDocId moduleDocId() {
        return moduleDocId;
    }
}
