package org.example.restservice;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT)
public class ConflictEx extends RuntimeException{
    public ConflictEx() {
        super("You can't hire this person");
    }
    public ConflictEx(String message) {
        super(message);
    }
}