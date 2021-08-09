package htwb.ai.dao;

import htwb.ai.model.SongList;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class SongListDao implements ISongListDao {

    @Autowired
    private SessionFactory sessionFactory;

    public SongListDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public SongList get(int id) {
        Session session = sessionFactory.getCurrentSession();
        Transaction transaction = session.beginTransaction();
        SongList songList = session.get(SongList.class, id);
        transaction.commit();
        return songList;
    }

    @Override
    public List<SongList> getAllByUser(String ownerId, Boolean isOwner) {
        Session session = sessionFactory.getCurrentSession();
        Transaction transaction = session.beginTransaction();
        String hql;
        if (isOwner) {
            hql = String.format("FROM SongList as s WHERE s.ownerId=\'%s\'", ownerId);
        } else {
            hql = String.format("FROM SongList as s WHERE s.ownerId=\'%s\' and s.isPrivate = false", ownerId);
        }
        List<SongList> list = session.createQuery(hql).list();
        transaction.commit();
        return list;
    }

    @Override
    public int deleteSongList(int id) {
        Session session = sessionFactory.getCurrentSession();
        Transaction transaction = session.beginTransaction();

        String hql = String.format("DELETE FROM SongList s WHERE s.id=%d", id);
        int numDeleted = session.createQuery(hql).executeUpdate();
        transaction.commit();
        return numDeleted;
    }

    @Override
    public int save(SongList sl) {
        Session session = sessionFactory.getCurrentSession();
        Transaction transaction = session.beginTransaction();
        session.save(sl);
        transaction.commit();
        return sl.getId();
    }
}
