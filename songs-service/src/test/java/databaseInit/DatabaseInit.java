package databaseInit;

import com.fasterxml.jackson.databind.ObjectMapper;
import htwb.ai.service.SongListService;
import htwb.ai.service.SongService;
import htwb.ai.service.UserService;
import htwb.ai.model.Song;
import htwb.ai.model.User;
import htwb.ai.model.SongList;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class DatabaseInit {



    private ObjectMapper mapper = new ObjectMapper();

    public void addSongs(String path, SongService songService) {
        try {
            File file = new File(path);
            List<Song> songs = Arrays.asList(mapper.readValue(file, Song[].class));

            for (Song song : songs) {
                songService.insertSong(song);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addUsers(String path, UserService userService) {
        try {
            File file = new File(path);
            List<User> users = Arrays.asList(mapper.readValue(file, User[].class));

            for (User user : users) {
                userService.insertUser(user);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addSongLists(String path, SongListService songListService) {
        try {
            File file = new File(path);
            List<SongList> songLists = Arrays.asList(mapper.readValue(file, SongList[].class));

            for (SongList songList : songLists) {
                songListService.insertSongList(songList);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




}