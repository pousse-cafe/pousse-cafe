package poussecafe.source.emil.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.antlr.v4.runtime.tree.TerminalNode;
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
import poussecafe.source.emil.parser.EmilParser.RepositoryConsumptionContext;
import poussecafe.source.emil.parser.EmilParser.SingleMessageConsumptionContext;
import poussecafe.source.model.Aggregate;
import poussecafe.source.model.Command;
import poussecafe.source.model.DomainEvent;
import poussecafe.source.model.Message;
import poussecafe.source.model.MessageListener;
import poussecafe.source.model.MessageListenerContainer;
import poussecafe.source.model.MessageListenerContainerType;
import poussecafe.source.model.Model;
import poussecafe.source.model.ProcessModel;
import poussecafe.source.model.ProducedEvent;
import poussecafe.source.model.ProductionType;

import static java.util.Objects.requireNonNull;

public class TreeAnalyzer {

    public void analyze() {
        ProcessContext process = tree.processContext();
        if(process.header().NAME() != null) {
            processName = process.header().NAME().getText();
            model.addProcess(new ProcessModel.Builder()
                    .name(processName)
                    .packageName(basePackage + ".process")
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

    private Model model = new Model();

    public Model model() {
        return model;
    }

    private void analyzeCommandConsumption(CommandConsumptionContext context) {
        var commandName = context.command().NAME().getText();
        model.addCommand(new Command.Builder()
                .name(commandName)
                .packageName(basePackage + ".commands")
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
        var factoryName = factoryListener.factoryName.getText();
        if(!factoryName.endsWith(FACTORY_NAME_SUFFIX)) {
            throw new IllegalStateException("Unexpected factory name " + factoryName + " (does not end with "
                    + FACTORY_NAME_SUFFIX + ")");
        }
        var aggregateName = factoryName.substring(0, factoryName.length() - FACTORY_NAME_SUFFIX.length());

        var builder = new MessageListener.Builder();
        builder.withContainer(new MessageListenerContainer.Builder()
                .aggregateName(aggregateName)
                .type(MessageListenerContainerType.FACTORY)
                .className(factoryName)
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
            var existingAggregate = ensureAggregateExists(aggregateName);
            Aggregate.Builder aggregateBuilder = new Aggregate.Builder().startingFrom(existingAggregate);

            var producedEvents = producedEvents(factoryConsumption.eventProductions());
            aggregateBuilder.onAddProducedEvents(producedEvents);
            model.putAggregate(aggregateBuilder.build());
        }

        builder.withConsumesFromExternal(consumesFromExternal);

        model.addMessageListener(builder.build());

        analyzeEventProductions(factoryConsumption.eventProductions());
    }

    private static final String FACTORY_NAME_SUFFIX = "Factory";

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

    private Aggregate ensureAggregateExists(String aggregateName) {
        var existingAggregate = model.aggregate(aggregateName);
        if(existingAggregate.isEmpty()) {
            var aggregate = new Aggregate.Builder()
                    .name(aggregateName)
                    .packageName(packageName(aggregateName))
                    .build();
            model.putAggregate(aggregate);
            return aggregate;
        } else {
            return existingAggregate.get();
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
        var aggregateRootName = aggregateRootConsumption.aggregateRoot().NAME().getText();
        String aggregateName;
        if(aggregateRootName.endsWith(ROOT_NAME_SUFFIX)) {
            aggregateName = aggregateRootName.substring(0, aggregateRootName.length() - ROOT_NAME_SUFFIX.length());
        } else {
            aggregateName = aggregateRootName;
        }

        ensureAggregateExists(aggregateName);

        var builder = new MessageListener.Builder();
        builder.withContainer(new MessageListenerContainer.Builder()
                .aggregateName(aggregateName)
                .type(MessageListenerContainerType.ROOT)
                .className(aggregateRootName)
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

    private static final String ROOT_NAME_SUFFIX = "Root";

    private void analyzeRepositoryConsumption(Optional<String> consumesFromExternal,
            Message consumedMessage,
            RepositoryConsumptionContext repositoryConsumption) {
        var repositoryName = repositoryConsumption.repositoryName.getText();
        if(!repositoryName.endsWith(REPOSITORY_NAME_SUFFIX)) {
            throw new IllegalStateException("Unexpected repository name " + repositoryName + " (does not end with "
                    + REPOSITORY_NAME_SUFFIX + ")");
        }
        var aggregateName = repositoryName.substring(0, repositoryName.length() - REPOSITORY_NAME_SUFFIX.length());

        var builder = new MessageListener.Builder();
        builder.withContainer(new MessageListenerContainer.Builder()
                .aggregateName(aggregateName)
                .type(MessageListenerContainerType.REPOSITORY)
                .className(repositoryName)
                .build());
        builder.withMethodName(repositoryConsumption.listenerName.getText());
        builder.withConsumedMessage(consumedMessage);
        if(processName != null) {
            builder.withProcessName(processName);
        }

        if(repositoryConsumption.eventProductions() != null) {
            var existingAggregate = ensureAggregateExists(aggregateName);
            Aggregate.Builder aggregateBuilder = new Aggregate.Builder().startingFrom(existingAggregate);

            var producedEvents = producedEvents(repositoryConsumption.eventProductions());
            aggregateBuilder.onDeleteProducedEvents(producedEvents);
            model.putAggregate(aggregateBuilder.build());
        }

        builder.withConsumesFromExternal(consumesFromExternal);

        model.addMessageListener(builder.build());

        analyzeEventProductions(repositoryConsumption.eventProductions());
    }

    private static final String REPOSITORY_NAME_SUFFIX = "Repository";

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
