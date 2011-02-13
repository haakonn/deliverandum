package no.uib.ii.deliverandum.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.SneakyThrows;
import no.uib.ii.deliverandum.beans.Assignment;
import no.uib.ii.deliverandum.beans.Delivery;
import no.uib.ii.deliverandum.beans.DeliveryState;
import no.uib.ii.deliverandum.beans.User;
import no.uib.ii.deliverandum.beans.command.UserDeliveryCommand;
import no.uib.ii.deliverandum.persistence.AssignmentDao;
import no.uib.ii.deliverandum.persistence.DeliveryDao;
import no.uib.ii.deliverandum.persistence.DeliveryFileDao;
import no.uib.ii.deliverandum.service.CourseMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class UserAreaController extends ControllerBase {

    static final String PATH = "userarea";
    
    @Autowired private CourseMapper courseMapper;
    @Autowired private DeliveryDao deliveryDao;
    @Autowired private AssignmentDao assignmentDao;
    @Autowired private DeliveryFileDao fileDao;
    @Value("${app.courseAdminEmail}") private String courseAdminEmail;
    
    @RequestMapping("/{courseName}/index.html")
    public ModelAndView index(
            @PathVariable String courseName,
            HttpServletRequest req,
            HttpServletResponse res) {
        User user = (User) req.getSession().getAttribute("user");
        if (user == null) {
        	// TODO
        }
        String courseId = courseMapper.getCourseId(courseName);
        if (courseId != null) {
            ModelAndView mav = makeModelAndView(courseName, user);
            System.out.println(((User) req.getSession().getAttribute("user")).getUsername());
            return mav;
        } else {
            return resourceNotFound(res, "Kurset eksisterer ikke.");
        }
    }
    
    @SneakyThrows(IOException.class)
    @RequestMapping(value = "/{courseName}/index.html", method = RequestMethod.POST)
    public ModelAndView handleFormUpload(
            HttpServletRequest req,
            HttpServletResponse res,
            @PathVariable String courseName,
            @ModelAttribute("command") UserDeliveryCommand command) {
        User user = (User) req.getSession().getAttribute("user");
        MultipartFile file = command.getFile();
        if (!file.isEmpty()) {
            int assignmentId = Integer.parseInt(command.getAssignmentId());
            Assignment assignment = assignmentDao.getAssignment(assignmentId);
            Delivery delivery = getAlreadyDelivered(courseName, assignment, user);
            if (delivery != null) {
                delivery.deliveredNow();
            } else {
                delivery = command.toDelivery(assignment, user);
            }
            deliveryDao.persist(delivery);
            fileDao.persistDeliveryFile(file.getInputStream(), file.getOriginalFilename(), command.getNotes(), delivery);
            ModelAndView mav = makeModelAndView(courseName, user);
            mav.addObject("status", "Oppgaven ble levert.");
            return mav;
        } else {
            return internalServerError(res, "En feil oppstod under opplastning; vennligst trykk tilbake-knappen og prøv igjen.");
        }
    }

    private Delivery getAlreadyDelivered(String courseName, Assignment assignment, User user) {
        List<Delivery> deliveries = deliveryDao.getDeliveries(courseName, assignment.getId(), user.getUsername(), null);
        if (deliveries != null && deliveries.size() > 0) {
            return deliveries.get(0);
        }
        return null;
    }

    private ModelAndView makeModelAndView(String courseName, User user) {
        List<Assignment> currentAssignments = new ArrayList<Assignment>();
        for (Assignment assignment : assignmentDao.getCurrentAssignments(courseName)) {
            List<Delivery> deliveries = deliveryDao.getDeliveries(courseName, assignment.getId(), user.getUsername(), null);
            if (deliveries.size() > 0) {
                Delivery d = deliveries.get(0);
                if (d.getState() != DeliveryState.EVALUATED) {
                    currentAssignments.add(assignment);
                }
            } else {
                currentAssignments.add(assignment);
            }
        }
        List<Delivery> previousDeliveries = deliveryDao.getDeliveries(courseName, null, user.getUsername(), null);
        ModelAndView mav = new ModelAndView(PATH);
        mav.addObject("deliveries", previousDeliveries);
        mav.addObject("openAssignments", currentAssignments);
        mav.addObject("courseName", courseName);
        mav.addObject("courseAdminEmail", courseAdminEmail);
        mav.addObject("command", new UserDeliveryCommand());
        mav.addObject("user", user);
        return mav;
    }

}
