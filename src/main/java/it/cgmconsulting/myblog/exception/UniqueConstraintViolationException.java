package it.cgmconsulting.myblog.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
@Getter
public class UniqueConstraintViolationException extends RuntimeException{

    private final String resourceName;
    private final String fieldName;
    private final Object fieldValue;

    public UniqueConstraintViolationException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s already present with %s: %s", resourceName, fieldName, fieldValue));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }
}
