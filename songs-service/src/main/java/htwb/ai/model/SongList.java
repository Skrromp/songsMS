package htwb.ai.model;

import com.fasterxml.jackson.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name = "\"SongList\"")
@JsonPropertyOrder({"id","ownerId", "name", "isPrivate", "songs"})
public class SongList {

    @Id
    @TableGenerator(name = "SongList_Gen", initialValue = 0, allocationSize = 10000)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "SongList_Gen")
    @Column(name = "songListId")
    int id;

    String ownerId;

    String name;


    Boolean isPrivate;

    @ManyToMany
    private Set<Song> songs;
}
