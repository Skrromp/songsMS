package htwb.ai.dao;

import htwb.ai.model.User;

public interface IUserDao {

    /**
     *
     * @param userId id of the user in the database
     * @return the User object
     */
    public User getUserByUserId (String userId);

    /**
         *
         * @param user user that will be saved in the database
         * @return the User object
         */
    public User save(User user);
}
