package no.uib.ii.deliverandum.beans;

import java.io.File;

import org.joda.time.DateTime;

import lombok.Data;

@Data
public class DeliveredFile {
    
    private File file;
    private String notes;
    
    public DateTime getTimestamp() {
        return new DateTime(file.lastModified());
    }
    
}
