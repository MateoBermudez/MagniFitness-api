package com.devcrew.usermicroservice.controller;

import com.devcrew.usermicroservice.exception.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * Test controller for throwing specific exceptions.
 * This controller is used to test exception handling in the application.
 */
@ActiveProfiles("test")
@RestController
@RequestMapping("/api/test")
public class TestExceptionController {

    /**
     * Throws an UnauthorizedException.
     * This endpoint is used to test unauthorized exception handling.
     */
    @GetMapping("/unauthorized")
    public void throwUnauthorizedException() {
        throw new UnauthorizedException("Unauthorized access");
    }

    /**
     * Throws a UserAlreadyExistsException.
     * This endpoint is used to test user already exists exception handling.
     */
    @GetMapping("/user-already-exists")
    public void throwUserAlreadyExistsException() {
        throw new UserAlreadyExistsException("User already exists");
    }

    /**
     * Throws a UserDoesNotExistException.
     * This endpoint is used to test user does not exist exception handling.
     */
    @GetMapping("/user-does-not-exist")
    public void throwUserDoesNotExistException() {
        throw new UserDoesNotExistException("User does not exist");
    }

    /**
     * Throws a BadRequestException.
     * This endpoint is used to test bad request exception handling.
     */
    @GetMapping("/bad-request")
    public void throwBadRequestException() {
        throw new BadRequestException("Bad request");
    }

    /**
     * Throws a CustomException.
     * This endpoint is used to test custom exception handling.
     */
    @GetMapping("/custom-exception")
    public void throwCustomException() {
        throw new CustomException("Custom exception occurred");
    }

    /**
     * Throws a RuntimeException.
     * This endpoint is used to test internal server error exception handling.
     */
    @GetMapping("/exception")
    public void throwException() {
        throw new RuntimeException("Internal server error");
    }

    /**
     * Throws an IOException.
     * This endpoint is used to test IO exception handling.
     *
     * @throws IOException if an IO exception occurs
     */
    @GetMapping("/io-exception")
    public void throwIOException() throws IOException {
        throw new IOException("IO exception occurred");
    }

    /**
     * Throws a BadCredentialsException.
     * This endpoint is used to test bad credentials exception handling.
     */
    @GetMapping("/bad-credentials")
    public void throwBadCredentialsException() {
        throw new BadCredentialsException("Bad credentials");
    }
}