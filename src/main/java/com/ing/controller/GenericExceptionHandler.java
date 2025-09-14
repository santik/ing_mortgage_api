package com.ing.controller;

import com.ing.mortgage.model.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.UUID;

import static com.ing.filter.TraceIdFilter.X_TRACE_ID;

@RestControllerAdvice
@Slf4j
public class GenericExceptionHandler {

    /**
     * Handles all uncaught exceptions and returns a generic error response.
     *
     * @param ex the exception that was thrown
     * @return a ResponseEntity containing an error response with status 500
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAllExceptions(Exception ex) {
        log.error("An error occurred: ", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new ErrorResponse("INTERNAL_SERVER_ERROR",
                        "An unexpected error occurred. Please try again later.",
                        UUID.fromString(MDC.get(X_TRACE_ID))));
    }

    /**
     * Handles validation exceptions and returns a detailed error response.
     *
     * @param ex the MethodArgumentNotValidException thrown during validation
     * @return a ResponseEntity containing an error response with status 400
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        log.error("Validation error: ", ex);
        String message = ex.getBindingResult().getAllErrors().getFirst().getDefaultMessage();
        if (!ex.getBindingResult().getFieldErrors().isEmpty()) {
            String field = ex.getBindingResult().getFieldErrors().getFirst().getField();
            message = field + ": " + message;
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ErrorResponse("INVALID_REQUEST",
                        message,
                        UUID.fromString(MDC.get(X_TRACE_ID))));
    }
}
