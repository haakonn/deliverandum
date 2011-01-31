package no.uib.ii.deliverandum.persistence.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import lombok.NonNull;
import no.uib.ii.deliverandum.beans.Delivery;
import no.uib.ii.deliverandum.beans.DeliveryState;
import no.uib.ii.deliverandum.beans.User;
import no.uib.ii.deliverandum.persistence.AssignmentDao;
import no.uib.ii.deliverandum.persistence.DeliveryDao;
import no.uib.ii.deliverandum.persistence.DeliveryFileDao;
import no.uib.ii.deliverandum.service.UserManager;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcDeliveryDao extends DeliverandumDao implements DeliveryDao {

    private static final String QUERY_DELIVERY =
        "SELECT * FROM deliveries " +
        "WHERE id=?";

    private static final String QUERY_DELIVERIES =
        "SELECT deliveries.*, assignments.courseName FROM deliveries " +
        "INNER JOIN assignments ON deliveries.assignmentid=assignments.id " +
        "WHERE assignments.courseName=?";
    
    private static final String QUERY_DELIVERIES_ASSIGNMENT_FILTER = " AND assignmentId=?";
    
    private static final String QUERY_DELIVERIES_DELIVEREDBY_FILTER = " AND username=?";
    
    private static final String QUERY_DELIVERIES_ASSIGNEDTO_FILTER = " AND assignedTo=?";
    
    private static final String INSERT_DELIVERY =
        "INSERT INTO deliveries (assignmentId, username, deliveredAt, assignedTo, state, grade, gradeComment) " +
        "VALUES (?, ?, ?, ?, ?, ?, ?) RETURNING *";

    private static final String UPDATE_DELIVERY =
        "UPDATE deliveries SET assignmentId=?, username=?, deliveredAt=?, assignedTo=?, state=?, grade=?, gradeComment=? " +
        "WHERE id=? RETURNING *";

    private final RowMapper<Delivery> rowMapper = new DeliveryRowMapper();
    
    @Autowired private AssignmentDao assignmentDao;
    @Autowired private DeliveryFileDao fileDao;
    @Autowired private UserManager userManager;
    
    @Override
    public Delivery persist(@NonNull Delivery delivery) {
        if (delivery.getId() == 0 || getDelivery(delivery.getId()) == null) {
            return insertDelivery(delivery);
        } else {
            return updateDelivery(delivery);
        }
    }
    
    private Delivery updateDelivery(Delivery delivery) {
        return jdbcTemplate.query(UPDATE_DELIVERY, getUpdateArgs(delivery), rowMapper).get(0);
    }

    private Delivery insertDelivery(Delivery delivery) {
        return jdbcTemplate.query(INSERT_DELIVERY, getInsertArgs(delivery), rowMapper).get(0);
    }
    
    private static Object[] getInsertArgs(Delivery delivery) {
        Object[] args = {
                delivery.getAssignment().getId(),
                delivery.getDeliveredBy().getUsername(),
                new Timestamp(delivery.getDeliveredAt().getMillis()),
                "", // unassigned
                delivery.getState().toString(),
                delivery.getGrade(),
                delivery.getGradeComment()
        };
        return args;
    }

    private static Object[] getUpdateArgs(Delivery delivery) {
        User assignedTo = delivery.getAssignedTo();
        String assignedToUsername = assignedTo == null ? "" : assignedTo.getUsername();
        Object[] args = {
                delivery.getAssignment().getId(),
                delivery.getDeliveredBy().getUsername(),
                new Timestamp(delivery.getDeliveredAt().getMillis()),
                assignedToUsername,
                delivery.getState().toString(),
                delivery.getGrade(),
                delivery.getGradeComment(),
                delivery.getId()
        };
        return args;
    }

    @Override
    public Delivery getDelivery(@NonNull int deliveryId) {
        Object[] args = new Object[] { deliveryId };
        List<Delivery> deliveries = jdbcTemplate.query(QUERY_DELIVERY, args, rowMapper);
        if (deliveries.isEmpty()) {
            return null;
        } else {
            return deliveries.get(0);
        }
    }


    @Override
    public List<Delivery> getDeliveries(@NonNull String course, Integer assignmentId, String deliveredById, String assignedToId) {
        List<Delivery> deliveries;
        List<Object> args = new ArrayList<Object>();
        args.add(course);
        String sql = QUERY_DELIVERIES;
        if (assignmentId != null) {
            sql += QUERY_DELIVERIES_ASSIGNMENT_FILTER;
            args.add(assignmentId);
        }
        if (deliveredById != null) {
            sql += QUERY_DELIVERIES_DELIVEREDBY_FILTER;
            args.add(deliveredById);
        }
        if (assignedToId != null) {
            sql += QUERY_DELIVERIES_ASSIGNEDTO_FILTER;
            args.add(assignedToId);
        }
        deliveries = jdbcTemplate.query(sql, args.toArray(), rowMapper);
        return deliveries;
    }
    
    class DeliveryRowMapper implements RowMapper<Delivery> {
        @Override
        public Delivery mapRow(ResultSet rs, int rowNum) throws SQLException {
            String assignedToUsername = rs.getString("assignedTo");
            User assignedTo = null;
            if (assignedToUsername != null && !assignedToUsername.isEmpty()) {
                assignedTo = userManager.getUser(assignedToUsername);
            }
            Delivery delivery = new Delivery();
            delivery.setId(rs.getInt("id"));
            delivery.setAssignment(assignmentDao.getAssignment(rs.getInt("assignmentId")));
            delivery.setDeliveredBy(userManager.getUser(rs.getString("username")));
            delivery.setDeliveredAt(new DateTime(rs.getTimestamp("deliveredAt")));
            delivery.setAssignedTo(assignedTo);
            delivery.setState(DeliveryState.valueOf(rs.getString("state")));
            delivery.setGrade(rs.getString("grade"));
            delivery.setGradeComment(rs.getString("gradeComment"));
            fileDao.addFiles(delivery);
            return delivery;
        }
    }
    
}
