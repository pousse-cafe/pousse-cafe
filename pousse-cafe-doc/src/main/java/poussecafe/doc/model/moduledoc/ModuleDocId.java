package poussecafe.doc.model.moduledoc;

import poussecafe.domain.ValueObject;
import poussecafe.util.StringId;

/**
 * @trivial
 */
public class ModuleDocId extends StringId implements ValueObject {

    public static ModuleDocId ofPackageName(String packageName) {
        return new ModuleDocId(packageName);
    }

    public ModuleDocId(String value) {
        super(value);
    }

}
