package no.uib.ii.deliverandum.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import no.uib.ii.deliverandum.beans.User;

import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Servlet Filter implementation class Authorization
 */
public class AuthorizationFilter extends OncePerRequestFilter {

    private void accessDenied(String msg, HttpServletResponse res) {
        res.setStatus(403);
        StringBuilder buf = new StringBuilder();
        buf.append("<html><body><h1>Access denied</h1><p>Error: ");
        buf.append(msg);
        buf.append("</p></body></html>");
        try {
            res.getOutputStream().write(buf.toString().getBytes());
            res.getOutputStream().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    protected void doFilterInternal(HttpServletRequest req,
            HttpServletResponse res, FilterChain chain)
            throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");
        if (user == null) {
            accessDenied("No such user", res);
            return;
        }
        if (!user.isAdmin()) {
            String path = req.getRequestURI();
            if (path.contains("/admin/")) {
                accessDenied("Administrator access required for this resource.", res);
                return;
            }
        }
        chain.doFilter(req, res);
    }

}
