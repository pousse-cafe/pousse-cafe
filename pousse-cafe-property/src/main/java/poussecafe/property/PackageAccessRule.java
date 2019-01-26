package poussecafe.property;

import java.util.Objects;

public class PackageAccessRule implements AllowRule {

    public PackageAccessRule(Package allowedPackage) {
        Objects.requireNonNull(allowedPackage);
        this.allowedPackage = allowedPackage;
    }

    private Package allowedPackage;

    @Override
    public boolean allow(Object accessor) {
        return allowedPackage.equals(accessor.getClass().getPackage());
    }

}
