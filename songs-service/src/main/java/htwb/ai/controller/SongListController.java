package htwb.ai.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import htwb.ai.model.Song;
import htwb.ai.model.SongList;
import htwb.ai.repo.SongListRepo;
import htwb.ai.repo.SongRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpHeaders;

import javax.servlet.http.*;
import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping(value = "/songs/playlist")
public class SongListController {

    @Autowired
    SongListRepo songListRepo;

    @Autowired
    SongRepo songRepo;

    @GetMapping(value = "/{id}", produces = {"application/json", "application/xml"})
    public ResponseEntity<String> get(@RequestHeader(name = "accept", required = true) String acceptedContentType, @RequestHeader(name = "authorization", required = true) String ownerId, @PathVariable int id) {

        SongList songList;
        ResponseEntity<String> re = null;

        //map SongList to xml
        if (acceptedContentType.contains("xml") || acceptedContentType.contains("*")) {
            XmlMapper xmapper = new XmlMapper();
            songList = songListRepo.findSonglistById(id);
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
                if (ownerId.equals(songList.getOwnerId()) && songList.getIsPrivate()) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
                }
                re = ResponseEntity.ok().contentType(MediaType.APPLICATION_XML).body(xmlString);
            }

            //map SongList to json
        } else if (acceptedContentType.contains("json")) {
            ObjectMapper jmapper = new ObjectMapper();
            songList = songListRepo.findSonglistById(id);
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
                if ((ownerId.equals(songList.getOwnerId())) && songList.getIsPrivate()) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
                }
                re = ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(jsonString);
            }
        }
        return re;

    }

    @GetMapping(value = "/", produces = {"application/json", "application/xml"})
    public ResponseEntity<String> getAll(@RequestHeader(name = "accept", required = true) String acceptedContentType, @RequestHeader(name = "authorization", required = true) String ownerId) {

        //authentification check

        List<SongList> songList;
        songList = songListRepo.findAllByOwnerIdOrIsPrivateFalse(ownerId);
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
    public ResponseEntity<String> getAllByUserId(@RequestHeader(name = "accept", required = true) String acceptedContentType, @RequestHeader(name = "authorization", required = true) String ownerId, @RequestParam(required = true, name = "userId") String userIdRequest) {

        List<SongList> songList;

        songList = songListRepo.findAllByOwnerIdOrIsPrivateFalse(ownerId);

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
    public ResponseEntity<String> postSongList(@RequestBody String json, @RequestHeader(name = "authorization", required = true) String username, HttpServletRequest request) {

        String url = request.getRequestURL().toString();

        ObjectMapper mapper = new ObjectMapper();

        SongList songList;
        try {
            songList = mapper.readValue(json, SongList.class);
            songList.setOwnerId(username);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }

        if (!songList.getOwnerId().equals(username)) {
            return ResponseEntity.badRequest().build();
        }

        if (songList.getSongs() == null || songList.getSongs().size() == 0) {

            return ResponseEntity.badRequest().build();
        }

        for (Song s : songList.getSongs()) {
            Song sbSong = songRepo.findSongById(s.getId());
            if (!s.equals(sbSong)) {
                return ResponseEntity.badRequest().build();
            }
        }

        int songListId = songList.getId();
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Location",
                url + songListId);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .headers(responseHeaders)
                .build();
    }

    @DeleteMapping(value = "/{id}", produces = "text/plain")
    public ResponseEntity<String> delete(@PathVariable int id, @RequestHeader(name = "authorization", required = true) String username) {

        SongList sl = songListRepo.findSonglistById(id);

        if (sl == null) {
            return ResponseEntity.notFound().build();
        } else if (!sl.getOwnerId().equals(username)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        songListRepo.deleteById(id);
        //confirm deletion
        if (!songListRepo.existsById(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
