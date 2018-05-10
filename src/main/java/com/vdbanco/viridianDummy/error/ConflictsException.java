package com.vdbanco.viridianDummy.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by marcelo on 05-03-18
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class ConflictsException extends RuntimeException{

    private ErrorDetalle errorDetalle;        // contiene informacion del error

    public ConflictsException() {
    }

    public ConflictsException(String message) {
        super(message);
    }

    public ConflictsException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public ConflictsException(ErrorDetalle errorDetalle) {
        this.errorDetalle = errorDetalle;
    }

    public ConflictsException(String message, ErrorDetalle errorDetalle) {
        super(message);
        this.errorDetalle = errorDetalle;
    }

    public ConflictsException(String message, Throwable throwable, ErrorDetalle errorDetalle) {
        super(message, throwable);
        this.errorDetalle = errorDetalle;
    }

    public ErrorDetalle getErrorDetalle() {
        return errorDetalle;
    }

    public void setErrorDetalle(ErrorDetalle errorDetalle) {
        this.errorDetalle = errorDetalle;
    }
}
