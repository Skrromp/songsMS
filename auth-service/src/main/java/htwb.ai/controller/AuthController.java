package htwb.ai.controller;

import htwb.ai.model.User;
import htwb.ai.repo.UserRepo;
import htwb.ai.service.AuthService;
import htwb.ai.model.AuthRequest;

import io.jsonwebtoken.Jwt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/auth")
public class AuthController {

    private final AuthService authService;
    private final UserRepo userRepo;


    @Autowired
    private AuthController(final UserRepo userRepo, final AuthService authService) {
        this.userRepo = userRepo;
        this.authService = authService;
    }

    //TODO fix register
    @PostMapping(produces = "text/plain;charset=UTF-8", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<String> register(@RequestBody AuthRequest authRequest) {
        if (validateAuth(authRequest)) {
            return ResponseEntity.ok(authService.register(authRequest));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    private boolean validateAuth(AuthRequest authRequest) {
        User user = userRepo.findUserById(authRequest.getUserId());
        if (user != null) {
            if (user.getPassword().equals(authRequest.getPassword())) {
                return true;
            }
        }
        return false;
    }
}