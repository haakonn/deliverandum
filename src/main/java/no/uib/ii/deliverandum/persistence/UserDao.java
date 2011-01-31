package no.uib.ii.deliverandum.persistence;

import no.uib.ii.deliverandum.beans.User;

/**
 * Simple DAO to "augment" a User with personal information
 * from an external datasource.
 * @author haakon
 */
public interface UserDao {
    
    public void augmentUser(User user);
    
}
