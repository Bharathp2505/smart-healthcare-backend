package io.bvb.smarthealthcare.backend.exception;

public class UserNotFoundException extends ApplicationException {
    private static final String ERROR_MESSAGE = "User not found : %s";

    public UserNotFoundException(String username) {
        super(String.format(ERROR_MESSAGE, username));
    }
}
