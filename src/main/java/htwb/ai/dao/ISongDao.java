package htwb.ai.dao;

import htwb.ai.model.Song;
import htwb.ai.model.User;

import java.util.List;

public interface ISongDao<T> {

    /**
     * Returns song with id from the db
     * @param id id of the song
     * @return the object of the song
     */
    Song get(int id);

    /**
     * Returns all songs from the db
     * @return a list of all songs
     */
    List<T> getAll();

    /**
     * Persists a new song
     * @param t a Song in Json format
     * @return the song that got persisted
     */
    int save(T t);

    /**
     * Updates a song that is already in the db
     * @param song song object to replace existing song in the db
     */
    void updateSong(Song song);

    /**
     * Deletes a song with songId from the db
     * @param songId
     * @return int>0 when delete was successful
     */
    int deleteSong(int songId);
}
