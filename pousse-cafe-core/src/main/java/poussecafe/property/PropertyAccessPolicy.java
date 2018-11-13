package poussecafe.property;

import java.util.ArrayList;
import java.util.List;

class PropertyAccessPolicy {

    public void addWriteRule(AllowRule rule) {
        writeRules.add(rule);
    }

    private List<AllowRule> writeRules = new ArrayList<>();

    public boolean allowsWriteFor(Object accessor) {
        return allowsFor(accessor, writeRules, DEFAULT_ALLOW_WRITE);
    }

    private static final boolean DEFAULT_ALLOW_WRITE = false;

    private boolean allowsFor(Object accessor, List<AllowRule> accessRules, boolean defaultAllow) {
        if(accessRules.isEmpty()) {
            return defaultAllow;
        } else {
            for(AllowRule rule : accessRules) {
                if(rule.allow(accessor)) {
                    return true;
                }
            }
            return false;
        }
    }
}
