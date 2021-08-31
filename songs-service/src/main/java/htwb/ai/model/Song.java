package htwb.ai.model;

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
@Table(name = "\"Song\"")
@Entity
public class Song {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;

    private String title;

    private String artist;

    private String label;

    private int released;

    @ManyToMany(mappedBy = "songs", cascade = CascadeType.ALL)
    private Set<SongList> songLists;
}
