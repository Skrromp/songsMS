package htwb.ai.service;

import htwb.ai.model.Song;

import java.util.List;

public interface ISongService {

    public Song getSong(int id);

    public List<Song> getAllSongs();

    public int insertSong(Song song);

    public void updateSong(Song song);

    public int deleteSong(int songid);

}
