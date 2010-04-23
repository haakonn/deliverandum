package no.uib.ii.deliverandum.beans.command;

import lombok.Data;
import lombok.NonNull;
import no.uib.ii.deliverandum.beans.Assignment;
import no.uib.ii.deliverandum.beans.Delivery;
import no.uib.ii.deliverandum.beans.DeliveryState;
import no.uib.ii.deliverandum.beans.User;

import org.springframework.web.multipart.MultipartFile;

@Data
public class UserDeliveryCommand {
    
    private String assignmentId;
    private MultipartFile file;
    private String notes;
    
    public Delivery toDelivery(
            @NonNull Assignment assignment,
            @NonNull User deliveredBy) {
        Delivery delivery = new Delivery();
        delivery.setState(DeliveryState.RECEIVED);
        delivery.setAssignment(assignment);
        delivery.setDeliveredBy(deliveredBy);
        return delivery;
    }
    
}
