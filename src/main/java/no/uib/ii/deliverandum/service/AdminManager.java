package no.uib.ii.deliverandum.service;

import java.util.HashSet;
import java.util.Set;

import no.uib.ii.deliverandum.beans.User;
import no.uib.ii.deliverandum.persistence.UserDao;

import lombok.Data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Data
public class AdminManager {

    private final Set<String> adminNames = new HashSet<String>();
    private final Set<User> admins = new HashSet<User>();
    
    @Autowired private UserDao userDao;
    
    @Value("${app.admins}")
    public void setAdminString(String adminString) {
        String[] adminParts = adminString.split(",");
        for (String admin : adminParts) {
            adminNames.add(admin);
            User user = new User();
            user.setUsername(admin);
            userDao.augmentUser(user);
            admins.add(user);
        }
    }

    public boolean isAdmin(String username) {
        return adminNames.contains(username);
    }
    
}
