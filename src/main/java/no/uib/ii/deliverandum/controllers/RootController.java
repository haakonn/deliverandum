package no.uib.ii.deliverandum.controllers;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import no.uib.ii.deliverandum.beans.User;
import no.uib.ii.deliverandum.service.CourseMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class RootController {
    
    @Autowired private CourseMapper courseMapper;
    
    @RequestMapping("/index.html")
    public ModelAndView index(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView();
        User user = (User) request.getSession().getAttribute("user");
        if (user.isAdmin()) {
            return fillModel(user, courseMapper.getCourseNames());
        } else if (user.getCourses().size() == 1) {
            mav.setView(new RedirectView(user.getCourses().get(0) + "/index.html"));
        } else {
            return fillModel(user, user.getCourses());
        }
        return mav;
    }

    private ModelAndView fillModel(User user, Collection<String> courses) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("courseList");
        mav.addObject("courses", courses);
        mav.addObject("user", user);
        return mav;
    }
    
}
