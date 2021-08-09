package htwb.ai.service;

import htwb.ai.model.User;

public interface IUserService {

    public User getUserByUserId(String id);

    public User insertUser(User user);

}
