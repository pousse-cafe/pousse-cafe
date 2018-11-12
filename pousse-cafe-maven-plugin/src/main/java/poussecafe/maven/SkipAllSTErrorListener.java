package poussecafe.maven;

import org.stringtemplate.v4.STErrorListener;
import org.stringtemplate.v4.misc.STMessage;

public class SkipAllSTErrorListener implements STErrorListener {

    @Override
    public void compileTimeError(STMessage msg) {
        // Skip
    }

    @Override
    public void runTimeError(STMessage msg) {
        // Skip
    }

    @Override
    public void IOError(STMessage msg) {
        // Skip
    }

    @Override
    public void internalError(STMessage msg) {
        // Skip
    }
}
