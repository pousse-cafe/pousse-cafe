package poussecafe.source.analysis;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public abstract class ClassResolver {

    public Optional<ResolvedClass> loadClass(Name name) {
        try {
            return Optional.of(loadClass(name.toString()));
        } catch (ClassNotFoundException e) {
            var newPath = new ArrayList<String>();
            newPath.add(name.getIdentifier().toString());
            return loadInnerClass(name.withoutLastSegment(), newPath);
        }
    }

    protected abstract ResolvedClass loadClass(String name) throws ClassNotFoundException;

    public Optional<ResolvedClass> loadInnerClass(Name rootClassName, List<String> path) {
        try {
            ResolvedClass rootClass = loadClass(rootClassName.toString());
            return locateInnerClass(rootClass, path);
        } catch (ClassNotFoundException e) {
            if(rootClassName.isSimpleName()) {
                return Optional.empty();
            } else {
                var newPath = new LinkedList<>(path);
                newPath.addFirst(rootClassName.getIdentifier().toString());
                return loadInnerClass(rootClassName.withoutLastSegment(), newPath);
            }
        }
    }

    private Optional<ResolvedClass> locateInnerClass(ResolvedClass rootClass, List<String> path) {
        ResolvedClass containerClass = rootClass;
        for(String name : path) {
            var classForName = containerClass.innerClasses().stream()
                    .filter(declaredClass -> declaredClass.name().simple().equals(name))
                    .findFirst();
            if(classForName.isEmpty()) {
                return Optional.empty();
            } else {
                containerClass = classForName.get();
            }
        }
        return Optional.of(containerClass);
    }
}
