package htwb.ai.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
@Entity
@Table(name="song")
public class Song {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "songId")
    private int id;

    private String title;

    private String artist;

    private String label;

    private int released;

    @ManyToMany(mappedBy = "songs" ,cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<SongList> songLists;
}
