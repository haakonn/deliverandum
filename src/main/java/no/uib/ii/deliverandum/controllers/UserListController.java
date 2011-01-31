package no.uib.ii.deliverandum.controllers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import no.uib.ii.deliverandum.beans.Delivery;
import no.uib.ii.deliverandum.beans.User;
import no.uib.ii.deliverandum.persistence.DeliveryDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class UserListController {
    
    private static final String PATH = "/admin/users";

    @Autowired private DeliveryDao deliveryDao;

    @RequestMapping("/{courseName}/" + PATH + ".html")
    public ModelAndView listAllDeliveries(
            @PathVariable String courseName,
            HttpServletRequest req) {
        List<Delivery> deliveries = deliveryDao.getDeliveries(courseName, null, null, null);
        Set<User> students = new HashSet<User>();
        for (Delivery delivery : deliveries) {
            students.add(delivery.getDeliveredBy());
        }
        ModelAndView mav = new ModelAndView(PATH);
        mav.addObject("courseName", courseName);
        mav.addObject("students", students);
        return mav;
    }
    
}
