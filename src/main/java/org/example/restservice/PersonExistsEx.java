package org.example.restservice;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT)
public class PersonExistsEx extends RuntimeException {
    public PersonExistsEx() {
        super("This person already exists");
    }
    public PersonExistsEx(int id) {
        super(String.valueOf(id));
    }
}
