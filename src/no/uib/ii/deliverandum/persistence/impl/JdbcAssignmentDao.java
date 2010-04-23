package no.uib.ii.deliverandum.persistence.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import lombok.NonNull;
import no.uib.ii.deliverandum.beans.Assignment;
import no.uib.ii.deliverandum.persistence.AssignmentDao;

import org.joda.time.DateTime;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcAssignmentDao extends DeliverandumDao implements AssignmentDao {

    private static final String QUERY_ASSIGNMENTS =
        "SELECT id, courseName, name, beginTime, endTime FROM assignments WHERE courseName=?";

    private static final String QUERY_ASSIGNMENT =
        "SELECT id, courseName, name, beginTime, endTime FROM assignments WHERE id=?";

    private static final String INSERT_ASSIGNMENT =
        "INSERT INTO assignments (courseName, name, beginTime, endTime) " +
        "VALUES (?, ?, ?, ?) RETURNING *";

    private static final String UPDATE_ASSIGNMENT =
        "UPDATE assignments SET courseName=?, name=?, beginTime=?, endTime=? " +
        "WHERE id=? RETURNING *";

    private static final String DELETE_ASSIGNMENT = "DELETE FROM assignments WHERE id=?";

    private final RowMapper<Assignment> rowMapper = new AssignmentRowMapper();
    
    @Override
    public Assignment persist(Assignment assignment) {
        if (assignment.getId() == 0 || getAssignment(assignment.getId()) == null) {
            return insertAssignment(assignment);
        } else {
            return updateAssignment(assignment);
        }
    }

    private Assignment updateAssignment(Assignment assignment) {
        return jdbcTemplate.query(UPDATE_ASSIGNMENT, getUpdateArgs(assignment), rowMapper).get(0);
    }

    private Assignment insertAssignment(Assignment assignment) {
        return jdbcTemplate.query(INSERT_ASSIGNMENT, getInsertArgs(assignment), rowMapper).get(0);
    }

    private Object[] getUpdateArgs(Assignment assignment) {
        Object[] args = {
                assignment.getCourseName(),
                assignment.getName(),
                new Timestamp(assignment.getBeginTime().getMillis()),
                new Timestamp(assignment.getEndTime().getMillis()),
                assignment.getId()
        };
        return args;
    }

    private Object[] getInsertArgs(Assignment assignment) {
        Object[] args = {
                assignment.getCourseName(),
                assignment.getName(),
                new Timestamp(assignment.getBeginTime().getMillis()),
                new Timestamp(assignment.getEndTime().getMillis())
        };
        return args;
    }

    @Override
    public void delete(Assignment assignment) {
        Object[] args = { assignment.getId() };
        jdbcTemplate.update(DELETE_ASSIGNMENT, args);
    }
    
    private void sortAssignments(List<Assignment> assignments) {
        Collections.sort(assignments, new Comparator<Assignment>() {
            @Override
            public int compare(Assignment a1, Assignment a2) {
                return a1.getBeginTime().compareTo(a2.getBeginTime());
            }
        });
    }

    @Override
    public List<Assignment> getAssignments(@NonNull String courseName) {
        Object[] args = { courseName };
        List<Assignment> assignments = jdbcTemplate.query(QUERY_ASSIGNMENTS, args, rowMapper);
        sortAssignments(assignments);
        return assignments;
    }
    
    @Override
    public Assignment getAssignment(int assignmentId) {
        Object[] args = { assignmentId };
        List<Assignment> assigments = jdbcTemplate.query(QUERY_ASSIGNMENT, args, rowMapper);
        if (assigments.isEmpty()) {
            return null;
        } else {
            return assigments.get(0);
        }
    }

    @Override
    public List<Assignment> getCurrentAssignments(@NonNull String courseName) {
        List<Assignment> currentAssignments = new ArrayList<Assignment>();
        for (Assignment a : getAssignments(courseName)) {
            if (a.getBeginTime().isBeforeNow() && a.getEndTime().isAfterNow()) {
                currentAssignments.add(a);
            }
        }
        sortAssignments(currentAssignments);
        return currentAssignments;
    }
    
    static class AssignmentRowMapper implements RowMapper<Assignment> {
        @Override
        public Assignment mapRow(ResultSet rs, int rowNum) throws SQLException {
            Assignment assignment = new Assignment(
                    new DateTime(rs.getTimestamp("beginTime")),
                    new DateTime(rs.getTimestamp("endTime")));
            assignment.setId(rs.getInt("id"));
            assignment.setCourseName(rs.getString("courseName"));
            assignment.setName(rs.getString("name"));
            return assignment;
        }
    }

}
