package poussecafe.source.validation;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import poussecafe.source.Source;
import poussecafe.source.SourceConsumer;
import poussecafe.source.SourceScanner;
import poussecafe.source.analysis.ClassLoaderClassResolver;
import poussecafe.source.analysis.ClassResolver;
import poussecafe.source.analysis.CompilationUnitResolver;
import poussecafe.source.analysis.Name;
import poussecafe.source.analysis.ResolvedClass;
import poussecafe.source.validation.entity.EntityValidator;
import poussecafe.source.validation.listener.MessageListenerValidator;
import poussecafe.source.validation.message.MessageValidator;
import poussecafe.source.validation.model.Module;
import poussecafe.source.validation.model.ValidationModel;
import poussecafe.source.validation.names.NamesValidator;
import poussecafe.source.validation.namingconventions.NamingConventionsValidator;

import static java.util.Objects.requireNonNull;

public class Validator implements SourceConsumer {

    public void validate() {
        if(result == null) {
            model = visitor.buildModel();
            enrichModelWithClassPathComponents();
            buildValidators();
            for(SubValidator validator : validators) {
                long startTime = System.currentTimeMillis();
                runAndIncludeMessages(validator);
                long endTime = System.currentTimeMillis();
                if(logger.isDebugEnabled()) {
                    logger.debug("{} validation took {} ms", validator.name(), (endTime - startTime));
                }
            }
            result = new ValidationResult(messages);
        }
    }

    private ValidationModel model;

    private void enrichModelWithClassPathComponents() {
        if(classPathExplorer.isPresent()) {
            var modules = classPathExplorer.get().getSubTypesOf(new Name(CompilationUnitResolver.MODULE_INTERFACE));
            for(ResolvedClass moduleClass : modules) {
                var module = new Module.Builder()
                        .name(moduleClass.name())
                        .build();
                model.addClassPathModule(module);
            }
        }
    }

    private void buildValidators() {
        validators = new ArrayList<>();
        validators.add(new MessageValidator(model));
        validators.add(new EntityValidator(model));
        validators.add(new MessageListenerValidator(model));
        validators.add(new NamesValidator(model));
        validators.add(new NamingConventionsValidator(model));
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

    private Logger logger = LoggerFactory.getLogger(getClass());

    public Validator() {
        this(new ClassLoaderClassResolver(), Optional.empty());
    }

    public Validator(ClassResolver classResolver, Optional<ClassPathExplorer> classPathExplorer) {
        visitor = new ValidationModelBuildingVisitor(classResolver);
        scanner = new SourceScanner(visitor);

        requireNonNull(classPathExplorer);
        this.classPathExplorer = classPathExplorer;
    }

    private Optional<ClassPathExplorer> classPathExplorer = Optional.empty();
}
