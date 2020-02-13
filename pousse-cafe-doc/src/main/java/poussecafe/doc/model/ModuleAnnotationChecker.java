package poussecafe.doc.model;

import java.util.Optional;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import poussecafe.discovery.Module;
import poussecafe.doc.annotations.AnnotationUtils;
import poussecafe.doc.model.moduledoc.ModuleDocId;
import poussecafe.domain.DomainException;
import poussecafe.domain.Service;

public class ModuleAnnotationChecker implements Service {

    public void packageMatchOrThrow(TypeElement processDoc, ModuleDocId moduleDocId) {
        Optional<AnnotationMirror> moduleAnnotation = AnnotationUtils.annotation(processDoc, Module.class);
        if(moduleAnnotation.isPresent()) {
            Optional<AnnotationValue> value = AnnotationUtils.value(moduleAnnotation.get(), "value");
            if(value.isPresent()) {
                Element moduleClass = docletAccess.getTypesUtils().asElement((TypeMirror) value.get().getValue());
                PackageElement moduleClassPackage = (PackageElement) moduleClass.getEnclosingElement();
                PackageElement aggregateRootPackage = (PackageElement) processDoc.getEnclosingElement();
                if(!aggregateRootPackage.getQualifiedName().toString().startsWith(moduleClassPackage.getQualifiedName().toString())) {
                    throw new DomainException("Class " + processDoc.getQualifiedName() + " is in the wrong package");
                }
                if(!moduleClassPackage.getQualifiedName().toString().equals(moduleDocId.stringValue())) {
                    throw new DomainException(processDoc.getQualifiedName() + " is in 2 different modules, mixing package-info and class based module definition? "
                            + moduleClassPackage.getSimpleName().toString() + " <> " + moduleDocId.stringValue());
                }
            }
        }
    }

    private DocletAccess docletAccess;
}
