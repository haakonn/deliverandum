package no.uib.ii.deliverandum.persistence.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import lombok.Data;

import no.uib.ii.deliverandum.beans.User;
import no.uib.ii.deliverandum.persistence.UserDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class WebucatorUserDao implements UserDao {

    private static final String PRE_SQL = "SELECT firstname, lastname, email FROM ";
    private static final String POST_SQL = " WHERE username=?";
    private static final String GET_USER_SQL = PRE_SQL + "users" + POST_SQL;
    private static final String GET_ADMIN_SQL = PRE_SQL + "administrators" + POST_SQL;
    
    protected JdbcTemplate jdbcTemplate;
    
    @Autowired
    @Required
    public void setDataSource(DataSource webucatorDataSource) {
        jdbcTemplate = new JdbcTemplate(webucatorDataSource);
    }
    
    @Override
    public void augmentUser(User user) {
        String sql = user.isAdmin() ? GET_ADMIN_SQL : GET_USER_SQL;
        String[] args = { user.getUsername() };
        jdbcTemplate.query(sql, args, new UserRowMapper(user));
    }

}

@Data
class UserRowMapper implements RowMapper<User> {
    
    private final User user;
    
    @Override
    public User mapRow(ResultSet rs, int rowNumber) throws SQLException {
        user.setFirstName(rs.getString("firstname"));
        user.setLastName(rs.getString("lastname"));
        user.setEmail(rs.getString("email"));
        return user;
    }
    
}
