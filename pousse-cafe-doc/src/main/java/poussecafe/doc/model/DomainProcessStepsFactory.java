package poussecafe.doc.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDocKey;
import poussecafe.doc.model.domainprocessdoc.DomainProcessDoc;
import poussecafe.doc.model.domainprocessdoc.Step;
import poussecafe.doc.model.domainprocessdoc.StepName;
import poussecafe.doc.model.domainprocessdoc.ToStep;
import poussecafe.doc.model.processstepdoc.ProcessStepDoc;
import poussecafe.doc.model.processstepdoc.ProcessStepDocRepository;
import poussecafe.doc.model.processstepdoc.StepMethodSignature;
import poussecafe.domain.Service;

import static java.util.stream.Collectors.toList;

public class DomainProcessStepsFactory implements Service {

    public DomainProcessSteps buildDomainProcessSteps(DomainProcessDoc domainProcessDoc) {
        HashMap<StepName, Step> steps = new HashMap<>();

        BoundedContextComponentDoc boundedContextComponentDoc = domainProcessDoc.attributes().boundedContextComponentDoc().value();
        BoundedContextDocKey boundedContextDocKey = boundedContextComponentDoc.boundedContextDocKey();
        String processName = boundedContextComponentDoc.componentDoc().name();

        HashMap<String, List<String>> eventToStep = new HashMap<>();
        List<ProcessStepDoc> processStepDocs = messageListenerDocRepository.findByDomainProcess(boundedContextDocKey, processName);
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
                signatures.add(processStepDoc.attributes().boundedContextComponentDoc().value().componentDoc().name());
            }
        }

        for(ProcessStepDoc processStepDoc : processStepDocs) {
            List<ToStep> toSteps = new ArrayList<>();
            List<StepName> tos = locateTos(processStepDoc, eventToStep);
            toSteps.addAll(toDirectSteps(tos));

            StepName currentStepName = new StepName(processStepDoc.attributes().boundedContextComponentDoc().value().componentDoc().name());
            List<StepName> toExternals = processStepDoc.attributes().toExternals().value().stream().map(StepName::new).collect(toList());
            for(StepName toExternal : toExternals) {
                Step toExternalStep = steps.get(toExternal);
                if(toExternalStep == null) {
                    toExternalStep = new Step.Builder()
                            .componentDoc(new ComponentDoc.Builder()
                                    .name(toExternal.getValue())
                                    .description("")
                                    .build())
                            .external(true)
                            .build();
                    steps.put(toExternal, toExternalStep);
                }
            }
            toSteps.addAll(toDirectSteps(toExternals));

            ComponentDoc messageListenerComponentDoc = processStepDoc.attributes().boundedContextComponentDoc().value().componentDoc();
            steps.put(currentStepName, new Step.Builder()
                    .componentDoc(messageListenerComponentDoc)
                    .tos(toSteps)
                    .build());

            List<StepName> fromExternals = processStepDoc.attributes().fromExternals().value().stream().map(StepName::new).collect(toList());
            for(StepName fromExternal : fromExternals) {
                ToStep additionalToStep = new ToStep.Builder()
                        .name(currentStepName)
                        .directly(true)
                        .build();
                Step fromExternalStep = steps.get(fromExternal);
                if(fromExternalStep == null) {
                    fromExternalStep = new Step.Builder()
                            .componentDoc(new ComponentDoc.Builder()
                                    .name(fromExternal.getValue())
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

    private List<ToStep> toDirectSteps(List<StepName> tos) {
        List<ToStep> toSteps = new ArrayList<>();
        for(StepName to : tos) {
            toSteps.add(new ToStep.Builder()
                    .name(to)
                    .directly(true)
                    .build());
        }
        return toSteps;
    }
}
