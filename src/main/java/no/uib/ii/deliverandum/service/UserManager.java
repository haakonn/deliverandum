package no.uib.ii.deliverandum.service;

import no.uib.ii.deliverandum.beans.User;
import no.uib.ii.deliverandum.persistence.UserDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserManager {

    @Autowired private UserDao userDao;
    @Autowired private AdminManager adminManager;

    public User getUser(String username) {
        User user = new User();
        user.setUsername(username);
        user.setAdmin(adminManager.isAdmin(username));
        userDao.augmentUser(user);
        return user;
    }
    
}
