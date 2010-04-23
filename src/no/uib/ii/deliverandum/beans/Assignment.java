package no.uib.ii.deliverandum.beans;

import lombok.Data;
import lombok.NonNull;

import org.joda.time.DateTime;

@Data
public class Assignment {
    
    private int id;
    private String courseName;
    private String name;
    @NonNull private DateTime beginTime;
    @NonNull private DateTime endTime;

}
