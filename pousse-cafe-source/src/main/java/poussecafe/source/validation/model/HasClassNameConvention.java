package poussecafe.source.validation.model;

import poussecafe.source.validation.names.DeclaredComponent;

public interface HasClassNameConvention extends DeclaredComponent {

    boolean validClassName();
}
