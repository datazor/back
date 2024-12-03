package lm.mr.pmt.controller.exception;

public class InvalidTaskDataException extends RuntimeException {

    public InvalidTaskDataException(String message) {
        super(message);
    }

    public InvalidTaskDataException(String message, Throwable cause) {
        super(message, cause);
    }
}
