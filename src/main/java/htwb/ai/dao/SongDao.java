package htwb.ai.dao;

import htwb.ai.model.Song;
import htwb.ai.model.User;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


public class SongDao implements ISongDao<Song> {

    @Autowired
    private SessionFactory sessionFactory;

    public SongDao(SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Song get(int id) {
        Session session = sessionFactory.getCurrentSession();
        Transaction transaction = session.beginTransaction();
        Song song = session.get(Song.class, id);
        transaction.commit();
        return song;
    }

    @Override
    public List<Song> getAll() {
        Session session = sessionFactory.getCurrentSession();
        Transaction transaction = session.beginTransaction();
        List<Song> songs = session.createQuery("FROM Song", Song.class).list();
        transaction.commit();
        return songs;
    }

    @Override
    public int save(Song song) {
        Session session = sessionFactory.getCurrentSession();
        Transaction transaction = session.beginTransaction();
        session.save(song);
        transaction.commit();
        return song.getId();
    }

    @Override
    public void updateSong(Song song) {
        int songid = song.getId();
        Song songToUpdate = get(songid);
        Session session = sessionFactory.getCurrentSession();
        Transaction transaction = session.beginTransaction();
        session.evict(songToUpdate);
        session.merge(song);
        transaction.commit();
    }

    //TODO orphan removal for songlist
    @Override
    public int deleteSong(int songId) {
        Session session = sessionFactory.getCurrentSession();
        Transaction transaction = session.beginTransaction();
        String hql = "DELETE FROM Song WHERE id=" + songId;
        int numDeleted = session.createQuery(hql).executeUpdate();
        transaction.commit();
        return numDeleted;
    }
}
