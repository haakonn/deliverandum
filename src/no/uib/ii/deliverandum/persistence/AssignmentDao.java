package no.uib.ii.deliverandum.persistence;

import java.util.List;

import lombok.NonNull;

import no.uib.ii.deliverandum.beans.Assignment;

public interface AssignmentDao {

    public Assignment persist(@NonNull Assignment assignment);
    
    public void delete(@NonNull Assignment assignment);
    
    public List<Assignment> getAssignments(@NonNull String courseName);
    
    public Assignment getAssignment(int assignmentId);
    
    public List<Assignment> getCurrentAssignments(@NonNull String courseName);
    
}
