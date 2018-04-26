package com.vdbanco.viridianDummy.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by marcelo on 07-03-18
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class NoEncontradoRestException extends RuntimeException{

    private ErrorNoEncontrado errorNoEncontrado;        // contiene informacion del error

    public NoEncontradoRestException() {
    }

    public NoEncontradoRestException(String message) {
        super(message);
    }

    public NoEncontradoRestException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public NoEncontradoRestException(ErrorNoEncontrado errorNoEncontrado) {
        this.errorNoEncontrado = errorNoEncontrado;
    }

    public NoEncontradoRestException(String message, ErrorNoEncontrado errorNoEncontrado) {
        super(message);
        this.errorNoEncontrado = errorNoEncontrado;
    }

    public NoEncontradoRestException(String message, Throwable throwable, ErrorNoEncontrado errorNoEncontrado) {
        super(message, throwable);
        this.errorNoEncontrado = errorNoEncontrado;
    }

    public ErrorNoEncontrado getErrorNoEncontrado() {
        return errorNoEncontrado;
    }

    public void setErrorNoEncontrado(ErrorNoEncontrado errorNoEncontrado) {
        this.errorNoEncontrado = errorNoEncontrado;
    }
}
