package poussecafe.source.analysis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class ClassResolver {

    public Optional<Class<?>> loadClass(Name name) {
        try {
            return Optional.of(getClass().getClassLoader().loadClass(name.toString()));
        } catch (ClassNotFoundException e) {
            var newPath = new ArrayList<String>();
            newPath.add(name.getIdentifier().toString());
            return loadInnerClass(name.withoutLastSegment(), newPath);
        }
    }

    public Optional<Class<?>> loadInnerClass(Name rootClassName, List<String> path) {
        try {
            Class<?> rootClass = Class.forName(rootClassName.toString());
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

    private Optional<Class<?>> locateInnerClass(Class<?> rootClass, List<String> path) {
        Class<?> containerClass = rootClass;
        for(String name : path) {
            var classForName = Arrays.stream(containerClass.getDeclaredClasses())
                    .filter(declaredClass -> declaredClass.getSimpleName().equals(name))
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
