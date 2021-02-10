package poussecafe.source.validation.types;

import poussecafe.storage.internal.InternalStorage;

public class InteralStorageTypesValidator extends StorageTypesValidator {

    @Override
    protected String storageName() {
        return InternalStorage.NAME;
    }
}
