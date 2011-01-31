package no.uib.ii.deliverandum.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import no.uib.ii.deliverandum.beans.Assignment;
import no.uib.ii.deliverandum.beans.Delivery;
import no.uib.ii.deliverandum.beans.User;
import no.uib.ii.deliverandum.persistence.AssignmentDao;
import no.uib.ii.deliverandum.persistence.DeliveryDao;
import no.uib.ii.deliverandum.service.UserManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class UserDeliveriesController {

    static final String PATH = "admin/deliveries/user";

    @Autowired private DeliveryDao deliveryDao;
    @Autowired private AssignmentDao assignmentDao;
    @Autowired private UserManager userManager;
    
    @RequestMapping("/{courseName}/" + PATH + "/{username}.html")
    public ModelAndView listAllDeliveries(
            @PathVariable String courseName,
            @PathVariable String username,
            HttpServletRequest req) {
        User student = userManager.getUser(username);
        Map<Assignment, List<Delivery>> assignmentMap = new HashMap<Assignment, List<Delivery>>();
        List<Assignment> assignments = assignmentDao.getAssignments(courseName);
        for (Assignment assignment : assignments) {
            List<Delivery> deliveries = deliveryDao.getDeliveries(courseName, assignment.getId(), student.getUsername(), null);
            if (deliveries != null && deliveries.size() > 0) {
                assignmentMap.put(assignment, deliveries);
            }
        }
        ModelAndView mav = new ModelAndView(PATH);
        mav.addObject("courseName", courseName);
        mav.addObject("student", student);
        mav.addObject("assignmentMap", assignmentMap);
        return mav;
    }
    
}
