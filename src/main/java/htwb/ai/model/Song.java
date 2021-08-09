package htwb.ai.model;


import com.sun.istack.NotNull;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "\"Song\"")
public class Song {

    public Song(String title, String artist, String label, int released, Set<SongList> songLists) {
        this.title = title;
        this.artist = artist;
        this.label = label;
        this.released = released;
        this.songLists = songLists;
    }

    public Song() {
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getReleased() {
        return released;
    }

    public void setReleased(int released) {
        this.released = released;
    }

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

    private Song(Song.Builder builder) {
        this.id = builder.id;
        this.title = builder.title;
        this.artist = builder.artist;
        this.label = builder.label;
        this.released = builder.released;
        this.songLists = builder.songLists;
    }

    public boolean equals(Song song) {
        return song.getId() == id &&
               song.getTitle().equals(title) &&
               song.getArtist().equals(artist) &&
               song.getLabel().equals(label) &&
               song.getReleased() == released;
    }

    /**
     * Creates builder to build {@link Song}.
     *
     * @return created builder
     */
    public static Song.Builder builder() {
        return new Song.Builder();
    }

    /**
     * Builder to build {@link Song}.
     */

    public static final class Builder {

        private int id;
        private String title;
        private String artist;
        private String label;
        private int released;
        private Set<SongList> songLists;

        private Builder() {
        }

        public Song.Builder withId(int id) {
            this.id = id;
            return this;
        }

        public Song.Builder withTitle(String title) {
            this.title = title;
            return this;
        }

        public Song.Builder withArtist(String artist) {
            this.artist = artist;
            return this;
        }

        public Song.Builder withLabel(String label) {
            this.label = label;
            return this;
        }

        public Song.Builder withReleased(int released) {
            this.released = released;
            return this;
        }

        public Song.Builder withSongList(Set<SongList> songList) {
            this.songLists = songLists;
            return this;
        }

        public Song build() {
            return new Song(this);
        }
    }


}
