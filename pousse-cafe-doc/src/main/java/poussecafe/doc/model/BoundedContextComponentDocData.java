package poussecafe.doc.model;

import java.io.Serializable;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDocId;

@SuppressWarnings("serial")
public class BoundedContextComponentDocData implements Serializable {

    public static BoundedContextComponentDocData adapt(BoundedContextComponentDoc boundedContextComponentDoc) {
        BoundedContextComponentDocData data = new BoundedContextComponentDocData();
        data.componentDoc = ComponentDocData.of(boundedContextComponentDoc.componentDoc());
        data.boundedContextId = boundedContextComponentDoc.boundedContextDocId().stringValue();
        return data;
    }

    public ComponentDocData componentDoc;

    public String boundedContextId;

    public BoundedContextComponentDoc adapt() {
        return new BoundedContextComponentDoc.Builder()
                .componentDoc(componentDoc.toModel())
                .boundedContextDocId(BoundedContextDocId.ofPackageName(boundedContextId))
                .build();
    }
}
