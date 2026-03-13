package com.cts.eventsphere.exception;

import com.cts.eventsphere.dto.shared.GenericErrorResponse;
import com.cts.eventsphere.dto.shared.GenericResponse;
import com.cts.eventsphere.exception.finance.BudgetNotFoundException;
import com.cts.eventsphere.exception.finance.ExpenseNotFoundException;
import com.cts.eventsphere.exception.finance.PaymentNotFoundException;
import com.cts.eventsphere.exception.registration.RegistrationAlreadyExistsException;
import com.cts.eventsphere.exception.registration.RegistrationNotFoundException;
import com.cts.eventsphere.exception.ticket.TicketAlreadyExistsException;
import com.cts.eventsphere.exception.ticket.TicketNotFoundException;

import com.cts.eventsphere.exception.user.EmailAlreadyExistsException;
import com.cts.eventsphere.exception.user.InvalidPasswordException;
import com.cts.eventsphere.exception.user.UserAlreadyExistsException;
import com.cts.eventsphere.exception.user.UserNotFoundException;
import lombok.extern.slf4j.Slf4j;

import org.jspecify.annotations.Nullable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * Global Exception Handler for Validation errors and Custom made Exceptions
 *
 * @author 2480081
 * @version 1.0
 * @since 09-03-2026
 */

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,String>> handleValidationException(
            MethodArgumentNotValidException ex){
        Map<String,String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(
                e -> errors.put(e.getField(),e.getDefaultMessage())
        );
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ExpenseNotFoundException.class)
    public ResponseEntity<GenericErrorResponse> handleExpenseNotFound(ExpenseNotFoundException e){
        return new ResponseEntity<>(new GenericErrorResponse(e.getMessage()),HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BudgetNotFoundException.class)
    public ResponseEntity<GenericErrorResponse> handleBudgetNotFound(BudgetNotFoundException e){
        return new ResponseEntity<>(new GenericErrorResponse(e.getMessage()),HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PaymentNotFoundException.class)
    public ResponseEntity<GenericErrorResponse> handlePaymentNotFound(PaymentNotFoundException e){
        return new ResponseEntity<>(new GenericErrorResponse(e.getMessage()),HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TicketAlreadyExistsException.class)
    public ResponseEntity<GenericErrorResponse> ticketAlreadyExistsException(TicketAlreadyExistsException e){
        return new ResponseEntity<>(new GenericErrorResponse(e.getMessage()),HttpStatus.CONFLICT);
    }

    @ExceptionHandler(TicketNotFoundException.class)
    public  ResponseEntity<GenericErrorResponse> ticketNotFoundException(TicketNotFoundException e){
        return new ResponseEntity<>(new GenericErrorResponse(e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RegistrationAlreadyExistsException.class)
    public  ResponseEntity<GenericErrorResponse> registrationAlreadyExistsException(RegistrationAlreadyExistsException e){
        return new ResponseEntity<>(new GenericErrorResponse(e.getMessage()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(RegistrationNotFoundException.class)
    public ResponseEntity<GenericErrorResponse> registrationNotFoundException(RegistrationNotFoundException e){
        return new ResponseEntity<>(new GenericErrorResponse("Registration not found"), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<GenericErrorResponse> emailAlreadyExistsException(EmailAlreadyExistsException e){
        return new ResponseEntity<>(new GenericErrorResponse("Email already exists"), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<GenericErrorResponse> invalidPasswordException(InvalidPasswordException e){
        return new ResponseEntity<>(new GenericErrorResponse("Invalid password"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<GenericErrorResponse> userAlreadyExistsException(UserAlreadyExistsException e){
        return new ResponseEntity<>(new GenericErrorResponse("User already exists"), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<GenericErrorResponse> userNotFoundException(UserNotFoundException e){
        return new ResponseEntity<>(new GenericErrorResponse("User not found"), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity< GenericErrorResponse> handleUnexpectedExceptions(Exception ex) {
        String traceId = java.util.UUID.randomUUID().toString();
        log.error("Unhandled exception. traceId={}", traceId, ex);
        GenericErrorResponse body = new GenericErrorResponse(
            "An unexpected error occurred. Please contact support with traceId: " + traceId
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }
}
