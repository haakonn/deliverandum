package no.uib.ii.deliverandum.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.activation.MimetypesFileTypeMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import no.uib.ii.deliverandum.beans.DeliveredFile;
import no.uib.ii.deliverandum.beans.Delivery;
import no.uib.ii.deliverandum.beans.User;
import no.uib.ii.deliverandum.persistence.DeliveryDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class GetFileController extends ControllerBase {

    static final String PATH = "file";
    
    private final MimetypesFileTypeMap mimetypes = new MimetypesFileTypeMap();
    
    @Autowired private DeliveryDao deliveryDao;
    
    @RequestMapping("/{courseName}/" + PATH + ".html")
    public ModelAndView getFile(
            @PathVariable String courseName,
            @RequestParam("assignment") int assignmentId,
            @RequestParam("name") String fileName,
            @RequestParam("user") String username,
            HttpServletRequest req,
            HttpServletResponse res) {
        User user = (User) req.getSession().getAttribute("user");
        if (!user.isAdmin() && !user.getUsername().equals(username)) {
            return permissionDenied(res, "Du har ikke tilgang til denne filen.");
        }
        List<Delivery> deliveries = deliveryDao.getDeliveries(courseName, assignmentId, username, null);
        if (deliveries == null || deliveries.size() < 1) {
            return resourceNotFound(res, "Ukjent innlevering");
        }
        Delivery delivery = deliveries.get(0);
        // search linearly for the requested file:
        File file = null;
        for (DeliveredFile dFile : delivery.getFiles()) {
            if (dFile.getFile().getName().equals(fileName)) {
                file = dFile.getFile();
                break;
            }
        }
        if (file == null) {
            return resourceNotFound(res, "Ukjent fil");
        }
        res.setContentType(mimetypes.getContentType(file));
        res.setHeader ("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"");
        FileInputStream in = null;
        try {
            OutputStream out = res.getOutputStream();
            in = new FileInputStream(file);
            int length;
            byte[] buf = new byte[4096];
            while ((length = in.read(buf)) != -1) {
                out.write(buf, 0, length);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
    
}

