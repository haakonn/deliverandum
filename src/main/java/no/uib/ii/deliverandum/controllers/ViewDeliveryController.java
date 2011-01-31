package no.uib.ii.deliverandum.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import no.uib.ii.deliverandum.beans.Delivery;
import no.uib.ii.deliverandum.beans.User;
import no.uib.ii.deliverandum.beans.command.AdminDeliveryCommand;
import no.uib.ii.deliverandum.persistence.DeliveryDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ViewDeliveryController extends ControllerBase {
    
    private static final String PATH = "delivery";
    
    @Autowired private DeliveryDao deliveryDao;

    @RequestMapping(value = "/{courseName}/" + PATH + "/{deliveryId}.html", method = RequestMethod.GET)
    public ModelAndView viewDelivery(
            @PathVariable String courseName,
            @PathVariable int deliveryId,
            HttpServletRequest req,
            HttpServletResponse res) {
        User user = (User) req.getSession().getAttribute("user");
        Delivery delivery = deliveryDao.getDelivery(deliveryId);
        boolean denied = false;
        if (delivery == null) {
            System.out.println("WARN!!! delivery for id " + deliveryId + " == null");
            denied = true;
        } else if (!user.getUsername().equals(delivery.getDeliveredBy().getUsername()) && !user.isAdmin()) {
            System.out.println("WARN!!! user " + user.getUsername()
                    + " is not admin, and delivery id " + deliveryId + " belongs to " + delivery.getDeliveredBy().getUsername());
            denied = true;
        }
        if (denied) {
            return permissionDenied(res, "Ingen slik innlevering, eller ingen rettigheter til å se");
        }
        return makeModelAndView(courseName, delivery, new AdminDeliveryCommand(delivery));
    }
    
    private ModelAndView makeModelAndView(String courseName, Delivery delivery, AdminDeliveryCommand cmd) {
        User student = delivery.getDeliveredBy();
        ModelAndView mav = new ModelAndView(PATH);
        mav.addObject("courseName", courseName);
        mav.addObject("student", student);
        mav.addObject("delivery", delivery);
        mav.addObject("command", cmd);
        return mav;
    }
    
}
