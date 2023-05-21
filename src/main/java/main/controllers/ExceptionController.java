package main.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/*
By using @ControllerAdvice, now the @ExceptionHandler is going to be global for all your controllers,
so all the controllers will pick this up and use it.

So this allows us to centralize our exception handling into what's called @ControllerAdvice.
Using the @ControllerAdvice annotation. This allows us to have a global exception handler and handle
that globally and customize the response.

The key technique here with using controller advice is it gives you control over the response body,
so you can provide a response body if you need to.
*/
@ControllerAdvice
public class ExceptionController {

    /*
    In this I am just simply taking the response entity, giving it a not found.
    If I wanted to provide some type of JSON body for clients to utilize, I could do so.

    I don't think that's typically necessary. But you may have a use case where you do want to
    provide additional details and this would be how you would do it.
    You'd be able to provide a more rich response if you needed to.
    */
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity handleNotFoundException(){
        return ResponseEntity.notFound().build();
    }
}
