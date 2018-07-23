package poussecafe.doc.model;

import java.io.Serializable;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDocKey;

@SuppressWarnings("serial")
public class BoundedContextComponentDocData implements Serializable {

    public static BoundedContextComponentDocData of(BoundedContextComponentDoc boundedContextComponentDoc) {
        BoundedContextComponentDocData data = new BoundedContextComponentDocData();
        data.componentDoc = ComponentDocData.of(boundedContextComponentDoc.componentDoc());
        data.boundedContextClassName = boundedContextComponentDoc.boundedContextDocKey().getValue();
        return data;
    }

    public ComponentDocData componentDoc;

    public String boundedContextClassName;

    public BoundedContextComponentDoc toModel() {
        return new BoundedContextComponentDoc.Builder()
                .componentDoc(componentDoc.toModel())
                .boundedContextDocKey(BoundedContextDocKey.ofClassName(boundedContextClassName))
                .build();
    }
}
