package htwb.ai.controller;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.lambdaexpression.annotation.EnableRequestBodyParam;
import htwb.ai.auth.Token;
import htwb.ai.auth.AuthenticationService;
import htwb.ai.model.User;
import htwb.ai.service.IUserService;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.util.Random;


@RestController
@EnableRequestBodyParam
@RequestMapping(value = "/auth")
public class UserControllerDI {


    @Autowired
    private IUserService userService;

    @PostMapping(produces = "text/plain;charset=UTF-8", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<String> auth(@RequestBody String body) {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = null;
        User user;
        String password;
        try {
            node = mapper.readTree(body);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            user = userService.getUserByUserId(node.get("userId").asText());
            password = node.get("password").asText();
        } catch (NullPointerException e) {
            return ResponseEntity.badRequest().build();
        }

        if (user != null && user.getPassword().equals(password)) {

            String token = generateToken();
            AuthenticationService.getInstance().addToken(new Token(user,token));

            return ResponseEntity.ok().body(token);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    private String generateToken() {
        StringBuilder sb = new StringBuilder();
        for (int i = 16; sb.toString().length() <= i; ) {
            Random r = new Random();
            sb.append(RandomStringUtils.randomAlphabetic(r.nextInt(2)));
            sb.append(RandomStringUtils.randomNumeric(r.nextInt(2)));
        }
        return sb.toString();
    }
}
