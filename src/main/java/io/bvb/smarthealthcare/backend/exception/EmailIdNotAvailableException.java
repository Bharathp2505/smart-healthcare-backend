package io.bvb.smarthealthcare.backend.exception;

public class EmailIdNotAvailableException extends ApplicationException {
    private static final String DEFAULT_MESSAGE = "Email id is already registered: %s";

    public EmailIdNotAvailableException(String emailId) {
        super(String.format(DEFAULT_MESSAGE, emailId));
    }
}
