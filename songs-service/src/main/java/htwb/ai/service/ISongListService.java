package htwb.ai.service;

import htwb.ai.model.SongList;

import java.util.List;

public interface ISongListService {

    public SongList getSongList(int id);

    public List<SongList> getAllSongLists(String ownerId,Boolean isOwner);

    public int insertSongList(SongList songList);

    public int deleteSongList(int songListId);

}