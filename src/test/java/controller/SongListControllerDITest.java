package controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import databaseInit.DatabaseInit;
import htwb.ai.auth.AuthenticationService;
import htwb.ai.auth.Token;
import htwb.ai.controller.SongListControllerDI;
import htwb.ai.model.User;
import htwb.ai.service.SongListService;
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
public class SongListControllerDITest {

    String payloadWrong = "test";
    String payloadFull = "{\n" +
            "  \"isPrivate\": true,\n" +
            "  \"name\": \"AnnaPlaylist\",\n" +
            "  \"songList\": [\n" +
            "    {\n" +
            "      \"id\": 1,\n" +
            "      \"title\": \"Fetus Deletus\",\n" +
            "      \"artist\": \"Unborn Child\",\n" +
            "      \"label\": \"Abortion Clinic\",\n" +
            "      \"released\": 2021\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": 3,\n" +
            "      \"title\": \"Pretty Cvnt\",\n" +
            "      \"artist\": \"Sewerslvt\",\n" +
            "      \"label\": \"Sewer//slvt\",\n" +
            "      \"released\": 2020\n" +
            "    }\n" +
            "  ]\n" +
            "}";

    String payloadNoMatchingSong = "{\n" +
            "  \"isPrivate\": true,\n" +
            "  \"name\": \"AnnaPlaylist\",\n" +
            "  \"songList\": [\n" +
            "    {\n" +
            "      \"id\": 2,\n" +
            "      \"title\": \"Fetus Deletus\",\n" +
            "      \"artist\": \"Unborn Child\",\n" +
            "      \"label\": \"Abortion Clinic\",\n" +
            "      \"released\": 2021\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": 3,\n" +
            "      \"title\": \"Pretty Cvnt\",\n" +
            "      \"artist\": \"Sewerslvt\",\n" +
            "      \"label\": \"Sewer//slvt\",\n" +
            "      \"released\": 2020\n" +
            "    }\n" +
            "  ]\n" +
            "}";

    String payloadWrongParam = "{\n" +
            "  \"isPrivate\": true,\n" +
            "  \"name\": \"AnnaPlaylist\",\n" +
            "  \"songList\": [\n" +
            "    {\n" +
            "      \"id\": 2,\n" +
            "      \"song-name\": \"Fetus Deletus\",\n" +
            "      \"artist\": \"Unborn Child\",\n" +
            "      \"album\": \"Abortion Clinic\",\n" +
            "      \"released\": 2021\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": 3,\n" +
            "      \"artist\": \"Sewerslvt\",\n" +
            "      \"album\": \"Sewer//slvt\",\n" +
            "      \"released\": 2020\n" +
            "    }\n" +
            "  ]\n" +
            "}";

    String authToken = "bliblablub";
    String authTokenBad = "badToken";

    private MockMvc mockMvc;
    @Autowired
    SongListControllerDI songListControllerDI;

    @Autowired
    SongService songService;

    @Autowired
    UserService userService;

    @Autowired
    SongListService songListService;

    private File songsFile;
    private File usersFile;
    private File songListsFile;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(songListControllerDI).build();

        String path = "src/test/resources";
        songsFile = new File(path + "/songs.json");
        usersFile = new File(path + "/users.json");
        songListsFile = new File(path + "/songLists.json");

        DatabaseInit dbinit = new DatabaseInit();
        dbinit.addSongs(songsFile.getAbsolutePath(), songService);
        dbinit.addUsers(usersFile.getAbsolutePath(), userService);
        dbinit.addSongLists(songListsFile.getAbsolutePath(), songListService);

        AuthenticationService.getInstance().addToken(new Token(User.builder().withUserId("ootto").build(), authToken));
    }

    @Test
    void getSongListJsonShouldReturn200() throws Exception {
        mockMvc.perform(get("/songLists/4").accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void getSongListJsonShouldReturn403() throws Exception {
        mockMvc.perform(get("/songLists/3").accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isForbidden());
    }

    @Test
    void getSongListXMLShouldReturn403() throws Exception {
        mockMvc.perform(get("/songLists/3").accept(MediaType.APPLICATION_XML).header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isForbidden());
    }

    @Test
    void getSongListInvalidParamShouldReturn400() throws Exception {
        mockMvc.perform(get("/songLists/blub").accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getSongListUnknownIdShouldReturn404() throws Exception {
        mockMvc.perform(get("/songLists/200").accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isNotFound());
    }

    @Test
    void getSongListWrongAcceptShouldReturn406() throws Exception {
        mockMvc.perform(get("/songLists/1").accept(MediaType.APPLICATION_PDF).header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isNotAcceptable());
    }

    @Test
    void getSongListWrongAuthTokenShouldReturn401() throws Exception {
        mockMvc.perform(get("/songLists/1").accept(MediaType.APPLICATION_PDF).header(HttpHeaders.AUTHORIZATION, authTokenBad))
                .andExpect(status().isNotAcceptable());
    }

    @Test
    void getSongListXmlShouldReturn200() throws Exception {
        MvcResult result = mockMvc.perform(get("/songLists/2").accept(MediaType.APPLICATION_XML).header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_XML)).andReturn();
    }

    @Test
    void getAllSongListsShouldReturn200() throws Exception {
        MvcResult result = mockMvc.perform(get("/songLists/").accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)).andReturn();
    }

    @Test
    void getAllSongListsNoAuthShouldReturn406() throws Exception {
        mockMvc.perform(get("/songLists/").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAllSongListsWrongAcceptShouldReturn406() throws Exception {
        mockMvc.perform(get("/songLists/").accept(MediaType.APPLICATION_PDF).header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isNotAcceptable());
    }

    @Test
    void getAllSongListsWrongAuthTokenShouldReturn401() throws Exception {
        mockMvc.perform(get("/songLists/").accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, authTokenBad))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getAllSongListsByUserIdShouldReturn200() throws Exception {
        MvcResult result = mockMvc.perform(get("/songLists?userId=ootto").accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)).andReturn();
    }

    //TODO fix in code
    @Test
    void getAllSongListsByUserIdUnknownIdShouldReturn404() throws Exception {
        mockMvc.perform(get("/songLists?userId=-ootto-").accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllSongListsByUserIdInvalidTokenShouldReturn401() throws Exception {
        mockMvc.perform(get("/songLists?userId=otto").accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, authTokenBad))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getAllSongListsByUserIdEmptyParamShouldReturn400() throws Exception {
        mockMvc.perform(get("/songLists/      ").accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, authTokenBad))
                .andExpect(status().isBadRequest());
    }


    @Test
    void postSongListShouldReturn201() throws Exception {
        mockMvc.perform(post("/songLists")
                .content(payloadFull).header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isCreated());
    }

    @Test
    void postSongListNoAuthHeaderShouldReturn400() throws Exception {
        mockMvc.perform(post("/songLists")
                .content(payloadFull))
                .andExpect(status().isBadRequest());
    }

    @Test
    void postSongListWrongPayloadShouldReturn400() throws Exception {
        mockMvc.perform(post("/songLists")
                .content(payloadWrong).header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isBadRequest());
    }

    @Test
    void postSongListNoSongMatchShouldReturn400() throws Exception {
        mockMvc.perform(post("/songLists")
                .content(payloadNoMatchingSong).header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isBadRequest());
    }

    @Test
    void postSongListWrongParamShouldReturn400() throws Exception {
        MvcResult result = mockMvc.perform(post("/songLists")
                .content(payloadWrongParam).header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isBadRequest()).andReturn();
    }

    @Test
    void postSongLocationHeaderShouldReturn201() throws Exception {
        MvcResult result = mockMvc.perform(post("/songLists")
                .content(payloadFull).header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isCreated()).andReturn();
        Assert.assertEquals("/rest/songLists/5", result.getResponse().getHeader("Location"));
    }

    @Test
    void postSongListInvalidTokenShouldReturn401() throws Exception {
        mockMvc.perform(post("/songLists")
                .content(payloadFull).header(HttpHeaders.AUTHORIZATION, authTokenBad))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void deleteSongListShouldReturn204() throws Exception {
        mockMvc.perform(delete("/songLists/1").header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteSongListShouldReturn403() throws Exception {
        mockMvc.perform(delete("/songLists/3").header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isForbidden());
    }

    @Test
    void deleteSongListNonExistingIdShouldReturn404() throws Exception {
        mockMvc.perform(delete("/songLists/200").header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteSongListInvalidPathShouldReturn400() throws Exception {
        mockMvc.perform(delete("/songLists/blub").header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteSongListNoAuthHeaderShouldReturn400() throws Exception {
        mockMvc.perform(delete("/songLists/1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteSongListInvalidTokenShouldReturn400() throws Exception {
        mockMvc.perform(delete("/songLists/1").header(HttpHeaders.AUTHORIZATION, authTokenBad))
                .andExpect(status().isUnauthorized());
    }
}