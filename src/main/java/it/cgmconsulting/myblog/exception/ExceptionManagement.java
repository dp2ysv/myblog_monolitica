package it.cgmconsulting.myblog.exception;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ConstraintViolation;
import org.hibernate.query.SemanticException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@ControllerAdvice
public class ExceptionManagement {

    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public ResponseEntity<String> methodArgumentTypeMismatchExceptionManagement(MethodArgumentTypeMismatchException ex){
        return new ResponseEntity<String>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ResourceNotFoundException.class})
    public ResponseEntity<String> resourceNotFoundExceptionManagement(ResourceNotFoundException ex){
        return new ResponseEntity<String>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({SemanticException.class})
    public ResponseEntity<String> semanticExceptionManagement(SemanticException ex){
        return new ResponseEntity<String>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<String> illegalArgumentExceptionManagement(IllegalArgumentException ex){
        return new ResponseEntity<String>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({DefaultAuthorityException.class})
    public ResponseEntity<String> defaultAuthorityExceptionManagement(DefaultAuthorityException ex){
        return new ResponseEntity<String>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({OwnerException.class})
    public ResponseEntity<String> ownerExceptionManagement(OwnerException ex){
        return new ResponseEntity<String>(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({UniqueConstraintViolationException.class})
    public ResponseEntity<String> uniqueConstraintViolationExceptionManagement(UniqueConstraintViolationException ex){
        return new ResponseEntity<String>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({EmptyListException.class})
    public ResponseEntity<String> emptyListExceptionManagement(EmptyListException ex){
        return new ResponseEntity<String>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<String> constraintViolationExceptionManagement(ConstraintViolationException ex) {
        Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();
        List<String> errors = violations.stream()
                .map(e -> {
                    return e.getMessage();
                }).collect(Collectors.toList());

        return new ResponseEntity(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<String> handleArgumentNotValidValue(MethodArgumentNotValidException ex) {

        BindingResult bindingResults = ex.getBindingResult();
        List<String> errors = bindingResults
                .getFieldErrors()
                .stream().map(e -> {
                    return e.getField()+": "+e.getDefaultMessage();
                })
                .collect(Collectors.toList());

        return new ResponseEntity<String>(errors.toString(), HttpStatus.BAD_REQUEST);
    }


}
