package de.paulwolf.passwordmanager.information;

public class WrongPasswordException extends Exception {

    private static final long serialVersionUID = 1L;

    public WrongPasswordException(String errorMessage) {
        super(errorMessage);
    }
}
