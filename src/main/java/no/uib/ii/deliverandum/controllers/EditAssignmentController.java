package no.uib.ii.deliverandum.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import no.uib.ii.deliverandum.beans.Assignment;
import no.uib.ii.deliverandum.beans.command.AssignmentCommand;
import no.uib.ii.deliverandum.persistence.AssignmentDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class EditAssignmentController extends ControllerBase {
    
    private static final String PATH = "admin/assignments/edit";
    
    @Autowired private AssignmentDao assignmentDao;
    
    @RequestMapping(value = "/{courseName}/" + PATH + "/{assignmentId}.html", method = RequestMethod.GET)
    public ModelAndView showForm(
            @PathVariable String courseName,
            @PathVariable int assignmentId,
            HttpServletRequest req,
            HttpServletResponse res) {
        Assignment assignment = assignmentDao.getAssignment(assignmentId);
        if (assignment == null) {
            return resourceNotFound(res, "Oppgaven finnes ikke!");
        }
        AssignmentCommand command = new AssignmentCommand(assignment);
        ModelAndView mav = new ModelAndView(PATH);
        mav.addObject("courseName", courseName);
        mav.addObject("command", command);
        return mav;
    }
    
    @RequestMapping(value = "/{courseName}/" + PATH + "/{assignmentId}.html", method = RequestMethod.POST)
    public ModelAndView createNewAssignment(
            @PathVariable String courseName,
            @PathVariable int assignmentId,
            @ModelAttribute("command") AssignmentCommand command,
            HttpServletRequest req) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("courseName", courseName);
        if (command.isDelete()) {
            assignmentDao.delete(command.toAssignment());
            mav.setView(new RedirectView("../list.html"));
            return mav;
        }
        assignmentDao.persist(command.toAssignment());
        mav.setViewName(PATH);
        mav.addObject("status", "Endringene ble lagret.");
        return mav;
    }
    
}
