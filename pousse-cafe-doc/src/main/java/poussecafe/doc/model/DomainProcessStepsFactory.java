package poussecafe.doc.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import poussecafe.doc.model.domainprocessdoc.DomainProcessDoc;
import poussecafe.doc.model.domainprocessdoc.Step;
import poussecafe.doc.model.domainprocessdoc.StepName;
import poussecafe.doc.model.domainprocessdoc.ToStep;
import poussecafe.doc.model.moduledoc.ModuleDocId;
import poussecafe.doc.model.processstepdoc.ProcessStepDoc;
import poussecafe.doc.model.processstepdoc.ProcessStepDocRepository;
import poussecafe.doc.model.processstepdoc.StepMethodSignature;
import poussecafe.domain.Service;

import static java.util.stream.Collectors.toList;

public class DomainProcessStepsFactory implements Service {

    public DomainProcessSteps buildDomainProcessSteps(DomainProcessDoc domainProcessDoc) {
        HashMap<StepName, Step> steps = new HashMap<>();

        ModuleComponentDoc moduleComponentDoc = domainProcessDoc.attributes().moduleComponentDoc().value();
        ModuleDocId moduleDocId = moduleComponentDoc.moduleDocId();
        String processName = moduleComponentDoc.componentDoc().name();

        HashMap<String, List<String>> eventToStep = new HashMap<>();
        List<ProcessStepDoc> processStepDocs = messageListenerDocRepository.findByDomainProcess(moduleDocId, processName);
        for(ProcessStepDoc processStepDoc : processStepDocs) {
            Optional<String> consumedEventName;
            Optional<StepMethodSignature> optionalStepMethodSignature = processStepDoc.attributes().stepMethodSignature().value();
            if(optionalStepMethodSignature.isPresent()) {
                consumedEventName = optionalStepMethodSignature.get().consumedEventName();
            } else {
                consumedEventName = Optional.empty();
            }

            if(consumedEventName.isPresent()) {
                String presentConsumedEventName = consumedEventName.get();
                List<String> signatures = eventToStep.get(presentConsumedEventName);
                if(signatures == null) {
                    signatures = new ArrayList<>();
                    eventToStep.put(presentConsumedEventName, signatures);
                }
                signatures.add(processStepDoc.attributes().moduleComponentDoc().value().componentDoc().name());
            }
        }

        for(ProcessStepDoc processStepDoc : processStepDocs) {
            List<ToStep> toSteps = new ArrayList<>();
            List<StepName> tos = locateTos(processStepDoc, eventToStep);
            toSteps.addAll(toDirectSteps(tos));

            StepName currentStepName = new StepName(processStepDoc.attributes().moduleComponentDoc().value().componentDoc().name());
            Set<StepName> toExternals = new HashSet<>();
            toExternals.addAll(processStepDoc.attributes().toExternals().value().stream().map(StepName::new).collect(toList()));
            for(Entry<String, List<String>> entry : processStepDoc.attributes().toExternalsByEvent().entrySet()) {
                toExternals.addAll(entry.getValue().stream().map(StepName::new).collect(toList()));
            }
            for(StepName toExternal : toExternals) {
                Step toExternalStep = steps.get(toExternal);
                if(toExternalStep == null) {
                    toExternalStep = new Step.Builder()
                            .componentDoc(new ComponentDoc.Builder()
                                    .name(toExternal.stringValue())
                                    .description("")
                                    .build())
                            .external(true)
                            .build();
                    steps.put(toExternal, toExternalStep);
                }
            }
            toSteps.addAll(toDirectSteps(toExternals));

            String domainProcessName = domainProcessDoc.attributes().moduleComponentDoc().value().componentDoc().name();
            List<StepName> toDomainProcesses = otherDomainProcesses(moduleDocId, domainProcessName, processStepDoc.attributes().producedEvents().value());
            for(StepName toDomainProcess : toDomainProcesses) {
                Step toDomainProcessStep = steps.get(toDomainProcess);
                if(toDomainProcessStep == null) {
                    toDomainProcessStep = new Step.Builder()
                            .componentDoc(new ComponentDoc.Builder()
                                    .name(toDomainProcess.stringValue())
                                    .description("")
                                    .build())
                            .external(true)
                            .build();
                    steps.put(toDomainProcess, toDomainProcessStep);
                }
            }
            toSteps.addAll(toDirectSteps(toDomainProcesses));

            ComponentDoc processStepComponentDoc = processStepDoc.attributes().moduleComponentDoc().value().componentDoc();
            steps.put(currentStepName, new Step.Builder()
                    .componentDoc(processStepComponentDoc)
                    .tos(toSteps)
                    .build());

            List<StepName> fromExternals = processStepDoc.attributes().fromExternals().value().stream().map(StepName::new).collect(toList());
            ToStep additionalToStep = directStep(currentStepName);
            for(StepName fromExternal : fromExternals) {
                Step fromExternalStep = steps.get(fromExternal);
                if(fromExternalStep == null) {
                    fromExternalStep = new Step.Builder()
                            .componentDoc(new ComponentDoc.Builder()
                                    .name(fromExternal.stringValue())
                                    .description("")
                                    .build())
                            .external(true)
                            .to(additionalToStep)
                            .build();
                } else {
                    fromExternalStep = new Step.Builder()
                            .step(fromExternalStep)
                            .to(additionalToStep)
                            .build();
                }
                steps.put(fromExternal, fromExternalStep);
            }

            Optional<StepMethodSignature> stepMethodSignature = processStepDoc.attributes().stepMethodSignature().value();
            Optional<String> consumedEvent = Optional.empty();
            if(stepMethodSignature.isPresent()) {
                consumedEvent = stepMethodSignature.get().consumedEventName();
            }
            List<StepName> fromDomainProcesses = fromDomainProcesses(moduleDocId, domainProcessName, consumedEvent);
            for(StepName fromDomainProcess : fromDomainProcesses) {
                Step fromDomainProcessStep = steps.get(fromDomainProcess);
                if(fromDomainProcessStep == null) {
                    fromDomainProcessStep = new Step.Builder()
                            .componentDoc(new ComponentDoc.Builder()
                                    .name(fromDomainProcess.stringValue())
                                    .description("")
                                    .build())
                            .external(true)
                            .to(additionalToStep)
                            .build();
                } else {
                    fromDomainProcessStep = new Step.Builder()
                            .step(fromDomainProcessStep)
                            .to(additionalToStep)
                            .build();
                }
                steps.put(fromDomainProcess, fromDomainProcessStep);
            }
        }

        return new DomainProcessSteps(steps);
    }

    private ProcessStepDocRepository messageListenerDocRepository;

    private List<StepName> locateTos(ProcessStepDoc stepDoc,
            HashMap<String, List<String>> eventToStep) {
        List<StepName> tos = new ArrayList<>();
        for(String producedEvent : stepDoc.attributes().producedEvents()) {
            List<String> signatures = eventToStep.get(producedEvent);
            if(signatures != null) {
                tos.addAll(signatures.stream().map(StepName::new).collect(toList()));
            }
        }
        return tos;
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

    private List<StepName> otherDomainProcesses(ModuleDocId moduleDocId,
            String domainProcessName,
            Set<String> producedEvents) {
        Set<String> otherDomainProcesses = new HashSet<>();
        for(String producedEvent : producedEvents) {
            for(ProcessStepDoc stepDoc : messageListenerDocRepository.findConsuming(moduleDocId, producedEvent)) {
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

    private List<StepName> fromDomainProcesses(ModuleDocId moduleDocId,
            String domainProcessName,
            Optional<String> consumedEvent) {
        Set<String> otherDomainProcesses = new HashSet<>();
        if(consumedEvent.isPresent()) {
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
}
