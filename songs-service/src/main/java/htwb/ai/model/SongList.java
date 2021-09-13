package htwb.ai.model;

import com.fasterxml.jackson.annotation.*;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
@Entity
@Table(name = "songList")
public class SongList {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "songListId")
    private int id;

    private String ownerId;

    private String name;

    @JsonProperty("isPrivate")
    private Boolean isPrivate;

    @ManyToMany
    @JsonProperty("songList")
    @JoinTable(name = "songList_song", joinColumns = {
            @JoinColumn(name = "songList_id", referencedColumnName = "songListId")}, inverseJoinColumns = {
            @JoinColumn(name = "song_id", referencedColumnName = "songId")})
    private Set<Song> songs;
}
