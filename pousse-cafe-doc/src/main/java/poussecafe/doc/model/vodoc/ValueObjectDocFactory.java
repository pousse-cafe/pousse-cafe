package poussecafe.doc.model.vodoc;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.ProgramElementDoc;
import poussecafe.doc.AnnotationsResolver;
import poussecafe.doc.ClassDocPredicates;
import poussecafe.doc.model.BoundedContextComponentDoc;
import poussecafe.doc.model.ComponentDocFactory;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDocKey;
import poussecafe.domain.DomainException;
import poussecafe.domain.Factory;
import poussecafe.domain.ValueObject;

public class ValueObjectDocFactory extends Factory<ValueObjectDocKey, ValueObjectDoc, ValueObjectDoc.Data> {

    public ValueObjectDoc newValueObjectDoc(BoundedContextDocKey boundedContextDocKey, ProgramElementDoc doc) {
        if(!isValueObjectDoc(doc)) {
            throw new DomainException("Class " + doc.name() + " is not an entity");
        }

        String name = name(doc);
        ValueObjectDocKey key = ValueObjectDocKey.ofClassName(doc.qualifiedName());
        ValueObjectDoc valueObjectDoc = newStorableWithKey(key);
        valueObjectDoc.boundedContextComponentDoc(new BoundedContextComponentDoc.Builder()
                .boundedContextDocKey(boundedContextDocKey)
                .componentDoc(componentDocFactory.buildDoc(name, doc))
                .build());
        return valueObjectDoc;
    }

    private ComponentDocFactory componentDocFactory;

    public static boolean isValueObjectDoc(ProgramElementDoc doc) {
        if(doc instanceof ClassDoc) {
            ClassDoc classDoc = (ClassDoc) doc;
            return ClassDocPredicates.documentsWithSuperinterface(classDoc, ValueObject.class) || classDoc.isEnum();
        } else {
            return AnnotationsResolver.isVo(doc);
        }
    }

    public static String name(ProgramElementDoc doc) {
        if(doc instanceof ClassDoc) {
            ClassDoc classDoc = (ClassDoc) doc;
            return classDoc.simpleTypeName();
        } else {
            return doc.containingClass().name() + " " + capitalizeFirst(doc.name());
        }
    }

    private static String capitalizeFirst(String name) {
        if(Character.isUpperCase(name.charAt(0))) {
            return name;
        } else {
            return Character.toUpperCase(name.charAt(0)) + name.substring(1);
        }
    }
}
