package no.uib.ii.deliverandum.controllers;

import javax.servlet.http.HttpServletRequest;

import no.uib.ii.deliverandum.beans.command.AssignmentCommand;
import no.uib.ii.deliverandum.persistence.AssignmentDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class NewAssignmentController {
    
    private static final String PATH = "admin/assignments/new";
    
    @Autowired private AssignmentDao assignmentDao;
    
    @RequestMapping(value = "/{courseName}/" + PATH + ".html", method = RequestMethod.GET)
    public ModelAndView showForm(
            HttpServletRequest req,
            @PathVariable String courseName) {
        ModelAndView mav = new ModelAndView(PATH);
        mav.addObject("courseName", courseName);
        mav.addObject("user", req.getSession().getAttribute("user"));
        mav.addObject("command", new AssignmentCommand(courseName));
        return mav;
    }
    
    @RequestMapping(value = "/{courseName}/"  + PATH + ".html", method = RequestMethod.POST)
    public ModelAndView createNewAssignment(
            @ModelAttribute("command") AssignmentCommand command,
            HttpServletRequest req,
            @PathVariable String courseName) {
        assignmentDao.persist(command.toAssignment());
        ModelAndView mav = new ModelAndView(PATH);
        mav.addObject("courseName", courseName);
        mav.addObject("status", "Oppgaven ble opprettet.");
        return mav;
    }
    
}
