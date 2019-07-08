package poussecafe.util;

import org.junit.Test;
import poussecafe.exception.PousseCafeException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

public class MethodInvokerTest {

    @Test
    public void invokerRethrows() throws ReflectiveOperationException {
        givenInvokerRethrowing();
        whenInvokingMethodThrowing();
        thenExceptionIsRethrown();
    }

    private void givenInvokerRethrowing() throws ReflectiveOperationException {
        invoker = new MethodInvoker.Builder()
                .method(getClass().getMethod("thrower"))
                .target(this)
                .rethrow(RuntimeException.class)
                .build();
    }

    private MethodInvoker invoker;

    public void thrower() {
        throw new RuntimeException();
    }

    private void whenInvokingMethodThrowing() {
        try {
            invoker.invoke();
        } catch (Exception e) {
            exception = e;
        }
    }

    private Exception exception;

    private void thenExceptionIsRethrown() {
        assertThat(exception, notNullValue());
        assertThat(exception.getClass(), equalTo(RuntimeException.class));
    }

    @Test
    public void invokerDoesNotRethrow() throws ReflectiveOperationException {
        givenInvokerNotRethrowing();
        whenInvokingMethodThrowing();
        thenExceptionIsNotRethrown();
    }

    private void givenInvokerNotRethrowing() throws ReflectiveOperationException {
        invoker = new MethodInvoker.Builder()
                .method(getClass().getMethod("notRethrownThrower"))
                .target(this)
                .rethrow(RuntimeException.class)
                .build();
    }

    public void notRethrownThrower() {
        throw new PousseCafeException();
    }

    private void thenExceptionIsNotRethrown() {
        assertThat(exception, notNullValue());
        assertThat(exception.getClass(), equalTo(PousseCafeException.class));
    }
}
