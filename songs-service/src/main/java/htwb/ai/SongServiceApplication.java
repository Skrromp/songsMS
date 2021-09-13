package htwb.ai;

import com.fasterxml.jackson.databind.ObjectMapper;
import htwb.ai.model.Song;
import htwb.ai.model.SongList;
import htwb.ai.repo.SongListRepo;
import htwb.ai.repo.SongRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

@EnableEurekaClient
@SpringBootApplication
public class SongServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SongServiceApplication.class, args);
    }

    @Bean
    public ApplicationRunner initializer() {
        return new ApplicationRunner() {

            @Autowired
            private SongRepo songRepo;
            @Autowired
            private SongListRepo songListRepo;

            ObjectMapper mapper = new ObjectMapper();


            public void addSongs() {
                try {
                    InputStream is = getClass().getClassLoader().getResourceAsStream("songs.json");
                    List<Song> songs = Arrays.asList(mapper.readValue(is, Song[].class));

                    for (Song song : songs) {
                        songRepo.save(song);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            public void addSongLists() {
                try {
                    InputStream is = getClass().getClassLoader().getResourceAsStream("songLists.json");
                    List<SongList> songLists = Arrays.asList(mapper.readValue(is, SongList[].class));

                    for (SongList songList : songLists) {
                        songListRepo.save(songList);
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
                addSongs();
                addSongLists();
            }
        };
    }
}
