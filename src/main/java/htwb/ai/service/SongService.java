package htwb.ai.service;

import htwb.ai.dao.SongDao;
import htwb.ai.model.Song;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class SongService implements ISongService{
    
    @Autowired
    private SongDao songDao;

    @Override
    public Song getSong(int id) {
        return songDao.get(id);
    }

    @Override
    public List<Song> getAllSongs() {
        return songDao.getAll();
    }

    @Override
    public int insertSong(Song song) {
        return songDao.save(song);
    }

    @Override
    public void updateSong(Song song){
       songDao.updateSong(song);
    }

    @Override
    public int deleteSong(int songId){
        return songDao.deleteSong(songId);
    }
}
