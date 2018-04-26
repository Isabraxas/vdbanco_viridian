package com.vdbanco.viridianDummy.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by marcelo on 05-03-18
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class NoEncontradoException extends RuntimeException{
    public NoEncontradoException(){
        super();
    }

    public NoEncontradoException(String message){
        super(message);
    }

    public NoEncontradoException(String message, Throwable cause){
        super(message, cause);
    }
}
