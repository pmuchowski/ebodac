package org.motechproject.bookingapp.exception;

public class VisitScheduleException extends RuntimeException {

    public VisitScheduleException(String message) {
        super(message);
    }

    public VisitScheduleException(String message, Throwable cause) {
        super(message, cause);
    }
}
