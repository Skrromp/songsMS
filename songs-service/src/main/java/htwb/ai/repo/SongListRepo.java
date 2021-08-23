package htwb.ai.repo;

import htwb.ai.model.SongList;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SongListRepo extends CrudRepository<SongList, Integer> {


}
