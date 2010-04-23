package no.uib.ii.deliverandum.beans.command;

import lombok.Data;
import no.uib.ii.deliverandum.beans.Assignment;

import org.joda.time.DateTime;

@Data
public class AssignmentCommand {

    private String courseName;
    private int id;
    private String name;
    private boolean delete;
    
    private String beginHour;
    private String beginDate;
    private String endHour;
    private String endDate;
    private String beginDateFull;
    private String endDateFull;
    
    public AssignmentCommand() {
    }
    
    public AssignmentCommand(String courseName) {
        setCourseName(courseName);
    }
    
    public AssignmentCommand(Assignment assignment) {
        id = assignment.getId();
        courseName = assignment.getCourseName();
        name = assignment.getName();
        beginDate = toFriendlyDate(assignment.getBeginTime());
        endDate = toFriendlyDate(assignment.getEndTime());
        beginDateFull = toIsoDate(assignment.getBeginTime());
        endDateFull = toIsoDate(assignment.getBeginTime());
        beginHour = Integer.toString(assignment.getBeginTime().getHourOfDay());
        endHour = Integer.toString(assignment.getEndTime().getHourOfDay());
    }
    
    private String toFriendlyDate(DateTime dateTime) {
        return dateTime.getDayOfMonth() + "/" + dateTime.getMonthOfYear() + " " + dateTime.getYear();
    }

    private String toIsoDate(DateTime dateTime) {
        return dateTime.getYear() + "-" + dateTime.getMonthOfYear() + "-" + dateTime.getDayOfMonth();
    }

    public Assignment toAssignment() {
        DateTime beginTime = new DateTime(beginDateFull + "T" + beginHour + ":00:00.000");
        DateTime endTime = new DateTime(endDateFull + "T" + endHour + ":00:00.000");
        Assignment assignment = new Assignment(beginTime, endTime);
        assignment.setName(name);
        assignment.setId(id);
        assignment.setCourseName(courseName);
        return assignment;
    }
    
}
