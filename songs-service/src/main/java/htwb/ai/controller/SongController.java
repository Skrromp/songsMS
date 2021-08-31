package htwb.ai.controller;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import htwb.ai.model.Song;
import htwb.ai.repo.SongRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

@RequestMapping(value = "/songs")
@RestController
public class SongController {

    @Autowired
    SongRepo songRepo;

    @GetMapping(value = "/{id}", produces = {"application/json", "application/xml"})
    public ResponseEntity<String> get(@RequestHeader(name = "accept", required = true) String acceptedContentType, @RequestHeader(name = "authorization", required = true) String authorizationToken, @PathVariable int id) {

        Song resultSong = songRepo.findSongById(id);
        ResponseEntity re = null;

        if (acceptedContentType.contains("xml") || acceptedContentType.contains("*")) {
            XmlMapper xmapper = new XmlMapper();
            String xmlString;
            try {
                xmlString = xmapper.writeValueAsString(resultSong);
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseEntity.badRequest().build();
            }
            if (xmlString == null || resultSong == null) {
                re = ResponseEntity.notFound().build();
            } else {
                re = ResponseEntity.ok().contentType(MediaType.APPLICATION_XML).body(xmlString);
            }

        } else if (acceptedContentType.contains("json")) {
            ObjectMapper jmapper = new ObjectMapper();
            String jsonString;
            try {
                jsonString = jmapper.writeValueAsString(resultSong);
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseEntity.badRequest().build();
            }
            if (jsonString == null || resultSong == null) {
                re = ResponseEntity.notFound().build();
            } else {
                re = ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(jsonString);
            }
        }
        return re;
    }

    @GetMapping(value = "", produces = {"application/json", "application/xml"})
    public ResponseEntity<String> getAll(@RequestHeader(name = "accept", required = true) String acceptedContentType, @RequestHeader(name = "authorization", required = true) String authorizationToken) {

        List<Song> songList = (List<Song>) songRepo.findAll();
        ResponseEntity re = null;

        if (acceptedContentType.contains("xml")) {
            XmlMapper xmapper = new XmlMapper();
            String xmlString;
            try {
                xmlString = xmapper.writeValueAsString(songList);
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseEntity.badRequest().build();
            }
            if (xmlString == null) {
                re = ResponseEntity.notFound().build();
            } else {
                re = ResponseEntity.ok().contentType(MediaType.APPLICATION_XML).body(xmlString);
            }

        } else if (acceptedContentType.contains("json")) {
            ObjectMapper jmapper = new ObjectMapper();
            String jsonString;
            try {
                jsonString = jmapper.writeValueAsString(songList);
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseEntity.badRequest().build();
            }
            if (jsonString == null) {
                re = ResponseEntity.notFound().build();
            } else {
                re = ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(jsonString);
            }
        }
        return re;
    }

    @PostMapping(produces = "text/plain")
    public ResponseEntity<String> postSong(@RequestBody String json, @RequestHeader(name = "authorization", required = true) String authorizationToken) {

        ObjectMapper mapper = new ObjectMapper();

        Song song = new Song();
        try {
            song = mapper.readValue(json, Song.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (song.getTitle() == null) {
            return ResponseEntity.badRequest().build();
        }
        int songId = songRepo.save(song).getId();
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Location",
                "/rest/songs/" + songId);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .headers(responseHeaders)
                .build();
    }

    @PutMapping(value = "/{id}", produces = "application/json", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<String> put(@PathVariable int id, @RequestBody String json, @RequestHeader(name = "authorization", required = true) String authorizationToken) {

        ObjectMapper mapper = new ObjectMapper();
        Song song = new Song();

        try {
            song = mapper.readValue(json, Song.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (song.getTitle() == null) {
            return ResponseEntity.badRequest().build();
        }

        if (songRepo.existsById(song.getId())) {
            return ResponseEntity.notFound().build();
        }

        if (id == song.getId()) {
            songRepo.save(song);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping(value = "/{id}", produces = "text/plain")
    public ResponseEntity<String> delete(@PathVariable int id, @RequestHeader(name = "authorization", required = true) String ownerId) {

        songRepo.deleteById(id);
        if (!songRepo.existsById(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
