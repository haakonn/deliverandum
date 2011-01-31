package no.uib.ii.deliverandum.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.validator.constraints.Email;

import lombok.Data;


@Data
public class User implements Serializable {
    
    private static final long serialVersionUID = -2676899374869922923L;
    
    private String username;
    private String firstName;
    private String lastName;
    @Email private String email;
    private boolean admin;
    private final List<String> courses = new ArrayList<String>(1);
    
    public void addCourse(String courseId) {
        courses.add(courseId);
    }
    
    public boolean subscribesTo(String courseId) {
        return courses.contains(courseId);
    }
    
    public String getFullName() {
        if (firstName == null || lastName == null) {
            return username;
        } else {
            return getFirstName() + " " + getLastName();
        }
    }
    
}
