package htwb.ai.service;

import htwb.ai.repo.SongRepo;
import htwb.ai.model.Song;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class SongService implements ISongService{
    
    @Autowired
    private SongRepo songRepo;

    @Override
    public Song getSong(int id) {
        return songRepo.get(id);
    }

    @Override
    public List<Song> getAllSongs() {
        return songRepo.getAll();
    }

    @Override
    public int insertSong(Song song) {
        return songRepo.save(song);
    }

    @Override
    public void updateSong(Song song){
       songRepo.updateSong(song);
    }

    @Override
    public int deleteSong(int songId){
        return songRepo.deleteSong(songId);
    }
}
