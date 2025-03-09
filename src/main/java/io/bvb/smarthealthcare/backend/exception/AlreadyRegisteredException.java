package io.bvb.smarthealthcare.backend.exception;

public class AlreadyRegisteredException extends ApplicationException {

    private static final String DEFAULT_MESSAGE = "%s is already registered: %s";

    public AlreadyRegisteredException(String fieldName, String phoneNumber) {
        super(String.format(DEFAULT_MESSAGE, fieldName, phoneNumber));
    }
}
