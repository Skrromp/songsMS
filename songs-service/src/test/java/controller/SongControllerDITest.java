package controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import databaseInit.DatabaseInit;
import htwb.ai.auth.AuthenticationService;
import htwb.ai.auth.Token;
import htwb.ai.controller.SongControllerDI;
import htwb.ai.model.User;
import htwb.ai.service.SongService;
import htwb.ai.service.UserService;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
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
public class SongControllerDITest {

    public static final String payloadFull = "{\"id\":\"1\",\"title\":\"title\",\"artist\":\"artist\",\"label\":\"label\",\"released\":42069}";
    public static final String payloadFull2 = "{\"id\":\"11\",\"title\":\"yeetus deletus\",\"artist\":\"someartist\",\"label\":\"somelabel\",\"released\":42069}";
    public static final String payloadFullNegativeId = "{\"id\":\"-1\",\"title\":\"fetus deletus\",\"artist\":\"someartist\",\"label\":\"somelabel\",\"released\":42069}";
    public static final String payloadNoTitle = "{\"id\":\"1\",\"artist\":\"someartist\",\"label\":\"somelabel\",\"released\":42069}";
    public static final String payloadEmpty = "{}";
    public static final String allSongs = "{\"id\":1,\"title\":\"someTitle\",\"artist\":\"bsmith\",\"label\":\"secret\",\"released\":2000}{\"id\":2,\"title\":\"someOtherTitle\",\"artist\":\"mjack\",\"label\":\"noSecret\",\"released\":2020}";
    public static final String authHeader = "{\"userId\":mmax,\"password\":\"pass1234\"}";
    public static final String authHeaderBad = "{\"userId\":xamm,\"password\":\"pass1234\"}";
    String authToken = "bliblablub";

    private MockMvc mockMvc;
    @Autowired
    SongControllerDI songControllerDI;

    @Autowired
    SongService songService;

    @Autowired
    UserService userService;

    private File songsFile;
    private File usersFile;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(songControllerDI).build();

        String path = "src/test/resources";
        songsFile = new File(path + "/songs.json");
        usersFile = new File(path + "/users.json");
        DatabaseInit dbinit = new DatabaseInit();
        dbinit.addSongs(songsFile.getAbsolutePath(), songService);
        dbinit.addUsers(usersFile.getAbsolutePath(), userService);

        AuthenticationService.getInstance().addToken(new Token(User.builder().withUserId("ootto").build(), authToken));
    }

    @Test
    void getSongShouldReturn200() throws Exception {
        mockMvc.perform(get("/songs/1").accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.title").value("Fetus Deletus"))
                .andExpect(jsonPath("$.artist").value("Unborn Child"))
                .andExpect(jsonPath("$.label").value("Abortion Clinic"))
                .andExpect(jsonPath("$.released").value("2021"));
    }

    @Test
    void getSongIDNotFoundShouldReturn404() throws Exception {
        mockMvc.perform(get("/songs/11").accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isNotFound());
    }

    @Test
    void getSongWrongInputShouldReturn400() throws Exception {
        mockMvc.perform(get("/songs/string").accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAllSongsShouldReturn200() throws Exception {
        MvcResult result = mockMvc.perform(get("/songs/").accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        String resultString = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        Assert.assertEquals(5, mapper.readTree(resultString).size());
    }

    @Test
    void getAllSongsInvalidPathShouldReturn404() throws Exception {
        mockMvc.perform(get("/songs blub").accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteSongShouldReturn204() throws Exception {
        mockMvc.perform(delete("/songs/2").header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteSongIDNotFoundShouldReturn404() throws Exception {
        mockMvc.perform(delete("/songs/12").header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteInvalidInputShouldReturn400() throws Exception {
        mockMvc.perform(delete("/songs/string").header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isBadRequest());
    }


    @Test
    void putSongShouldReturn204() throws Exception {
        String b = "";
        mockMvc.perform(put("/songs/1").content(payloadFull).contentType(MediaType.APPLICATION_JSON_VALUE).header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isNoContent());
    }

    @Test
    void putSongEmptyShouldReturn400() throws Exception {
        mockMvc.perform(put("/songs/1").content(payloadEmpty).contentType(MediaType.APPLICATION_JSON_VALUE).header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isBadRequest());
    }

    @Test
    void putSongInvalidIdShouldReturn404() throws Exception {
        mockMvc.perform(put("/songs/-1").content(payloadFullNegativeId).contentType(MediaType.APPLICATION_JSON_VALUE).header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isNotFound());
    }

    @Test
    void putSongNoMatchingIDsShouldReturn400() throws Exception {
        mockMvc.perform(put("/songs/2").content(payloadFull).contentType(MediaType.APPLICATION_JSON_VALUE).header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isBadRequest());
    }

    @Test
    void putSongNoTitleShouldReturn400() throws Exception {
        mockMvc.perform(put("/songs/1").content(payloadNoTitle).contentType(MediaType.APPLICATION_JSON_VALUE).header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isBadRequest());
    }

    @Test
    void putSongNonExistentIDShouldReturn404() throws Exception {
        mockMvc.perform(put("/songs/11").content(payloadFull2).contentType(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isNotFound());
    }

    @Test
    void postSongShouldReturnCreated() throws Exception {
        mockMvc.perform(post("/songs").content(payloadFull).contentType(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isCreated());
    }

    @Test
    void postSongNoTitleShouldReturn400() throws Exception {
        mockMvc.perform(post("/songs").content(payloadNoTitle).contentType(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isBadRequest());
    }

    @Test
    void postSongNoContentShouldReturn400() throws Exception {
        mockMvc.perform(post("/songs").content(payloadEmpty).contentType(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isBadRequest());
    }
}

