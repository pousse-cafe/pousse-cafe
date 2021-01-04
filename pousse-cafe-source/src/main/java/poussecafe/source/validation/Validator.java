package poussecafe.source.validation;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import poussecafe.source.Source;
import poussecafe.source.SourceConsumer;
import poussecafe.source.SourceScanner;
import poussecafe.source.analysis.ClassLoaderClassResolver;
import poussecafe.source.analysis.ClassResolver;
import poussecafe.source.validation.entity.EntityValidator;
import poussecafe.source.validation.listener.MessageListenerValidator;
import poussecafe.source.validation.message.MessageValidator;
import poussecafe.source.validation.model.ValidationModel;
import poussecafe.source.validation.names.NamesValidator;

public class Validator implements SourceConsumer {

    public void validate() {
        if(result == null) {
            model = visitor.buildModel();
            buildValidators();
            for(SubValidator validator : validators) {
                runAndIncludeMessages(validator);
            }
            result = new ValidationResult(messages);
        }
    }

    private ValidationModel model;

    private void buildValidators() {
        validators = new ArrayList<>();
        validators.add(new MessageValidator(model));
        validators.add(new EntityValidator(model));
        validators.add(new MessageListenerValidator(model));
        validators.add(new NamesValidator(model));
    }

    private List<SubValidator> validators;

    private ValidationModelBuildingVisitor visitor;

    private void runAndIncludeMessages(SubValidator validator) {
        validator.validate();
        messages.addAll(validator.messages());
    }

    private ValidationResult result;

    private List<ValidationMessage> messages = new ArrayList<>();

    public ValidationResult result() {
        return result;
    }

    @Override
    public void includeFile(Path sourceFilePath) throws IOException {
        scanner.includeFile(sourceFilePath);
    }

    private SourceScanner scanner;

    @Override
    public void includeTree(Path sourceDirectory) throws IOException {
        scanner.includeTree(sourceDirectory);
    }

    @Override
    public void includeSource(Source source) {
        scanner.includeSource(source);
    }

    public Validator() {
        this(new ClassLoaderClassResolver());
    }

    public Validator(ClassResolver classResolver) {
        visitor = new ValidationModelBuildingVisitor(classResolver);
        scanner = new SourceScanner(visitor);
    }
}
