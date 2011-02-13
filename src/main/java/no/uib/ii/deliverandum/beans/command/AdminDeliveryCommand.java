package no.uib.ii.deliverandum.beans.command;

import org.springframework.web.multipart.MultipartFile;

import no.uib.ii.deliverandum.beans.Delivery;
import no.uib.ii.deliverandum.beans.DeliveryState;
import no.uib.ii.deliverandum.beans.User;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AdminDeliveryCommand {
    
    private String assignedTo;
    private String grade;
    private String gradeComment;
    private MultipartFile gradingAttachment;
    private boolean closed;

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
