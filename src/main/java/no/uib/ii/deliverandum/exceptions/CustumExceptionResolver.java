package no.uib.ii.deliverandum.exceptions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

@Component
public class CustumExceptionResolver extends SimpleMappingExceptionResolver {

    @Override
    protected ModelAndView doResolveException(HttpServletRequest req,
            HttpServletResponse res, Object handler, Exception e) {
        e.printStackTrace();
        res.setStatus(500);
        ModelAndView mav = new ModelAndView("error");
        mav.addObject("message", e.getClass());
        return mav;
    }
    
}
