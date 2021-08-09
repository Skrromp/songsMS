package htwb.ai.auth;

import htwb.ai.model.User;

import java.util.ArrayList;
import java.util.List;

public class AuthenticationService {

    private List<Token> tokens;

    private static final AuthenticationService authenticationService = new AuthenticationService();

    private AuthenticationService() {
    }

    public static AuthenticationService getInstance() {
        return authenticationService;
    }

    public Boolean checkToken(String tokenString) {
        if (tokens != null) {
            for (Token t : tokens) {
                if (t.getTokenString().equals(tokenString)) {
                    return true;
                }
            }
        }
        return false;
    }

    public synchronized void addToken(Token token) {
        if (tokens == null) {
            tokens = new ArrayList<>();
        }

        for (int i = 0; i < tokens.size(); i++) {
            if (tokens.get(i).getUserId().equals(token.getUserId())) {
                tokens.set(i, token);
                return;
            }
        }
        tokens.add(token);
    }

    public User getUserByToken(String tokenString) {
        if (tokens != null) {
            for (Token t : tokens) {
                if (t.getTokenString().equals(tokenString)) {
                    return t.getUser();
                }
            }
        }
        return null;
    }
}
