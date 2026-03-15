package com.cts.eventsphere.exception;

import com.cts.eventsphere.dto.shared.GenericErrorResponse;
import com.cts.eventsphere.dto.shared.GenericResponse;
import com.cts.eventsphere.exception.booking.BookingNotFoundException;
import com.cts.eventsphere.exception.event.EventNotFoundException;
import com.cts.eventsphere.exception.finance.BudgetNotFoundException;
import com.cts.eventsphere.exception.finance.ExpenseNotFoundException;
import com.cts.eventsphere.exception.finance.PaymentNotFoundException;
import com.cts.eventsphere.exception.registration.RegistrationAlreadyExistsException;
import com.cts.eventsphere.exception.registration.RegistrationNotFoundException;
import com.cts.eventsphere.exception.schedule.ScheduleNotFoundException;
import com.cts.eventsphere.exception.ticket.TicketAlreadyExistsException;
import com.cts.eventsphere.exception.ticket.TicketNotFoundException;

import com.cts.eventsphere.exception.user.EmailAlreadyExistsException;
import com.cts.eventsphere.exception.user.InvalidPasswordException;
import com.cts.eventsphere.exception.user.UserAlreadyExistsException;
import com.cts.eventsphere.exception.user.UserNotFoundException;
import lombok.extern.slf4j.Slf4j;

import org.jspecify.annotations.Nullable;
import com.cts.eventsphere.exception.resource.InsufficientResourceException;
import com.cts.eventsphere.exception.resource.ResourceAlreadyExistsException;
import com.cts.eventsphere.exception.resource.ResourceDuplicateAllocationException;
import com.cts.eventsphere.exception.resource.ResourceNotFoundException;
import com.cts.eventsphere.exception.venue.VenueNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * Global Exception Handler for validation and custom exceptions.
 *
 * <p>This class provides a centralized way to handle exceptions thrown
 * by REST controllers. Instead of writing try-catch blocks in every
 * controller, all errors are intercepted here and converted into
 * meaningful HTTP responses.</p>
 *
 * <p>Typical use cases include:</p>
 * <ul>
 *   <li>Handling validation errors (e.g., invalid request data).</li>
 *   <li>Mapping custom exceptions to proper HTTP status codes.</li>
 *   <li>Returning consistent error messages in JSON format.</li>
 * </ul>
 *
 * <p>By using {@code @ControllerAdvice} and {@code @ExceptionHandler},
 * this class ensures that clients always receive clear and uniform
 * error responses, improving both usability and maintainability.</p>
 *
 * @author 2480081
 * @version 1.0
 * @since 09-03-2026
 */


@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    /**
     * Handles validation exceptions thrown when request data fails
     * to meet defined constraints (e.g., @Valid annotations).
     *
     * <p>This method captures {@link MethodArgumentNotValidException},
     * extracts field-level error messages, and returns them as a
     * key-value map where the field name is the key and the validation
     * message is the value.</p>
     *
     * @param ex the exception containing details about validation errors,
     *           including the fields and their corresponding messages
     * @return a ResponseEntity containing a map of field names to error
     *         messages, with HTTP status {@code BAD_REQUEST} (400)
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,String>> handleValidationException(
            MethodArgumentNotValidException ex){
        Map<String,String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(
                e -> errors.put(e.getField(),e.getDefaultMessage())
        );
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles cases where an expense cannot be found.
     *
     * <p>This method catches {@link ExpenseNotFoundException} thrown by
     * the application and returns a user-friendly error message with
     * HTTP status 404 (NOT_FOUND).</p>
     *
     * @param e the exception containing details about the missing expense
     * @return a ResponseEntity with the exception message and
     *         HTTP status NOT_FOUND
     */
    @ExceptionHandler(ExpenseNotFoundException.class)
    public ResponseEntity<GenericErrorResponse> handleExpenseNotFound(ExpenseNotFoundException e){
        return new ResponseEntity<>(new GenericErrorResponse(e.getMessage()),HttpStatus.NOT_FOUND);
    }

    /**
     * Handles cases where a budget cannot be found.
     *
     * <p>This method catches {@link BudgetNotFoundException} thrown by
     * the application and returns a user-friendly error message with
     * HTTP status 404 (NOT_FOUND).</p>
     *
     * @param e the exception containing details about the missing budget
     * @return a ResponseEntity with the exception message and
     *         HTTP status NOT_FOUND
     */
    @ExceptionHandler(BudgetNotFoundException.class)
    public ResponseEntity<GenericErrorResponse> handleBudgetNotFound(BudgetNotFoundException e){
        return new ResponseEntity<>(new GenericErrorResponse(e.getMessage()),HttpStatus.NOT_FOUND);
    }

    /**
     * Handles cases where a payment cannot be found.
     *
     * <p>This method catches {@link PaymentNotFoundException} thrown by
     * the application and returns a user-friendly error message with
     * HTTP status 404 (NOT_FOUND).</p>
     *
     * @param e the exception containing details about the missing payment
     * @return a ResponseEntity with the exception message and
     *         HTTP status NOT_FOUND
     */
    @ExceptionHandler(PaymentNotFoundException.class)
    public ResponseEntity<GenericErrorResponse> handlePaymentNotFound(PaymentNotFoundException e){
        return new ResponseEntity<>(new GenericErrorResponse(e.getMessage()),HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TicketAlreadyExistsException.class)
    public ResponseEntity<GenericErrorResponse> ticketAlreadyExistsException(TicketAlreadyExistsException e){
        return new ResponseEntity<>(new GenericErrorResponse(e.getMessage()),HttpStatus.CONFLICT);
    }

    @ExceptionHandler(VenueNotFoundException.class)
    public ResponseEntity<String> handleVenueNotFoundException(VenueNotFoundException e){
        return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFound(ResourceNotFoundException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InsufficientResourceException.class)
    public ResponseEntity<String> handleInsufficientResource(InsufficientResourceException e) {
               return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<String> handleResourceAlreadyExists(ResourceAlreadyExistsException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ResourceDuplicateAllocationException.class)
    public ResponseEntity<String> handleResourceDuplicateAllocation(ResourceDuplicateAllocationException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(BookingNotFoundException.class)
    public ResponseEntity<String> handleBookingNotFound(BookingNotFoundException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
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

    /**
     * Handles EventNotFoundException by returning a standardized error response.
     * This method is triggered when an event lookup fails and the requested event
     * cannot be found in the system.
     *
     * @param e the EventNotFoundException thrown when the event is missing
     * @return ResponseEntity containing a GenericErrorResponse with a "Event Not Found"
     *         message and HTTP status 404 (NOT_FOUND)
     */
    @ExceptionHandler(EventNotFoundException.class)
    public ResponseEntity<GenericErrorResponse> handleEventNotFoundException(EventNotFoundException e) {
        return new ResponseEntity<>(new GenericErrorResponse("Event Not Found"), HttpStatus.NOT_FOUND);
    }

    /**
     * Handles ScheduleNotFoundException by returning a standardized error response.
     * This method is triggered when a schedule lookup fails and the requested schedule
     * cannot be found in the system.
     *
     * @param e the ScheduleNotFoundException thrown when the schedule is missing
     * @return ResponseEntity containing a GenericErrorResponse with a "Schedule Not Found"
     *         message and HTTP status 404 (NOT_FOUND)
     */
    @ExceptionHandler(ScheduleNotFoundException.class)
    public ResponseEntity<GenericErrorResponse> handleScheduleNotFoundException(ScheduleNotFoundException e) {
        return new ResponseEntity<>(new GenericErrorResponse("Schedule Not Found"), HttpStatus.NOT_FOUND);
    }

}
