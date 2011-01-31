package no.uib.ii.deliverandum.beans.command;

import no.uib.ii.deliverandum.beans.Delivery;
import no.uib.ii.deliverandum.beans.DeliveryState;
import no.uib.ii.deliverandum.beans.User;
import lombok.Data;

@Data
public class AdminDeliveryCommand {
    
    private String assignedTo;
    private String grade;
    private String gradeComment;
    private boolean closed;

    public AdminDeliveryCommand() {
    }

    public AdminDeliveryCommand(Delivery delivery) {
        User assignedToUser = delivery.getAssignedTo();
        if (assignedToUser != null) {
            assignedTo = assignedToUser.getUsername();
        }
        grade = delivery.getGrade();
        gradeComment = delivery.getGradeComment();
        closed = delivery.getState() == DeliveryState.EVALUATED;
    }
    
}
