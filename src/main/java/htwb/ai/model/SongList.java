package htwb.ai.model;

import com.fasterxml.jackson.annotation.*;
import org.hibernate.annotations.Proxy;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "\"SongList\"")
public class SongList {
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOwnerId() {
        return ownerId.getUserId();
    }

    public void setOwnerId(User ownerId) {
        this.ownerId = ownerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("isPrivate")
    public boolean isPrivate() {
        return isPrivate;
    }

    @JsonProperty("isPrivate")
    public void setPrivate(boolean isPrivate) {
        this.isPrivate = isPrivate;
    }

    public Set<Song> getSongs() {
        return songs;
    }

    public void setSongs(Set<Song> songs) {
        this.songs = songs;
    }

    @Id
    @TableGenerator(name = "SongList_Gen", initialValue = 0, allocationSize = 10000)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "SongList_Gen")
    @Column(name = "songListId")
    @JsonProperty("songListId")
    int id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ownerId")
    @JsonProperty("ownerId")
    User ownerId;

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


    public SongList(User ownerId, String name, boolean isPrivate, Set<Song> songs) {
        this.ownerId = ownerId;
        this.name = name;
        this.isPrivate = isPrivate;
        this.songs = songs;
    }

    private SongList(SongList.Builder builder) {
        this.id = builder.id;
        this.ownerId = builder.ownerId;
        this.name = builder.name;
        this.isPrivate = builder.isPrivate;
        this.songs = builder.songs;
    }

    public SongList() {
    }

    public static final class Builder {
        private int id;
        private User ownerId;
        private String name;
        private boolean isPrivate;
        private Set<Song> songs;

        private Builder() {
        }

        public SongList.Builder withId(int id) {
            this.id = id;
            return this;
        }

        public SongList.Builder withOwnerId(User ownerId) {
            this.ownerId = ownerId;
            return this;
        }

        public SongList.Builder withName(String name) {
            this.name = name;
            return this;
        }

        public SongList.Builder withIsPrivate(boolean isPrivate) {
            this.isPrivate = isPrivate;
            return this;
        }

        public SongList.Builder withSongs(Set<Song> songs) {
            this.songs = songs;
            return this;
        }

        public SongList build() {
            return new SongList(this);
        }
    }

}
