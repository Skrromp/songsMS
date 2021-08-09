package htwb.ai.dao;

import htwb.ai.model.Song;
import htwb.ai.model.SongList;

import java.util.List;

public interface ISongListDao {

    /**
     * Returns a SongList with {@param id}
     *
     * @param id
     * @return SongList
     */
    SongList get(int id);

    /**
     * Returns all song lists of user with {@param id}
     * only public ones if not {@param isOwner}
     *
     * @param userId
     * @param isOwner
     * @return list
     */
    List<SongList> getAllByUser(String userId, Boolean isOwner);

    /**
     * Deletes a song list
     *
     * @param id
     * @return >0 if delete was successful
     */
    int deleteSongList(int id);

    /**
     * Persists a new song list
     *
     * @param sl song list to be save
     * @return the song list that got persisted
     */
    int save(SongList sl);

}
