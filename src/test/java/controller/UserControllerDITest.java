package controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import databaseInit.DatabaseInit;
import htwb.ai.controller.UserControllerDI;
import htwb.ai.dao.UserDao;
import htwb.ai.service.UserService;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.File;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest
@ContextConfiguration(locations = "classpath:testSpringMVC-servlet.xml")
public class UserControllerDITest {

    private String payloadIncorrect = "{\"userId\":\"ootto\",\"password\":\"geheim\"}";
    private String payloadOkay = "{\"userId\":\"ootto\",\"password\":\"pass1234\"}";
    private String payloadInvalid = "{\"userId.ERROR\":\"mmuster\",\"password\":\"geheim\"}";

    private MockMvc mockMvc;
    @Autowired
    UserControllerDI userControllerDI;

    @Autowired
    UserService userService;

    private File usersFile;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(
                userControllerDI).build();

        String path = "src/test/resources";
        usersFile = new File(path + "/users.json");
        DatabaseInit dbinit = new DatabaseInit();
        dbinit.addUsers(usersFile.getAbsolutePath(), userService);
    }


    @Test
    void postUserShouldReturnOkForExistingId() throws Exception {
        mockMvc.perform(post("/auth").content(payloadOkay).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void postUserShouldReturn401ForIncorrectLogin() throws Exception {
        mockMvc.perform(post("/auth").content(payloadIncorrect).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void postUserShouldReturn400ForInvalidLogin() throws Exception {
        mockMvc.perform(post("/auth").content(payloadInvalid).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

}
