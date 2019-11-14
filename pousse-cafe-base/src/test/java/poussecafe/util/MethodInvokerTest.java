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
                .rethrow(SampleRuntimeException.class)
                .build();
    }

    private MethodInvoker invoker;

    public void thrower() {
        throw new SampleRuntimeException();
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
        assertThat(exception.getClass(), equalTo(SampleRuntimeException.class));
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
                .rethrow(SampleRuntimeException.class)
                .build();
    }

    public void notRethrownThrower() {
        throw new PousseCafeException();
    }

    private void thenExceptionIsNotRethrown() {
        assertThat(exception, notNullValue());
        assertThat(exception.getClass(), equalTo(MethodInvokerException.class));
        assertThat(exception.getCause().getClass(), equalTo(PousseCafeException.class));
    }
}
