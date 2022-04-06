package de.paulwolf.passwordmanager.information;

import java.io.Serial;

public class WrongPasswordException extends Exception {

    @Serial
    private static final long serialVersionUID = 1L;

    public WrongPasswordException(String errorMessage) {
        super(errorMessage);
    }
}
