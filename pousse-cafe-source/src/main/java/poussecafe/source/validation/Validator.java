package poussecafe.source.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import poussecafe.source.analysis.ClassLoaderClassResolver;
import poussecafe.source.analysis.ClassResolver;
import poussecafe.source.analysis.CompilationUnitResolver;
import poussecafe.source.analysis.ClassName;
import poussecafe.source.analysis.ResolvedClass;
import poussecafe.source.validation.entity.EntityValidator;
import poussecafe.source.validation.listener.MessageListenerValidator;
import poussecafe.source.validation.message.MessageValidator;
import poussecafe.source.validation.model.Module;
import poussecafe.source.validation.model.ValidationModel;
import poussecafe.source.validation.names.NamesValidator;
import poussecafe.source.validation.namingconventions.NamingConventionsValidator;
import poussecafe.source.validation.types.StorageTypesValidator;
import poussecafe.source.validation.types.TypesValidator;

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
            var modules = classPathExplorer.get().getSubTypesOf(new ClassName(CompilationUnitResolver.MODULE_INTERFACE));
            for(ResolvedClass moduleClass : modules) {
                var module = new Module.Builder()
                        .className(moduleClass.name())
                        .build();
                model.addClassPathModule(module);
            }
        }
    }

    private void buildValidators() {
        validators = new ArrayList<>();
        validators.add(new MessageValidator(model));
        validators.add(new EntityValidator(model));
        validators.add(new MessageListenerValidator(model, classResolver));
        validators.add(new NamesValidator(model));
        validators.add(new NamingConventionsValidator(model));
        validators.add(new TypesValidator(model, storageTypesValidators, classResolver));
    }

    private ClassResolver classResolver;

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

    public static class Builder {

        public Validator build() {
            requireNonNull(validator.model);
            requireNonNull(validator.classPathExplorer);
            requireNonNull(validator.storageTypesValidators);

            if(validator.classResolver == null) {
                validator.classResolver = new ClassLoaderClassResolver();
            }

            validator.storageTypesValidators.forEach(this::initValidator);

            return validator;
        }

        private Validator validator = new Validator();

        private void initValidator(StorageTypesValidator storageValidator) {
            storageValidator.setModel(validator.model);
            storageValidator.setClassResolver(validator.classResolver);
        }

        public Builder model(ValidationModel model) {
            validator.model = model;
            return this;
        }

        public Builder classResolver(ClassResolver classResolver) {
            validator.classResolver = classResolver;
            return this;
        }

        public Builder classPathExplorer(ClassPathExplorer classPathExplorer) {
            validator.classPathExplorer = Optional.of(classPathExplorer);
            return this;
        }

        public Builder storageTypesValidator(StorageTypesValidator storageTypesValidator) {
            requireNonNull(storageTypesValidator);
            validator.storageTypesValidators.add(storageTypesValidator);
            return this;
        }
    }

    private Validator() {

    }

    private Optional<ClassPathExplorer> classPathExplorer = Optional.empty();

    private List<StorageTypesValidator> storageTypesValidators = new ArrayList<>();
}
