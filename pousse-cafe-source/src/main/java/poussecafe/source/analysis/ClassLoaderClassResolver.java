package poussecafe.source.analysis;

public class ClassLoaderClassResolver extends ClassResolver {

    @Override
    protected ResolvedClass loadClass(String name) throws ClassNotFoundException {
        return new ClassLoaderResolvedClass.Builder()
                .classObject(getClass().getClassLoader().loadClass(name))
                .classResolver(this)
                .build();
    }
}
