package poussecafe.source.validation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import poussecafe.source.validation.model.ValidationModel;

import static java.util.Objects.requireNonNull;

public abstract class SubValidator {

    protected SubValidator(ValidationModel model) {
        requireNonNull(model);
        this.model = model;
    }

    protected ValidationModel model;

    public abstract void validate();

    public List<ValidationMessage> messages() {
        return Collections.unmodifiableList(messages);
    }

    protected List<ValidationMessage> messages = new ArrayList<>();

    protected abstract String name();
}
