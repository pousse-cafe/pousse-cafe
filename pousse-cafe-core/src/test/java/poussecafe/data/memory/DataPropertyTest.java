package poussecafe.data.memory;

import org.junit.Test;

public abstract class DataPropertyTest {

    @Test
    public abstract void gettingAfterSetIsSameValue();

    @Test
    public abstract void gettingAfterNoSetIsDefault();
}
