package no.uib.ii.deliverandum.beans;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

import org.joda.time.DateTime;

@Data
public class Delivery {
    
    private int id;
    private Assignment assignment;
    private User deliveredBy;
    private User assignedTo;
    private DateTime deliveredAt = new DateTime();
    private DeliveryState state;
    private String grade;
    private String gradeComment;
    private List<DeliveredFile> files = new ArrayList<DeliveredFile>();
    
    public void deliveredNow() {
        deliveredAt = new DateTime();
    }
}
