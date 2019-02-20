package com.gmail.onishchenko.oleksii.agile.controller;

import com.gmail.onishchenko.oleksii.agile.exception.AgileBoardException;
import com.gmail.onishchenko.oleksii.agile.exception.CardNotFoundException;
import com.gmail.onishchenko.oleksii.agile.exception.UserAlreadyExistsException;
import com.gmail.onishchenko.oleksii.agile.exception.UserNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ErrorController {
    private static final Logger log = LogManager.getLogger(ErrorController.class);

    @ExceptionHandler(value = {AgileBoardException.class,
            UserAlreadyExistsException.class, UserNotFoundException.class,
            CardNotFoundException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public void generalException(Exception e) {
        log.warn(e);
    }
}
