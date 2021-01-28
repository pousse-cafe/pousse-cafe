package poussecafe.source.emil.parser;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import org.antlr.v4.runtime.tree.TerminalNode;
import poussecafe.source.PathSource;
import poussecafe.source.Source;
import poussecafe.source.analysis.Name;
import poussecafe.source.analysis.SafeClassName;
import poussecafe.source.emil.parser.EmilParser.AggregateRootConsumptionContext;
import poussecafe.source.emil.parser.EmilParser.CommandConsumptionContext;
import poussecafe.source.emil.parser.EmilParser.ConsumptionContext;
import poussecafe.source.emil.parser.EmilParser.EventConsumptionContext;
import poussecafe.source.emil.parser.EmilParser.EventProductionContext;
import poussecafe.source.emil.parser.EmilParser.EventProductionsContext;
import poussecafe.source.emil.parser.EmilParser.ExternalContext;
import poussecafe.source.emil.parser.EmilParser.FactoryConsumptionContext;
import poussecafe.source.emil.parser.EmilParser.MessageConsumptionsContext;
import poussecafe.source.emil.parser.EmilParser.MultipleMessageConsumptionsContext;
import poussecafe.source.emil.parser.EmilParser.MultipleMessageConsumptionsItemContext;
import poussecafe.source.emil.parser.EmilParser.ProcessConsumptionContext;
import poussecafe.source.emil.parser.EmilParser.ProcessContext;
import poussecafe.source.emil.parser.EmilParser.QualifiedNameContext;
import poussecafe.source.emil.parser.EmilParser.RepositoryConsumptionContext;
import poussecafe.source.emil.parser.EmilParser.SingleMessageConsumptionContext;
import poussecafe.source.generation.NamingConventions;
import poussecafe.source.model.AggregateContainer;
import poussecafe.source.model.Command;
import poussecafe.source.model.DomainEvent;
import poussecafe.source.model.Hooks;
import poussecafe.source.model.Message;
import poussecafe.source.model.MessageListener;
import poussecafe.source.model.MessageListenerContainer;
import poussecafe.source.model.MessageListenerContainerType;
import poussecafe.source.model.Model;
import poussecafe.source.model.ModelBuilder;
import poussecafe.source.model.ProcessModel;
import poussecafe.source.model.ProducedEvent;
import poussecafe.source.model.ProductionType;
import poussecafe.source.model.StandaloneAggregateFactory;
import poussecafe.source.model.StandaloneAggregateRepository;
import poussecafe.source.model.StandaloneAggregateRoot;
import poussecafe.source.model.TypeComponent;

import static java.util.Objects.requireNonNull;

public class TreeAnalyzer {

    public void analyze() {
        ProcessContext process = tree.processContext();
        if(process.header().NAME() != null) {
            processName = process.header().NAME().getText();
            model.addProcess(new ProcessModel.Builder()
                    .name(processName)
                    .packageName(NamingConventions.processesPackageName(basePackage))
                    .build());
        }

        for(ConsumptionContext consumption : process.consumptions().consumption()) {
            if(consumption.commandConsumption() != null) {
                analyzeCommandConsumption(consumption.commandConsumption());
            } else if(consumption.eventConsumption() != null) {
                analyzeEventConsumption(consumption.eventConsumption());
            } else {
                throw new IllegalStateException("Unsupported consumption rule");
            }
        }
    }

    private Tree tree;

    private String processName;

    private ModelBuilder model = new ModelBuilder();

    public Model model() {
        for(Entry<String, StandaloneAggregateRoot.Builder> root : standaloneAggregateRoots.entrySet()) {
            var aggregateName = root.getKey();
            var builder = root.getValue();
            var hooksBuilder = aggregateHooks.get(aggregateName);
            if(hooksBuilder != null) {
                builder.hooks(hooksBuilder.build());
            } else {
                builder.hooks(Hooks.EMPTY);
            }
            model.addStandaloneAggregateRoot(builder.build());
        }

        for(Entry<String, AggregateContainer.Builder> root : aggregateContainers.entrySet()) {
            var aggregateName = root.getKey();
            var builder = root.getValue();
            var hooksBuilder = aggregateHooks.get(aggregateName);
            if(hooksBuilder != null) {
                builder.hooks(hooksBuilder.build());
            }
            model.addAggregateContainer(builder.build());
        }

        return model.build();
    }

    private void analyzeCommandConsumption(CommandConsumptionContext context) {
        var commandName = context.command().NAME().getText();
        model.addCommand(new Command.Builder()
                .name(commandName)
                .packageName(NamingConventions.commandsPackageName(basePackage))
                .build());
        analyzeMessageConsumptions(Optional.empty(), Message.command(commandName), context.messageConsumptions());
    }

    private void analyzeMessageConsumptions(Optional<String> consumesFromExternal,
            Message consumedMessage,
            MessageConsumptionsContext messageConsumptions) {
        if(messageConsumptions.singleMessageConsumption() != null) {
            analyzeSingleMessageConsumption(consumesFromExternal, consumedMessage,
                    messageConsumptions.singleMessageConsumption());
        } else if(messageConsumptions.multipleMessageConsumptions() != null) {
            analyzeMultipleMessageConsumptions(consumesFromExternal, consumedMessage,
                    messageConsumptions.multipleMessageConsumptions());
        } else {
            throw new IllegalStateException("Unsupported messageConsumptions rule");
        }
    }

    private void analyzeSingleMessageConsumption(Optional<String> consumesFromExternal,
            Message consumedMessage,
            SingleMessageConsumptionContext singleMessageConsumption) {
        if(singleMessageConsumption.factoryConsumption() != null) {
            analyzeFactoryConsumption(consumesFromExternal, consumedMessage,
                    singleMessageConsumption.factoryConsumption());
        } else if(singleMessageConsumption.aggregateRootConsumption() != null) {
            analyzeAggregateRootConsumption(consumesFromExternal, consumedMessage,
                    singleMessageConsumption.aggregateRootConsumption());
        } else if(singleMessageConsumption.repositoryConsumption() != null) {
            analyzeRepositoryConsumption(consumesFromExternal, consumedMessage,
                    singleMessageConsumption.repositoryConsumption());
        } else if(singleMessageConsumption.processConsumption() != null) {
            analyzeProcessConsumption(singleMessageConsumption.processConsumption());
        } else if(singleMessageConsumption.emptyConsumption() != null) {
            // No listener to add, consumed by externals is handled by consumedByExternal
        } else {
            throw new IllegalStateException("Unsupported singleMessageConsumption rule");
        }
    }

    private void analyzeFactoryConsumption(Optional<String> consumesFromExternal,
            Message consumedMessage,
            FactoryConsumptionContext factoryConsumption) {
        var factoryListener = factoryConsumption.factoryListener();
        var simpleFactoryName = factoryListener.simpleFactoryName;
        var qualifiedFactoryName = factoryListener.qualifiedFactoryName;

        String aggregateName;
        String containerIdentifier;
        if(simpleFactoryName != null) {
            var simpleFactoryNameString = simpleFactoryName.getText();
            containerIdentifier = simpleFactoryNameString;
            if(!NamingConventions.isAggregateFactoryName(simpleFactoryNameString)) {
                throw new IllegalStateException("Unexpected factory name " + simpleFactoryNameString);
            }
            aggregateName = NamingConventions.aggregateNameFromSimpleFactoryName(simpleFactoryNameString);
            var typeName = SafeClassName.ofRootClass(new Name(packageName(aggregateName), simpleFactoryNameString));
            model.addStandaloneAggregateFactory(new StandaloneAggregateFactory.Builder()
                    .typeComponent(typeComponent(typeName))
                    .build());
        } else if(qualifiedFactoryName != null) {
            aggregateName = qualifiedFactoryName.qualifier.getText();
            containerIdentifier = qualifiedFactoryName.getText();
            addAggregateContainer(qualifiedFactoryName);
        } else {
            throw new UnsupportedOperationException();
        }

        var builder = new MessageListener.Builder();
        builder.withContainer(new MessageListenerContainer.Builder()
                .aggregateName(aggregateName)
                .type(simpleFactoryName != null ? MessageListenerContainerType.STANDALONE_FACTORY : MessageListenerContainerType.INNER_FACTORY)
                .containerIdentifier(containerIdentifier)
                .build());
        builder.withMethodName(factoryListener.listenerName.getText());
        builder.withConsumedMessage(consumedMessage);
        if(processName != null) {
            builder.withProcessName(processName);
        }

        if(factoryListener.optional != null) {
            builder.withProductionType(Optional.of(ProductionType.OPTIONAL));
        } else if(factoryListener.serveral != null) {
            builder.withProductionType(Optional.of(ProductionType.SEVERAL));
        } else {
            builder.withProductionType(Optional.of(ProductionType.SINGLE));
        }

        if(factoryConsumption.eventProductions() != null) {
            var hooksBuilder = aggregateHooks.computeIfAbsent(aggregateName, key -> new Hooks.Builder());
            var producedEvents = producedEvents(factoryConsumption.eventProductions());
            hooksBuilder.onAddProducedEvents(producedEvents);
        }

        builder.withConsumesFromExternal(consumesFromExternal);

        model.addMessageListener(builder.build());

        analyzeEventProductions(factoryConsumption.eventProductions());
    }

    private void addAggregateContainer(QualifiedNameContext qualifiedFactoryName) {
        var typeName = SafeClassName.ofRootClass(new Name(packageName(qualifiedFactoryName.qualifier.getText()),
                qualifiedFactoryName.qualifier.getText())).withLastSegment(qualifiedFactoryName.name.getText());
        aggregateContainers.computeIfAbsent(qualifiedFactoryName.qualifier.getText(), key -> new AggregateContainer.Builder()
                .typeComponent(typeComponent(typeName)));
    }

    private TypeComponent typeComponent(SafeClassName typeName) {
        return new TypeComponent.Builder()
                .name(typeName)
                .source(source(typeName))
                .build();
    }

    private Source source(SafeClassName typeName) {
        var rootClassName = typeName.rootClassName();
        var segments = rootClassName.segments();
        var pathElements = new String[segments.length];
        System.arraycopy(segments, 0, pathElements, 0, segments.length - 1);
        pathElements[segments.length - 1] = segments[segments.length - 1] + ".java";
        return new PathSource(Path.of("", pathElements));
    }

    private Map<String, AggregateContainer.Builder> aggregateContainers = new HashMap<>();

    private Map<String, Hooks.Builder> aggregateHooks = new HashMap<>();

    private List<ProducedEvent> producedEvents(EventProductionsContext eventProductions) {
        var producedEvents = new ArrayList<ProducedEvent>();
        if(eventProductions != null) {
            for(EventProductionContext eventProduction : eventProductions.eventProduction()) {
                var producedEvent = new ProducedEvent.Builder()
                        .message(Message.domainEvent(eventProduction.event().NAME().getText()))
                        .required(eventProduction.optional == null)
                        .consumedByExternal(consumedByExternal(eventProduction.messageConsumptions()))
                        .build();
                producedEvents.add(producedEvent);
            }
        }
        return producedEvents;
    }

    private List<String> consumedByExternal(MessageConsumptionsContext messageConsumptions) {
        var externals = new ArrayList<String>();
        if(messageConsumptions.singleMessageConsumption() != null) {
            external(messageConsumptions.singleMessageConsumption()).ifPresent(externals::add);
        } else if(messageConsumptions.multipleMessageConsumptions() != null) {
            for(MultipleMessageConsumptionsItemContext item : messageConsumptions.multipleMessageConsumptions()
                    .multipleMessageConsumptionsItem()) {
                external(item.singleMessageConsumption()).ifPresent(externals::add);
            }
        } else {
            throw new IllegalStateException("Unsupported messageConsumptions rule");
        }
        return externals;
    }

    private Optional<String> external(SingleMessageConsumptionContext singleMessageConsumption) {
        if(singleMessageConsumption.emptyConsumption() != null
                && singleMessageConsumption.emptyConsumption().external() != null) {
            return Optional.of(singleMessageConsumption.emptyConsumption().external().NAME().getText());
        } else {
            return Optional.empty();
        }
    }

    private String packageName(String aggregateName) {
        return basePackage + ".model." + aggregateName.toLowerCase();
    }

    private String basePackage;

    private void analyzeEventProductions(EventProductionsContext eventProductions) {
        if(eventProductions != null) {
            for(EventProductionContext eventProduction : eventProductions.eventProduction()) {
                analyzeEventProduction(eventProduction);
            }
        }
    }

    private void analyzeEventProduction(EventProductionContext eventProduction) {
        var message = Message.domainEvent(eventProduction.event().NAME().getText());
        model.addEvent(event(message.name()));
        analyzeMessageConsumptions(Optional.empty(), message, eventProduction.messageConsumptions());
    }

    private DomainEvent event(String name) {
        return new DomainEvent.Builder()
                .name(name)
                .packageName(basePackage + ".model.events")
                .build();
    }

    private void analyzeAggregateRootConsumption(Optional<String> consumesFromExternal,
            Message consumedMessage,
            AggregateRootConsumptionContext aggregateRootConsumption) {
        var simpleName = aggregateRootConsumption.aggregateRoot().simpleRootName;
        var qualifiedName = aggregateRootConsumption.aggregateRoot().qualifiedRootName;

        String aggregateName;
        String containerIdentifier;
        if(simpleName != null) {
            aggregateName = NamingConventions.aggregateNameFromSimpleRootName(simpleName.getText());
            containerIdentifier = simpleName.getText();

            var typeName = SafeClassName.ofRootClass(new Name(packageName(aggregateName), simpleName.getText()));
            standaloneAggregateRoots.computeIfAbsent(aggregateName, key -> new StandaloneAggregateRoot.Builder()
                    .typeComponent(typeComponent(typeName)));
        } else if(qualifiedName != null) {
            aggregateName = qualifiedName.qualifier.getText();
            containerIdentifier = qualifiedName.getText();
            addAggregateContainer(qualifiedName);
        } else {
            throw new UnsupportedOperationException();
        }

        var builder = new MessageListener.Builder();
        builder.withContainer(new MessageListenerContainer.Builder()
                .aggregateName(aggregateName)
                .type(simpleName != null ? MessageListenerContainerType.STANDALONE_ROOT : MessageListenerContainerType.INNER_ROOT)
                .containerIdentifier(containerIdentifier)
                .build());
        builder.withMethodName(aggregateRootConsumption.listenerName.getText());
        builder.withConsumedMessage(consumedMessage);
        if(processName != null) {
            builder.withProcessName(processName);
        }

        var runnerName = aggregateRootConsumption.runnerName.getText();
        builder.withRunnerName(Optional.of(runnerName));

        var producedEvents = producedEvents(aggregateRootConsumption.eventProductions());
        builder.withProducedEvents(producedEvents);

        builder.withConsumesFromExternal(consumesFromExternal);

        model.addMessageListener(builder.build());

        analyzeEventProductions(aggregateRootConsumption.eventProductions());
    }

    private Map<String, StandaloneAggregateRoot.Builder> standaloneAggregateRoots = new HashMap<>();

    private void analyzeRepositoryConsumption(Optional<String> consumesFromExternal,
            Message consumedMessage,
            RepositoryConsumptionContext repositoryConsumption) {

        var simpleRepositoryName = repositoryConsumption.simpleRepositoryName;
        var qualifiedRepositoryName = repositoryConsumption.qualifiedRepositoryName;

        String aggregateName;
        String containerIdentifier;
        if(simpleRepositoryName != null) {
            var simpleRepositoryNameString = simpleRepositoryName.getText();
            if(!NamingConventions.isAggregateRepositoryName(simpleRepositoryNameString)) {
                throw new IllegalStateException("Unexpected repository name " + simpleRepositoryName);
            }
            aggregateName = NamingConventions.aggregateNameFromSimpleRepositoryName(simpleRepositoryNameString);
            containerIdentifier = simpleRepositoryName.getText();

            var typeName = SafeClassName.ofRootClass(new Name(packageName(aggregateName), simpleRepositoryNameString));
            model.addStandaloneAggregateRepository(new StandaloneAggregateRepository.Builder()
                    .typeComponent(typeComponent(typeName))
                    .build());
        } else if(qualifiedRepositoryName != null) {
            aggregateName = qualifiedRepositoryName.qualifier.getText();
            containerIdentifier = qualifiedRepositoryName.getText();
            addAggregateContainer(qualifiedRepositoryName);
        } else {
            throw new UnsupportedOperationException();
        }

        var builder = new MessageListener.Builder();
        builder.withContainer(new MessageListenerContainer.Builder()
                .aggregateName(aggregateName)
                .type(simpleRepositoryName != null ? MessageListenerContainerType.STANDALONE_REPOSITORY : MessageListenerContainerType.INNER_REPOSITORY)
                .containerIdentifier(containerIdentifier)
                .build());
        builder.withMethodName(repositoryConsumption.listenerName.getText());
        builder.withConsumedMessage(consumedMessage);
        if(processName != null) {
            builder.withProcessName(processName);
        }

        if(repositoryConsumption.eventProductions() != null) {
            var hooksBuilder = aggregateHooks.computeIfAbsent(aggregateName, key -> new Hooks.Builder());
            var producedEvents = producedEvents(repositoryConsumption.eventProductions());
            hooksBuilder.onDeleteProducedEvents(producedEvents);
        }

        builder.withConsumesFromExternal(consumesFromExternal);

        model.addMessageListener(builder.build());

        analyzeEventProductions(repositoryConsumption.eventProductions());
    }

    private void analyzeProcessConsumption(ProcessConsumptionContext processConsumption) {
        model.addProcess(new ProcessModel.Builder()
                .name(processConsumption.NAME().getText())
                .packageName(basePackage + ".process")
                .build());
    }

    private void analyzeMultipleMessageConsumptions(Optional<String> consumesFromExternal,
            Message consumedMessage,
            MultipleMessageConsumptionsContext multipleMessageConsumptions) {
        for(MultipleMessageConsumptionsItemContext item : multipleMessageConsumptions.multipleMessageConsumptionsItem()) {
            analyzeSingleMessageConsumption(consumesFromExternal, consumedMessage, item.singleMessageConsumption());
        }
    }

    private void analyzeEventConsumption(EventConsumptionContext context) {
        var eventName = context.event().NAME().getText();
        model.addEvent(event(eventName));
        Optional<String> consumesFromExternal = Optional.ofNullable(context.external())
                .map(ExternalContext::NAME)
                .map(TerminalNode::getText);
        analyzeMessageConsumptions(consumesFromExternal, Message.domainEvent(eventName), context.messageConsumptions());
    }

    public static class Builder {

        private TreeAnalyzer analyzer = new TreeAnalyzer();

        public TreeAnalyzer build() {
            requireNonNull(analyzer.tree);

            if(!analyzer.tree.isValid()) {
                throw new IllegalStateException("Tree must be valid, see errors");
            }

            return analyzer;
        }

        public Builder tree(Tree tree) {
            analyzer.tree = tree;
            return this;
        }

        public Builder basePackage(String basePackage) {
            analyzer.basePackage = basePackage;
            return this;
        }
    }

    private TreeAnalyzer() {

    }
}
