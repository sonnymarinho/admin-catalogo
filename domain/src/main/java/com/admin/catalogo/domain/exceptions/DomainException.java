package com.admin.catalogo.domain.exceptions;

import java.util.List;
import com.admin.catalogo.domain.validation.Error;

public class DomainException extends NoStacktraceException {

    protected final List<Error> errors;

    protected DomainException(final String aMessage, final List<Error> anErrors) {
        super(aMessage);
        this.errors = anErrors;
    }

    public static DomainException with(final List<Error> anErrors) {
        return new DomainException(anErrors.get(0).message(), anErrors);
    }

    public static DomainException with(Error error) {
        return new DomainException(error.message(), List.of(error));
    }

    public List<Error> getErrors() {
        return errors;
    }
}
