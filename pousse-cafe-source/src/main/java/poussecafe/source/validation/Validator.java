package poussecafe.source.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

public class Validator {

    public void validate() {
        if(result == null) {
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

    private void runAndIncludeMessages(SubValidator validator) {
        validator.validate();
        messages.addAll(validator.messages());
    }

    private ValidationResult result;

    private List<ValidationMessage> messages = new ArrayList<>();

    public ValidationResult result() {
        return result;
    }

    private Logger logger = LoggerFactory.getLogger(getClass());

    public Validator(ValidationModel model) {
        this(model, Optional.empty());
    }

    public Validator(ValidationModel model, Optional<ClassPathExplorer> classPathExplorer) {
        requireNonNull(model);
        this.model = model;

        requireNonNull(classPathExplorer);
        this.classPathExplorer = classPathExplorer;
    }

    private Optional<ClassPathExplorer> classPathExplorer = Optional.empty();
}
