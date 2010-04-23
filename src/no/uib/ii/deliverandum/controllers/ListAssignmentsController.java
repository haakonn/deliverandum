package no.uib.ii.deliverandum.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import no.uib.ii.deliverandum.beans.Assignment;
import no.uib.ii.deliverandum.persistence.AssignmentDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ListAssignmentsController {
    
    static final String PATH = "admin/assignments/list";
    
    @Autowired private AssignmentDao assignmentDao;
    
    @RequestMapping("/{courseName}/" + PATH + ".html")
    public ModelAndView listAssignments(
            @PathVariable String courseName,
            HttpServletRequest req) {
        List<Assignment> assignments = assignmentDao.getAssignments(courseName);
        ModelAndView mav = new ModelAndView(PATH);
        mav.addObject("courseName", courseName);
        mav.addObject("assignments", assignments);
        return mav;
    }

}
