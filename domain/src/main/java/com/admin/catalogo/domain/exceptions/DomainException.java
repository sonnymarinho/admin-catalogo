package com.admin.catalogo.domain.exceptions;

import java.util.List;
import com.admin.catalogo.domain.validation.Error;

public class DomainException extends NoStacktraceException {

    private final List<Error> errors;

    private DomainException(final String aMessage, final List<Error> anErrors) {
        super(aMessage);
        this.errors = anErrors;
    }

    public static DomainException with(final List<Error> anErrors) {
        return new DomainException("", anErrors);
    }

    public static DomainException with(Error error) {
        return new DomainException("", List.of(error));
    }

    public List<Error> getErrors() {
        return errors;
    }
}
