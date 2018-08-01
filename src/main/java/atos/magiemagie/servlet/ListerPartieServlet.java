package atos.magiemagie.servlet;

import atos.magiemagie.entity.Partie;
import atos.magiemagie.service.PartieService;
import atos.magiemagie.spring.AutowireServlet;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Administrateur
 */
@WebServlet(name = "ListerPartieServlet", urlPatterns = {"/lister-partie-servlet"})
public class ListerPartieServlet extends AutowireServlet {
    
    @Autowired
    private PartieService partieService;
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        
        List<Partie> parties = partieService.getPartiesEnPrepapration();
        if (parties != null)
            req.setAttribute("parties", parties);
        
        req.getRequestDispatcher("LISTE_PARTIE.jsp").forward(req, resp);
    }
}
