package no.uib.ii.deliverandum.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import no.uib.ii.deliverandum.beans.User;
import no.uib.ii.deliverandum.persistence.UserDao;
import no.uib.ii.deliverandum.service.AdminManager;
import no.uib.ii.deliverandum.service.CourseMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * This is a custom authentication filter based on a non-standard
 * single-signon method. Usage is not recommended.
 * TODO: support standard login using Spring Security.
 * 
 * @author haakon
 */
@Component
public class CookieAuthenticationFilter extends OncePerRequestFilter {
    
    @Autowired private AdminManager adminManager;
    @Autowired private CourseMapper courseMapper;
    @Autowired private UserDao userDao;
    
    private String mockAuthentication = null;
    
    @Value("${app.mockauth.session}")
    public void setMockAuthentication(String mockAuth) {
        if (mockAuth != null && mockAuth.contains(":")) {
            mockAuthentication = mockAuth;
        }
    }
    
    private User getUser(String authString) {
        String[] parts = authString.split(":");
        User user = new User();
        user.setUsername(parts[0]);
        for (int i = 1; i < parts.length; i++) {
            user.addCourse(courseMapper.getCourseName(parts[i]));
        }
        user.setAdmin(adminManager.isAdmin(user.getUsername()));
        userDao.augmentUser(user);
        return user;
    }
    
    @Override
    protected void doFilterInternal(HttpServletRequest req,
            HttpServletResponse res, FilterChain chain)
            throws ServletException, IOException {
        User user = null;
        String ssoInfo = null;
        String sessionId = null;
        if (mockAuthentication != null) {
            ssoInfo = mockAuthentication;
        } else {
            System.out.println("BEGIN COOKIES");
            Cookie[] cookies = req.getCookies();
            for (Cookie cookie : cookies) {
                System.out.println("* Cookie: " + cookie.getName() + " = " + cookie.getValue());
                if ("JSESSIONID".equals(cookie.getName())) {
                    String ssoProp = System.getProperty("dpg2.session." + cookie.getValue());
                    if (ssoProp != null) {
                        ssoInfo = ssoProp;
                        sessionId = cookie.getValue();
                    }
                }
            }
            System.out.println("END COOKIES");
        }
        System.out.println("CHOSEN COOKIE: " + ssoInfo);
        if (ssoInfo != null) {
            user = getUser(ssoInfo);
            req.getSession().setAttribute("user", user);
        } else {
            System.out.println("Found JSESSIONID " + sessionId + ", but no SSO property!");
        }
        System.out.println("Authenticated user: " + user);
        chain.doFilter(req, res);
    }

}
