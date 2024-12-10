package com.devcrew.usermicroservice.controller;

import com.devcrew.usermicroservice.dto.AuthResponse;
import com.devcrew.usermicroservice.dto.LoginRequest;
import com.devcrew.usermicroservice.dto.RegisterRequest;
import com.devcrew.usermicroservice.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

/**
 * AuthController is a class that handles the authentication requests.
 * The login endpoint is used to authenticate the user and return a JWT token.
 * The register endpoint is used to register a new user.
 * Both endpoints return an AuthResponse object.
 * The AuthResponse object contains the JWT token.
 * The AuthResponse object is serialized to JSON and returned to the client.
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    //Delete it later
    @Value("${internal.api.key}")
    private String internalApiKey;

    /**
     * The authService field is used to authenticate the user and register a new user.
     */
    private final AuthService authService;

    //Delete it later
    private final RestTemplate restTemplate;

    /**
     * The login method is used to authenticate the user.
     * It takes a LoginRequest object as a parameter.
     * The LoginRequest object contains the user's email and password.
     * The login method calls the login method of the authService field.
     *
     * @param request The LoginRequest object that contains the user's identifier and password.
     * @return The ResponseEntity object that contains the AuthResponse object, which contains the JWT token.
     */
    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    /**
     * The register method is used to register a new user.
     * It takes a RegisterRequest object as a parameter.
     * The RegisterRequest object contains the user's email, password, and name.
     * The register method calls the register method of the authService field.
     *
     * @param request The RegisterRequest object that contains the user's email, password, and name.
     * @return The ResponseEntity object that contains the AuthResponse object, which contains the JWT token.
     */
    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody RegisterRequest request) {
        AuthResponse response = authService.register(request);
        return ResponseEntity.ok(response);
    }

    //Delete it later
    @GetMapping("/test")
    public void test() {
        String json = """
                {
                     "actionId": {
                         "id": 1,
                         "name": "action1"
                     },
                     "moduleId": {
                         "id": 1,
                         "name": "module1"
                     },
                     "entityId": {
                         "id": 1,
                         "name": "entity1"
                     },
                     "userId": 1,
                     "creationDate": "2024-01-01",
                     "description": "description",
                     "jsonBefore": "{}",
                     "jsonAfter": "{Hello: World}"
                 }
                """;
        HttpHeaders headers = new HttpHeaders();
        headers.set("Internal-Api-Key", internalApiKey);
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(json, headers);
        // This changes with the ip, fix it later
        String url = "http://192.168.10.14:8079/log/save-log";
        restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
    }
}