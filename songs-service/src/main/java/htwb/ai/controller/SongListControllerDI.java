package htwb.ai.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import htwb.ai.auth.AuthenticationService;
import htwb.ai.model.Song;
import htwb.ai.model.SongList;
import htwb.ai.model.User;
import htwb.ai.service.ISongListService;
import htwb.ai.service.ISongService;
import htwb.ai.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpHeaders;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value = "/songLists")
public class SongListControllerDI {

    @Autowired
    ISongListService songListService;

    @Autowired
    ISongService songService;



    //authentification check

    @GetMapping(value = "/{id}", produces = {"application/json", "application/xml"})
    public ResponseEntity<String> get(@RequestHeader(name = "accept", required = true) String acceptedContentType, @RequestHeader(name = "authorization", required = true) String authorizationToken, @PathVariable int id) {

        User user = authService.getUserByToken(authorizationToken);

        //authentification check

        SongList songList;
        ResponseEntity<String> re = null;

        //map SongList to xml
        if (acceptedContentType.contains("xml") || acceptedContentType.contains("*")) {
            XmlMapper xmapper = new XmlMapper();
            songList = songListService.getSongList(id);
            String xmlString;

            try {
                xmlString = xmapper.writeValueAsString(songList);
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseEntity.badRequest().build();
            }

            if (xmlString == null || songList == null) {
                re = ResponseEntity.notFound().build();
            } else {
                //is request allowed to get this SongList
                if ((!user.getUserId().equals(songList.getOwnerId())) && songList.isPrivate()) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
                }
                re = ResponseEntity.ok().contentType(MediaType.APPLICATION_XML).body(xmlString);
            }

            //map SongList to json
        } else if (acceptedContentType.contains("json")) {
            ObjectMapper jmapper = new ObjectMapper();
            songList = songListService.getSongList(id);
            String jsonString;

            try {
                jsonString = jmapper.writeValueAsString(songList);
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseEntity.badRequest().build();
            }

            if (jsonString == null || songList == null) {
                re = ResponseEntity.notFound().build();
            } else {
                //is request allowed to get this SongList
                if ((!user.getUserId().equals(songList.getOwnerId())) && songList.isPrivate()) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
                }
                re = ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(jsonString);
            }
        }
        return re;

    }

    @GetMapping(value = "/", produces = {"application/json", "application/xml"})
    public ResponseEntity<String> getAll(@RequestHeader(name = "accept", required = true) String acceptedContentType, @RequestHeader(name = "authorization", required = true) String authorizationToken) {

        //authentification check


        User user = authService.getUserByToken(authorizationToken);
        List<SongList> songList;
        songList = songListService.getAllSongLists(user.getUserId(), true);
        ResponseEntity re = null;

        //map SongList to xml
        if (acceptedContentType.contains("xml") || acceptedContentType.contains("*")) {
            XmlMapper xmapper = new XmlMapper();
            String xmlString;

            try {
                xmlString = xmapper.writeValueAsString(songList);
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseEntity.badRequest().build();
            }

            if (xmlString == null || songList == null || xmlString.isEmpty()) {
                re = ResponseEntity.notFound().build();
            } else {
                re = ResponseEntity.ok().contentType(MediaType.APPLICATION_XML).body(xmlString);
            }
            //map SongList to json
        } else if (acceptedContentType.contains("json")) {
            ObjectMapper jmapper = new ObjectMapper();
            String jsonString;

            try {
                jsonString = jmapper.writeValueAsString(songList);
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseEntity.badRequest().build();
            }

            if (jsonString == null || songList == null || jsonString.isEmpty()) {
                re = ResponseEntity.notFound().build();
            } else {
                re = ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(jsonString);
            }
        }

        return re;
    }

    @GetMapping(value = "", produces = {"application/json", "application/xml"})
    public ResponseEntity<String> getAllByUserId(@RequestHeader(name = "accept", required = true) String acceptedContentType, @RequestHeader(name = "authorization", required = true) String authorizationToken, @RequestParam(required = true, name = "userId") String userIdRequest) {

        //authentification check

        User user = authService.getUserByToken(authorizationToken);
        List<SongList> songList;

        if (user.getUserId().equals(userIdRequest)) {
            songList = songListService.getAllSongLists(userIdRequest, true);
        } else {
            songList = songListService.getAllSongLists(userIdRequest, false);
        }

        ResponseEntity re = null;

        //map SongList to xml
        if (acceptedContentType.contains("xml") || acceptedContentType.contains("*")) {
            XmlMapper xmapper = new XmlMapper();
            String xmlString;
            try {
                xmlString = xmapper.writeValueAsString(songList);
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseEntity.badRequest().build();
            }

            if (xmlString == null || songList == null || xmlString.equals("[]")) {
                re = ResponseEntity.notFound().build();
            } else {
                re = ResponseEntity.ok().contentType(MediaType.APPLICATION_XML).body(xmlString);
            }
            //map SongList to json
        } else if (acceptedContentType.contains("json")) {
            ObjectMapper jmapper = new ObjectMapper();
            String jsonString;
            try {
                jsonString = jmapper.writeValueAsString(songList);
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseEntity.badRequest().build();
            }

            //confirm contents
            if (jsonString == null || songList == null || jsonString.equals("[]")) {
                re = ResponseEntity.notFound().build();
            } else {
                re = ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(jsonString);
            }
        }

        return re;
    }

    @PostMapping(produces = "text/plain")
    public ResponseEntity<String> postSongList(@RequestBody String json, @RequestHeader(name = "authorization", required = true) String authorizationToken) {

        //authentification check

        User user = authService.getUserByToken(authorizationToken);
        ObjectMapper mapper = new ObjectMapper();

        SongList songList;
        try {
            songList = mapper.readValue(json, SongList.class);
            songList.setOwnerId(user);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }

        if (!songList.getOwnerId().equals(user.getUserId())) {
            return ResponseEntity.badRequest().build();
        }

        if (songList.getSongs() == null || songList.getSongs().size() == 0) {

            return ResponseEntity.badRequest().build();
        }

        for (Song s : songList.getSongs()) {
            Song sbSong = songService.getSong(s.getId());
            if (!s.equals(sbSong)) {
                return ResponseEntity.badRequest().build();
            }
        }

        int songListId = songListService.insertSongList(songList);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Location",
                "/rest/songLists/" + songListId);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .headers(responseHeaders)
                .build();
    }

    @DeleteMapping(value = "/{id}", produces = "text/plain")
    public ResponseEntity<String> delete(@PathVariable int id, @RequestHeader(name = "authorization", required = true) String authorizationToken) {

        //authentification check

        SongList sl = songListService.getSongList(id);

        if (sl == null) {
            return ResponseEntity.notFound().build();
        } else if (!sl.getOwnerId().equals(user.getUserId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        int rslt = songListService.deleteSongList(id);
        if (rslt > 0) {
            return ResponseEntity.noContent().build();
        } else
            return ResponseEntity.notFound().build();
    }
}
