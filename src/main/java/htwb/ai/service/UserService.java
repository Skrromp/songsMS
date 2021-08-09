package htwb.ai.service;

import htwb.ai.dao.UserDao;
import htwb.ai.model.User;
import org.springframework.beans.factory.annotation.Autowired;

public class UserService implements IUserService {

    @Autowired
    private UserDao userDao;

    @Override
    public User getUserByUserId(String id) {
        return userDao.getUserByUserId(id);
    }

    @Override
    public User insertUser(User user) {
        return userDao.save(user);
    }

}
