package no.uib.ii.deliverandum.service;

import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

@Service
public class CourseMapper {
    
    private BiMap<String, String> nameToIdMap = HashBiMap.create(2);
    
    @Value("${app.courseNames}")
    public void setCourseMapping(String mappings) {
        String[] mappingSplit = mappings.split(",");
        for (String mapping : mappingSplit) {
            String[] parts = mapping.split(":");
            nameToIdMap.put(parts[0], parts[1]);
        }
    }
    
    public String getCourseId(String courseName) {
        return nameToIdMap.get(courseName);
    }
    
    public String getCourseName(String courseId) {
        return nameToIdMap.inverse().get(courseId);
    }

    public boolean containsCourseName(String courseName) {
        return nameToIdMap.containsKey(courseName);
    }
    
    public Set<String> getCourseNames() {
        return nameToIdMap.keySet();
    }
    
}
