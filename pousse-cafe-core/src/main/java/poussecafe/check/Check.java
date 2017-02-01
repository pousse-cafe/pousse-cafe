package poussecafe.check;

import poussecafe.exception.PousseCafeException;

public class Check<T> {

    private CheckSpecification<T> specification;

    public Check(CheckSpecification<T> specification) {
        setSpecification(specification);
    }

    private void setSpecification(CheckSpecification<T> specification) {
        validateSpecification(specification);
        this.specification = specification;
    }

    private static <T> void validateSpecification(CheckSpecification<T> specification) {
        if (specification == null) {
            throw new PousseCafeException("No specification provided");
        }
        if (specification.getPredicate() == null) {
            throw new PousseCafeException("No matcher in given specification");
        }
        if (specification.getMessage() == null) {
            throw new PousseCafeException("No message in given specification");
        }
    }

    public void run() {
        if (!specification.getPredicate().test(specification.getValue())) {
            specification.otherwise();
        }
    }

}
