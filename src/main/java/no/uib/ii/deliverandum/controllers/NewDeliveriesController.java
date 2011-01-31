package no.uib.ii.deliverandum.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import no.uib.ii.deliverandum.beans.Assignment;
import no.uib.ii.deliverandum.beans.Delivery;
import no.uib.ii.deliverandum.persistence.AssignmentDao;
import no.uib.ii.deliverandum.persistence.DeliveryDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class NewDeliveriesController {

    static final String PATH = "admin/deliveries/new";

    @Autowired private DeliveryDao deliveryDao;
    @Autowired private AssignmentDao assignmentDao;
    
    @RequestMapping("/{courseName}/" + PATH + ".html")
    public ModelAndView listAllDeliveries(
            @PathVariable String courseName,
            HttpServletRequest req) {
        Map<Assignment, List<Delivery>> assignmentMap = new HashMap<Assignment, List<Delivery>>();
        List<Assignment> assignments = assignmentDao.getAssignments(courseName);
        for (Assignment assignment : assignments) {
            List<Delivery> deliveries = deliveryDao.getDeliveries(courseName, assignment.getId(), null, null);
            List<Delivery> filtered = new ArrayList<Delivery>();
            for (Delivery delivery : deliveries) {
                if (delivery.getAssignedTo() == null || delivery.getAssignedTo().getUsername().isEmpty()) {
                    filtered.add(delivery);
                }
            }
            if (filtered.size() > 0) {
                assignmentMap.put(assignment, filtered);
            }
        }
        System.out.println(assignmentMap);
        ModelAndView mav = new ModelAndView(PATH);
        mav.addObject("courseName", courseName);
        mav.addObject("assignmentMap", assignmentMap);
        return mav;
    }
    
}
