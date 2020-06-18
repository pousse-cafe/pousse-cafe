package poussecafe.attribute.list;

import java.util.List;
import poussecafe.collection.ListEditor;

public interface EditableList<T> extends List<T> {

    ListEditor<T> edit();
}
