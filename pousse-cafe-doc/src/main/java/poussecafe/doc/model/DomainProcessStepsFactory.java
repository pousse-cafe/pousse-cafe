package poussecafe.doc.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import poussecafe.doc.model.domainprocessdoc.DomainProcessDoc;
import poussecafe.doc.model.domainprocessdoc.Step;
import poussecafe.doc.model.domainprocessdoc.StepName;
import poussecafe.doc.model.domainprocessdoc.ToStep;
import poussecafe.doc.model.moduledoc.ModuleDocId;
import poussecafe.doc.model.processstepdoc.NameRequired;
import poussecafe.doc.model.processstepdoc.ProcessStepDoc;
import poussecafe.doc.model.processstepdoc.ProcessStepDocRepository;
import poussecafe.doc.model.processstepdoc.StepMethodSignature;
import poussecafe.domain.Service;

import static java.util.stream.Collectors.toList;

public class DomainProcessStepsFactory implements Service {

    public DomainProcessSteps buildDomainProcessSteps(DomainProcessDoc domainProcessDoc) {
        DomainProcessSteps.Builder stepsBuilder = new DomainProcessSteps.Builder();

        ModuleComponentDoc moduleComponentDoc = domainProcessDoc.attributes().moduleComponentDoc().value();
        ModuleDocId moduleDocId = moduleComponentDoc.moduleDocId();
        String processName = moduleComponentDoc.componentDoc().name();

        List<ProcessStepDoc> processStepDocs = messageListenerDocRepository.findByDomainProcess(moduleDocId, processName);
        ConsumingStepsPerEvent eventToConsumingStepsMap = buildConsumingStepsPerEvent(processStepDocs);

        Set<StepName> otherProcesses = new HashSet<>();
        for(ProcessStepDoc processStepDoc : processStepDocs) {
            List<ToStep> currentStepToSteps = new ArrayList<>();

            List<StepName> toInternals = eventToConsumingStepsMap.locateToInternals(processStepDoc);
            currentStepToSteps.addAll(toDirectSteps(toInternals));

            Set<ToStep> toExternals = locateToExternals(processStepDoc);
            stepsBuilder.merge(toExternalStepsMap(toExternals));
            currentStepToSteps.addAll(toExternals);

            List<ToStep> toDomainProcesses = locateToDomainProcesses(domainProcessDoc, processStepDoc);
            otherProcesses.addAll(toDomainProcesses.stream().map(ToStep::name).collect(toList()));
            stepsBuilder.merge(toExternalStepsMap(toDomainProcesses));
            currentStepToSteps.addAll(toDomainProcesses);

            ComponentDoc processStepComponentDoc = processStepDoc.attributes().moduleComponentDoc().value().componentDoc();
            StepName currentStepName = new StepName(processStepDoc.attributes().moduleComponentDoc().value().componentDoc().name());
            Step currentStep = new Step.Builder()
                    .componentDoc(processStepComponentDoc)
                    .tos(currentStepToSteps)
                    .build();
            stepsBuilder.add(currentStep);

            ToStep toCurrentStep = directStep(currentStepName);

            List<StepName> fromExternals = locateFromExternals(processStepDoc);
            stepsBuilder.merge(fromExternalStepsMap(fromExternals, toCurrentStep));

            List<StepName> fromDomainProcesses = fromDomainProcesses(domainProcessDoc, processStepDoc);
            otherProcesses.addAll(fromDomainProcesses);
            stepsBuilder.merge(fromExternalStepsMap(fromDomainProcesses, toCurrentStep));
        }

        Map<StepName, Step> interprocessSteps = buildInterprocessSteps(moduleDocId, otherProcesses);
        stepsBuilder.merge(interprocessSteps);

        return stepsBuilder.build();
    }

    private List<StepName> locateFromExternals(ProcessStepDoc processStepDoc) {
        return processStepDoc.attributes().fromExternals().value().stream().map(StepName::new).collect(toList());
    }

    private Map<StepName, Step> fromExternalStepsMap(List<StepName> fromExternals, ToStep toCurrentStep) {
        Map<StepName, Step> fromExternalSteps = new HashMap<>();
        for(StepName fromExternal : fromExternals) {
            var fromExternalStep = new Step.Builder()
                    .componentDoc(new ComponentDoc.Builder()
                            .name(fromExternal.stringValue())
                            .description("")
                            .build())
                    .external(true)
                    .to(toCurrentStep)
                    .build();
            fromExternalSteps.put(fromExternalStep.stepName(), fromExternalStep);
        }
        return fromExternalSteps;
    }

    private Map<StepName, Step> toExternalStepsMap(Collection<ToStep> externalStepsNames) {
        Map<StepName, Step> steps = new HashMap<>();
        for(ToStep externalToStep : externalStepsNames) {
            StepName externalStepName = externalToStep.name();
            steps.computeIfAbsent(externalStepName, key -> new Step.Builder()
                    .componentDoc(new ComponentDoc.Builder()
                            .name(externalStepName.stringValue())
                            .description("")
                            .build())
                    .external(true)
                    .build());
        }
        return steps;
    }

    private ProcessStepDocRepository messageListenerDocRepository;

    private ConsumingStepsPerEvent buildConsumingStepsPerEvent(List<ProcessStepDoc> processStepDocs) {
        var builder = new ConsumingStepsPerEvent.Builder();
        for(ProcessStepDoc processStepDoc : processStepDocs) {
            builder.withProcessStepDoc(processStepDoc);
        }
        return builder.build();
    }

    private List<ToStep> toDirectSteps(Collection<StepName> tos) {
        List<ToStep> toSteps = new ArrayList<>();
        for(StepName to : tos) {
            toSteps.add(directStep(to));
        }
        return toSteps;
    }

    private ToStep directStep(StepName to) {
        return new ToStep.Builder()
                .name(to)
                .directly(true)
                .build();
    }

    private Set<ToStep> locateToExternals(ProcessStepDoc processStepDoc) {
        Set<ToStep> toExternals = new HashSet<>();
        toExternals.addAll(processStepDoc.attributes().toExternals().value().stream().map(StepName::new).map(this::directStep).collect(toList()));
        for(Entry<NameRequired, List<String>> entry : processStepDoc.attributes().toExternalsByEvent().entrySet()) {
            boolean required = entry.getKey().required();
            toExternals.addAll(entry.getValue().stream().map(name -> toStep(name, required)).collect(toList()));
        }
        return toExternals;
    }

    private ToStep toStep(String name, boolean required) {
        return new ToStep.Builder()
                .name(new StepName(name))
                .directly(required)
                .build();
    }

    private List<ToStep> locateToDomainProcesses(DomainProcessDoc domainProcessDoc, ProcessStepDoc processStepDoc) {
        Set<NameRequired> producedEvents = processStepDoc.attributes().producedEvents().value();
        String domainProcessName = domainProcessDoc.attributes().moduleComponentDoc().value().componentDoc().name();
        ModuleComponentDoc moduleComponentDoc = domainProcessDoc.attributes().moduleComponentDoc().value();
        ModuleDocId moduleDocId = moduleComponentDoc.moduleDocId();
        Set<ToStep> toDomainProcesses = new HashSet<>();
        for(NameRequired producedEvent : producedEvents) {
            for(ProcessStepDoc stepDoc : messageListenerDocRepository.findConsuming(moduleDocId, producedEvent.name())) {
                Set<String> processNames = stepDoc.attributes().processNames().value();
                for(String processName : processNames) {
                    if(!processName.equals(domainProcessName)) {
                        toDomainProcesses.add(new ToStep.Builder()
                                .name(new StepName(processName))
                                .directly(producedEvent.required())
                                .build());
                    }
                }
            }
        }
        return toDomainProcesses.stream().collect(toList());
    }

    private List<StepName> fromDomainProcesses(DomainProcessDoc domainProcessDoc, ProcessStepDoc processStepDoc) {
        Optional<StepMethodSignature> stepMethodSignature = processStepDoc.attributes().stepMethodSignature().value();
        Optional<String> consumedEvent = Optional.empty();
        if(stepMethodSignature.isPresent()) {
            consumedEvent = stepMethodSignature.get().consumedEventName();
        }
        String domainProcessName = domainProcessDoc.attributes().moduleComponentDoc().value().componentDoc().name();
        Set<String> otherDomainProcesses = new HashSet<>();
        if(consumedEvent.isPresent()) {
            ModuleComponentDoc moduleComponentDoc = domainProcessDoc.attributes().moduleComponentDoc().value();
            ModuleDocId moduleDocId = moduleComponentDoc.moduleDocId();
            List<ProcessStepDoc> stepsProducingEvent = messageListenerDocRepository.findProducing(moduleDocId, consumedEvent.get());
            for(ProcessStepDoc stepDoc : stepsProducingEvent) {
                Set<String> processNames = stepDoc.attributes().processNames().value();
                for(String processName : processNames) {
                    if(!processName.equals(domainProcessName)) {
                        otherDomainProcesses.add(processName);
                    }
                }
            }
        }
        return otherDomainProcesses.stream()
                .map(StepName::new)
                .collect(toList());
    }

    private Map<StepName, Step> buildInterprocessSteps(ModuleDocId moduleDocId, Set<StepName> otherProcesses) {
        Map<StepName, Step> interprocessSteps = new HashMap<>();
        List<StepName> otherProcessesList = new ArrayList<>(otherProcesses);
        Map<StepName, Set<String>> producedEventsPerProcess = new HashMap<>();
        Map<StepName, Set<String>> consumedEventsPerProcess = new HashMap<>();
        for(int i = 0; i < otherProcessesList.size(); ++i) {
            StepName processName1 = otherProcessesList.get(i);
            Set<String> producedEventsOf1 = producedEventsPerProcess.computeIfAbsent(processName1, name -> producedEventsOfProcess(moduleDocId, name));
            Set<String> consumedEventsOf1 = consumedEventsPerProcess.computeIfAbsent(processName1, name -> consumedEventsOfProcess(moduleDocId, name));
            for(int j = i + 1; j < otherProcessesList.size(); ++j) {
                StepName processName2 = otherProcessesList.get(j);
                Set<String> producedEventsOf2 = producedEventsPerProcess.computeIfAbsent(processName2, name -> producedEventsOfProcess(moduleDocId, name));
                Set<String> consumedEventsOf2 = consumedEventsPerProcess.computeIfAbsent(processName2, name -> consumedEventsOfProcess(moduleDocId, name));

                Set<String> producedBy1AndConsumedBy2 = intersect(producedEventsOf1, consumedEventsOf2);
                if(!producedBy1AndConsumedBy2.isEmpty()) {
                    interprocessSteps.put(processName1, new Step.Builder()
                            .componentDoc(new ComponentDoc.Builder()
                                    .name(processName1.stringValue())
                                    .description("")
                                    .build())
                            .to(directStep(processName2))
                            .build());
                }

                Set<String> producedBy2AndConsumedBy1 = intersect(producedEventsOf2, consumedEventsOf1);
                if(!producedBy2AndConsumedBy1.isEmpty()) {
                    interprocessSteps.put(processName2, new Step.Builder()
                            .componentDoc(new ComponentDoc.Builder()
                                    .name(processName2.stringValue())
                                    .description("")
                                    .build())
                            .to(directStep(processName1))
                            .build());
                }
            }
        }
        return interprocessSteps;
    }

    private Set<String> producedEventsOfProcess(ModuleDocId moduleDocId, StepName processName) {
        Set<String> producedEvents = new HashSet<>();
        List<ProcessStepDoc> processStepDocs = messageListenerDocRepository.findByDomainProcess(moduleDocId, processName.stringValue());
        for(ProcessStepDoc stepDoc : processStepDocs) {
            producedEvents.addAll(stepDoc.attributes().producedEvents().value().stream().map(NameRequired::name).collect(toList()));
        }
        return producedEvents;
    }

    private Set<String> consumedEventsOfProcess(ModuleDocId moduleDocId, StepName processName) {
        Set<String> consumedEvents = new HashSet<>();
        List<ProcessStepDoc> processStepDocs = messageListenerDocRepository.findByDomainProcess(moduleDocId, processName.stringValue());
        for(ProcessStepDoc stepDoc : processStepDocs) {
            Optional<StepMethodSignature> signature = stepDoc.attributes().stepMethodSignature().value();
            if(signature.isPresent()) {
                Optional<String> consumedEvent = signature.get().consumedEventName();
                if(consumedEvent.isPresent()) {
                    consumedEvents.add(consumedEvent.get());
                }
            }
        }
        return consumedEvents;
    }

    private Set<String> intersect(Set<String> set1, Set<String> set2) {
        Set<String> intersection = new HashSet<>(set1);
        intersection.retainAll(set2);
        return intersection;
    }
}
