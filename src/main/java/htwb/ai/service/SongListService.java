package htwb.ai.service;

import htwb.ai.dao.SongListDao;
import htwb.ai.model.SongList;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class SongListService implements ISongListService {

    @Autowired
    private SongListDao songListDao;

    @Override
    public SongList getSongList(int id) {
        return songListDao.get(id);
    }

    @Override
    public List<SongList> getAllSongLists(String ownerId, Boolean isOwner) {
        return songListDao.getAllByUser(ownerId,isOwner);
    }

    @Override
    public int insertSongList(SongList songList) {
        return songListDao.save(songList);
    }

    @Override
    public int deleteSongList(int songListId) {
        return songListDao.deleteSongList(songListId);
    }
}
