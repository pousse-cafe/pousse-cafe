package poussecafe.doc.data;

import poussecafe.doc.model.BoundedContextDocDataAccess;
import poussecafe.storage.memory.InMemoryDataAccess;

public class InMemoryBoundedContextDocDataAccess extends InMemoryDataAccess<String, BoundedContextDocData> implements BoundedContextDocDataAccess<BoundedContextDocData> {

}
