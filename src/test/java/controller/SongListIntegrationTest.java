package controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import databaseInit.DatabaseInit;
import htwb.ai.auth.AuthenticationService;
import htwb.ai.auth.Token;
import htwb.ai.controller.SongControllerDI;
import htwb.ai.controller.SongListControllerDI;
import htwb.ai.controller.UserControllerDI;
import htwb.ai.model.Song;
import htwb.ai.model.SongList;
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
import java.util.ArrayList;
import java.util.List;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest
@ContextConfiguration(locations = "classpath:testSpringMVC-servlet.xml")
public class SongListIntegrationTest {

    String payloadWrong = "test";
    String payloadFull = "{\n" +
            "  \"isPrivate\": \"true\",\n" +
            "  \"name\": \"AnnaPlaylist\",\n" +
            "  \"ownerId\": \"aanna\",\n" +
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

    private String authUser1 = "{\"userId\":\"ootto\",\"password\":\"pass1234\"}";
    private String authUser2 = "{\"userId\":\"aanna\",\"password\":\"pass1234\"}";

    private MockMvc mockMvcAuth;
    private MockMvc mockMvcSongList;

    @Autowired
    SongListControllerDI songListControllerDI;

    @Autowired
    SongControllerDI songControllerDI;

    @Autowired
    UserControllerDI userControllerDI;

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
        mockMvcAuth = MockMvcBuilders.standaloneSetup(userControllerDI).build();
        mockMvcSongList = MockMvcBuilders.standaloneSetup(songListControllerDI).build();

        String path = "src/test/resources";
        songsFile = new File(path + "/songs.json");
        usersFile = new File(path + "/users.json");
        songListsFile = new File(path + "/songLists.json");

        DatabaseInit dbinit = new DatabaseInit();
        dbinit.addSongs(songsFile.getAbsolutePath(), songService);
        dbinit.addUsers(usersFile.getAbsolutePath(), userService);
        dbinit.addSongLists(songListsFile.getAbsolutePath(), songListService);

    }

    @Test
    public void addSongListTest() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        MvcResult result = mockMvcAuth.perform(post("/auth").content(authUser2).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        String authToken = result.getResponse().getContentAsString();

        mockMvcSongList.perform(post("/songLists")
                .content(payloadFull).contentType(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isCreated());

        result = mockMvcSongList.perform(get("/songLists/5").accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)).andReturn();

        String temp = result.getResponse().getContentAsString();
        SongList songlist = mapper.readValue(temp, SongList.class);

        Assert.assertEquals("AnnaPlaylist", songlist.getName());
        Assert.assertEquals("aanna", songlist.getOwnerId());
        Assert.assertEquals(true, songlist.isPrivate());
        Assert.assertEquals(2, songlist.getSongs().size());
    }

    @Test
    public void deleteSongListTest() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        MvcResult result = mockMvcAuth.perform(post("/auth").content(authUser2).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        String authToken = result.getResponse().getContentAsString();

        result = mockMvcSongList.perform(get("/songLists/").accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)).andReturn();

        Assert.assertEquals(2, mapper.readTree(result.getResponse().getContentAsString()).size());


        mockMvcSongList.perform(delete("/songLists/3").header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isNoContent());

        result = mockMvcSongList.perform(get("/songLists/").accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)).andReturn();

        Assert.assertEquals(1, mapper.readTree(result.getResponse().getContentAsString()).size());
    }

    @Test
    public void getPubicSongListsTest() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        MvcResult result = mockMvcAuth.perform(post("/auth").content(authUser2).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        String authToken = result.getResponse().getContentAsString();

        result = mockMvcSongList.perform(get("/songLists?userId=ootto").accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)).andReturn();

        Assert.assertEquals(1, mapper.readTree(result.getResponse().getContentAsString()).size());
    }

    @Test
    public void addSongListTestNegative() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        MvcResult result = mockMvcAuth.perform(post("/auth").content(authUser2).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        String authToken = result.getResponse().getContentAsString();

        mockMvcSongList.perform(post("/songLists")
                .content(payloadNoMatchingSong).contentType(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isBadRequest());

        mockMvcSongList.perform(get("/songLists/5").accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isNotFound());

    }

    @Test
    public void deleteSongListTestNegative() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        MvcResult result = mockMvcAuth.perform(post("/auth").content(authUser2).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        String authToken = result.getResponse().getContentAsString();

        result = mockMvcSongList.perform(get("/songLists/").accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)).andReturn();

        Assert.assertEquals(2, mapper.readTree(result.getResponse().getContentAsString()).size());


        mockMvcSongList.perform(delete("/songLists/10").header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isNotFound());

        result = mockMvcSongList.perform(get("/songLists/").accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)).andReturn();

        Assert.assertEquals(2, mapper.readTree(result.getResponse().getContentAsString()).size());
    }

    @Test
    public void getPubicSongListsTestNegative() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        MvcResult result = mockMvcAuth.perform(post("/auth").content(authUser2).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        String authToken = result.getResponse().getContentAsString();

        mockMvcSongList.perform(get("/songLists?userId=oottooo").accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isNotFound());
    }


}