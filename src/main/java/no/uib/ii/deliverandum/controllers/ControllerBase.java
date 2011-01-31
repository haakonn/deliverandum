package no.uib.ii.deliverandum.controllers;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

abstract class ControllerBase {
    
    protected ModelAndView resourceNotFound(HttpServletResponse res, String msg) {
        return error(res, 404, msg);
    }
    
    protected ModelAndView permissionDenied(HttpServletResponse res, String msg) {
        return error(res, 403, msg);
    }
    
    protected ModelAndView internalServerError(HttpServletResponse res, String msg) {
        return error(res, 500, msg);
    }

    protected ModelAndView error(HttpServletResponse res, int code, String msg) {
        ModelAndView mav = new ModelAndView("error");
        mav.addObject("message", msg);
        res.setStatus(code);
        return mav;
        
    }
    
}
