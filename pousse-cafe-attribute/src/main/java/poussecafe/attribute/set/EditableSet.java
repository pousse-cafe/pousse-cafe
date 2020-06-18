package poussecafe.attribute.set;

import java.util.Set;
import poussecafe.collection.SetEditor;

public interface EditableSet<T> extends Set<T> {

    SetEditor<T> edit();
}
