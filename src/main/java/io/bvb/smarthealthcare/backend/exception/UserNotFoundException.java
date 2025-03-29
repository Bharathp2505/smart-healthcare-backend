package io.bvb.smarthealthcare.backend.exception;

public abstract class UserNotFoundException extends ApplicationException {
    public UserNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
