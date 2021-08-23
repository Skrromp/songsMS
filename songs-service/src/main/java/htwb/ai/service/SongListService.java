package htwb.ai.service;

import htwb.ai.repo.SongListRepo;
import htwb.ai.model.SongList;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class SongListService implements ISongListService {

    @Autowired
    private SongListRepo songListRepo;

    @Override
    public SongList getSongList(int id) {
        return songListRepo.get(id);
    }

    @Override
    public List<SongList> getAllSongLists(String ownerId, Boolean isOwner) {
        return songListRepo.getAllByUser(ownerId,isOwner);
    }

    @Override
    public int insertSongList(SongList songList) {
        return songListRepo.save(songList);
    }

    @Override
    public int deleteSongList(int songListId) {
        return songListRepo.deleteSongList(songListId);
    }
}
