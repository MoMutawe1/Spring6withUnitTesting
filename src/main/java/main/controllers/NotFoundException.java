package main.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/*
Usually when you're doing RESTFUL APIs, you don't need a lot of complication for it.
You just want the HTTP status has being returned is really the primary thing that you're working with.

This eliminates the need for the @ControllerAdvice annotation so by using this class it's safe to delete
our ExceptionController class without effecting the results of the test cases,
and that is because now the exception handling is being handled by the response status.
So you can define custom exception classes if you need to provide different statuses but here we only
handle (Not Found exception - 404) for our entities.

Handling exceptions becomes much simpler to use. You annotate it once and then when you do have a situation
in your controller classes where the ID was not found, we can throw that exception.
*/

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Value Not Found")
public class NotFoundException extends RuntimeException{
    public NotFoundException() {
    }

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFoundException(Throwable cause) {
        super(cause);
    }

    public NotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
