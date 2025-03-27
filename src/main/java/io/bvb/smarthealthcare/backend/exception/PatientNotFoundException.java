package io.bvb.smarthealthcare.backend.exception;

public class PatientNotFoundException extends  UserNotFoundException {
    private static final String DEFAULT_MESSAGE = "Patient not found : %s";
    public PatientNotFoundException(Long id) {
        super(String.format(DEFAULT_MESSAGE, id));
    }
}
