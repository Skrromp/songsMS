package htwb.ai.auth;

import htwb.ai.model.User;

public class Token {

    private User user;

    private String tokenString;

    public Token(User user, String tokenString) {
        this.user = user;
        this.tokenString = tokenString;
    }

    public Boolean equals(Token token) {
        if (token.getUserId().equals(user.getUserId()) && tokenString.equals(token.tokenString)) {
            return true;
        } else {
            return false;
        }
    }

    public String getUserId() {
        return user.getUserId();
    }

    public User getUser(){
        return user;
    }

    public String getTokenString() {
        return tokenString;
    }

}
