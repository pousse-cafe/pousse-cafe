package poussecafe.source.analysis;

import java.util.Optional;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class ClassResolverTest {

    @Test
    public void resolveFullyQualifiedName() {
        givenQualifiedName("poussecafe.domain.AggregateRoot");
        whenResolving();
        thenResolved();
    }

    private void givenQualifiedName(String qualifiedName) {
        this.qualifiedName = new Name(qualifiedName);
    }

    private void whenResolving() {
        resolved = classResolver.loadClass(qualifiedName);
    }

    private void thenResolved() {
        assertTrue(resolved.isPresent());
    }

    private Name qualifiedName;

    private Optional<ResolvedClass> resolved;

    private ClassResolver classResolver = new ClassLoaderClassResolver();
}
