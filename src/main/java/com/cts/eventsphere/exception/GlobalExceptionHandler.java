package com.cts.eventsphere.exception;

import com.cts.eventsphere.exception.finance.BudgetNotFoundException;
import com.cts.eventsphere.exception.finance.ExpenseNotFoundException;
import com.cts.eventsphere.exception.finance.PaymentNotFoundException;
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
    public ResponseEntity<String> handleExpenseNotFound(ExpenseNotFoundException e){
        return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
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
    public ResponseEntity<String> handleBudgetNotFound(BudgetNotFoundException e){
        return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
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
    public ResponseEntity<String> handlePaymentNotFound(PaymentNotFoundException e){
        return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
    }

}
