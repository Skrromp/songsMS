package htwb.ai;

import com.fasterxml.jackson.databind.ObjectMapper;
import htwb.ai.model.User;
import htwb.ai.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

@EnableEurekaClient
@SpringBootApplication
public class AuthServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthServiceApplication.class, args);
    }

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }

    @Bean
    public ApplicationRunner initializer() {
        return new ApplicationRunner() {

            @Autowired
            private UserRepo userRepo;

            ObjectMapper mapper = new ObjectMapper();

            public void addUser() {
                try {
                    InputStream is = getClass().getClassLoader().getResourceAsStream("users.json");
                    List<User> users = Arrays.asList(mapper.readValue(is, User[].class));

                    for (User user : users) {
                        userRepo.save(user);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void run(ApplicationArguments args) throws Exception {
                initialize();
            }

            public void initialize() {
                addUser();
            }
        };
    }
}