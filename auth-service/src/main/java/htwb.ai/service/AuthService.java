package htwb.ai.service;

import htwb.ai.model.AuthRequest;

import htwb.ai.model.User;
import htwb.ai.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final JwtUtil jwt;

    @Autowired
    public AuthService(final JwtUtil jwt) {
        this.jwt = jwt;
    }

    public String register(AuthRequest authRequest) {

        authRequest.setPassword(BCrypt.hashpw(authRequest.getPassword(), BCrypt.gensalt()));
        User user = User.builder().id(authRequest.getUserId()).password(authRequest.getPassword()).build();
        String token = jwt.generate(user, "ACCESS");
        return token;
    }
}
