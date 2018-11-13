package poussecafe.property;

import static poussecafe.check.Checks.checkThatValue;

public class PackageAccessRule implements AllowRule {

    public PackageAccessRule(Package allowedPackage) {
        checkThatValue(allowedPackage).notNull();
        this.allowedPackage = allowedPackage;
    }

    private Package allowedPackage;

    @Override
    public boolean allow(Object accessor) {
        return allowedPackage.equals(accessor.getClass().getPackage());
    }

}
