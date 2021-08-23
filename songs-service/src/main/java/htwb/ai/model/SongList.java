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
public class SongList {

    @Id
    @TableGenerator(name = "SongList_Gen", initialValue = 0, allocationSize = 10000)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "SongList_Gen")
    @Column(name = "songListId")
    @JsonProperty("songListId")
    int id;

//    @ManyToOne(fetch = FetchType.EAGER)
//    @JoinColumn(name = "ownerId")
//    @JsonProperty("ownerId")
//    User ownerId;

    @JsonProperty("name")
    String name;

    @JsonProperty("isPrivate")
    boolean isPrivate;

    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinTable(name = "songList_song", joinColumns = {
            @JoinColumn(name = "songList_id", referencedColumnName = "songListId")}, inverseJoinColumns = {
            @JoinColumn(name = "song_id", referencedColumnName = "id")})
    @JsonProperty("songList")
    Set<Song> songs;
}
