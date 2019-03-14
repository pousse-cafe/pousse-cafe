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
import poussecafe.doc.model.messagelistenerdoc.MessageListenerDoc;
import poussecafe.doc.model.messagelistenerdoc.MessageListenerDocRepository;
import poussecafe.doc.model.messagelistenerdoc.StepMethodSignature;
import poussecafe.domain.Service;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

public class DomainProcessStepsFactory implements Service {

    public DomainProcessSteps buildDomainProcessSteps(DomainProcessDoc domainProcessDoc) {
        HashMap<StepName, Step> steps = new HashMap<>();

        HashMap<String, List<StepMethodSignature>> eventToStep = new HashMap<>();
        for(StepMethodSignature stepMethodSignature : domainProcessDoc.attributes().steps()) {
            Optional<String> consumedEventName = stepMethodSignature.consumedEventName();
            if(consumedEventName.isPresent()) {
                String presentConsumedEventName = consumedEventName.get();
                List<StepMethodSignature> signatures = eventToStep.get(presentConsumedEventName);
                if(signatures == null) {
                    signatures = new ArrayList<>();
                    eventToStep.put(presentConsumedEventName, signatures);
                }
                signatures.add(stepMethodSignature);
            }
        }

        for(StepMethodSignature stepMethodSignature : domainProcessDoc.attributes().steps()) {
            MessageListenerDoc messageListenerDoc = locateStepDoc(
                    domainProcessDoc.attributes().boundedContextComponentDoc().value().boundedContextDocKey(),
                    stepMethodSignature);

            List<ToStep> toSteps = new ArrayList<>();
            List<StepName> tos = locateTos(messageListenerDoc, eventToStep);
            toSteps.addAll(toDirectSteps(tos));

            StepName currentStepName = new StepName(stepMethodSignature);
            List<StepName> toExternals = domainProcessDoc.attributes().toExternals().get(currentStepName).orElse(emptyList());
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

            ComponentDoc messageListenerComponentDoc = messageListenerDoc.attributes().boundedContextComponentDoc().value().componentDoc();
            steps.put(currentStepName, new Step.Builder()
                    .componentDoc(messageListenerComponentDoc)
                    .tos(toSteps)
                    .build());

            List<StepName> fromExternals = domainProcessDoc.attributes().fromExternals().get(currentStepName).orElse(emptyList());
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

    private List<StepName> locateTos(MessageListenerDoc stepDoc,
            HashMap<String, List<StepMethodSignature>> eventToStep) {
        List<StepName> tos = new ArrayList<>();
        for(String producedEvent : stepDoc.attributes().producedEvents()) {
            List<StepMethodSignature> signatures = eventToStep.get(producedEvent);
            if(signatures != null) {
                tos.addAll(signatures.stream().map(StepName::new).collect(toList()));
            }
        }
        return tos;
    }

    private MessageListenerDoc locateStepDoc(BoundedContextDocKey boundedContextDocKey,
            StepMethodSignature stepMethodSignature) {
        return messageListenerDocRepository.getByStepMethodSignature(boundedContextDocKey, stepMethodSignature);
    }

    private MessageListenerDocRepository messageListenerDocRepository;

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
