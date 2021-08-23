package htwb.ai.model;


import com.sun.istack.NotNull;
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
public class Song {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;

    @NotNull
    private String title;

    private String artist;

    private String label;

    private int released;

    @ManyToMany(mappedBy = "songs", cascade = CascadeType.ALL)
    private Set<SongList> songLists;
}
