package htwb.ai.controller;

import htwb.ai.service.AuthService;
import htwb.ai.model.AuthResponse;
import htwb.ai.model.AuthRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    private AuthController(final AuthService authService){
        this.authService = authService;
    }

    @PostMapping(produces = "text/plain;charset=UTF-8", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<AuthResponse> register(@RequestBody AuthRequest authRequest) {
        return  ResponseEntity.ok(authService.register(authRequest));
    }
}
