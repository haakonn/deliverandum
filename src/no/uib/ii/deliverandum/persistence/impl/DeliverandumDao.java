package no.uib.ii.deliverandum.persistence.impl;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.jdbc.core.JdbcTemplate;

public abstract class DeliverandumDao {
    
    protected JdbcTemplate jdbcTemplate;
    
    @Autowired
    @Required
    public void setDataSource(DataSource deliverandumDataSource) {
        jdbcTemplate = new JdbcTemplate(deliverandumDataSource);
    }
    
}
