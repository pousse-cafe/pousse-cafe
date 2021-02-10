package poussecafe.source.analysis;

import java.util.List;
import java.util.Optional;

public abstract class ClassResolver {

    public Optional<ResolvedClass> loadClass(ClassName name) {
        return loadClass(SafeClassName.ofRootClass(name));
    }

    public Optional<ResolvedClass> loadClass(SafeClassName name) {
        return loadInnerClass(name.rootClassName(), name.innerClassPath());
    }

    public Optional<ResolvedClass> loadInnerClass(ClassName rootClassName, List<String> path) {
        try {
            ResolvedClass rootClass = loadClass(rootClassName.toString());
            return locateInnerClass(rootClass, path);
        } catch (ClassNotFoundException e) {
            return Optional.empty();
        }
    }

    protected abstract ResolvedClass loadClass(String name) throws ClassNotFoundException;

    public Optional<ResolvedClass> locateInnerClass(ResolvedClass rootClass, List<String> path) {
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
