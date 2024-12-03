package lm.mr.pmt.controller;

import lm.mr.pmt.controller.exception.*;
import lm.mr.pmt.port.out.jwt.AuthenticationFailedException;
import lombok.Getter;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @Getter
    private static class ErrorResponse {
        private final LocalDateTime timestamp;
        private final int status;
        private final String message;
        private final Map<String, Object> errors;

        public ErrorResponse(HttpStatus status, String message, Map<String, Object> errors) {
            this.timestamp = LocalDateTime.now();
            this.status = status.value();
            this.message = message;
            this.errors = errors;
        }

    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, Object> errors = new HashMap<>();

        errors.putAll(ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        error -> error.getDefaultMessage(),
                        (error1, error2) -> error1
                )));

        ErrorResponse response = new ErrorResponse(
                HttpStatus.BAD_REQUEST,
                "Validation failed",
                errors
        );

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        Map<String, Object> errors = new HashMap<>();

        String message = ex.getMostSpecificCause().getMessage().toLowerCase();
        if (message.contains("email")) {
            errors.put("email", "Email already in use");
        } else if (message.contains("username")) {
            errors.put("username", "Username already in use");
        } else {
            errors.put("error", "Data integrity violation");
        }

        ErrorResponse response = new ErrorResponse(
                HttpStatus.CONFLICT,
                "Resource already exists",
                errors
        );

        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(AuthenticationFailedException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationFailed(AuthenticationFailedException ex) {
        Map<String, Object> errors = new HashMap<>();
        errors.put("authentication", ex.getMessage());

        if (ex.getCause() instanceof BadCredentialsException) {
            errors.put("detail", "Invalid credentials provided");
        }

        ErrorResponse response = new ErrorResponse(
                HttpStatus.UNAUTHORIZED,
                "Authentication failed",
                errors
        );

        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(InvalidTaskDataException.class)
    public ResponseEntity<String> handleInvalidTaskDataException(InvalidTaskDataException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(ProjectNotFoundException.class)
    public ResponseEntity<String> handleProjectNotFoundException(ProjectNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(InvalidRoleException.class)
    public ResponseEntity<String> handleInvalidRole(InvalidRoleException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<String> handleTaskNotFound(TaskNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFoundException(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}
