package htwb.ai.repo;

import htwb.ai.model.SongList;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SongListRepo extends CrudRepository<SongList, Integer> {

    List<SongList> findAllByOwnerIdOrIsPrivateFalse(String ownerId);
    SongList findSonglistById(Integer id);
}
