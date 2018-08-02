package atos.magiemagie.servlet;

import atos.magiemagie.entity.Joueur;
import atos.magiemagie.entity.Partie;
import atos.magiemagie.service.JoueurService;
import atos.magiemagie.service.PartieService;
import atos.magiemagie.spring.AutowireServlet;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Administrateur
 */

@WebServlet(name = "PlateauPartieServlet", urlPatterns = {"/plateau-partie-servlet"})
public class PlateauPartieServlet extends AutowireServlet {
    
    @Autowired
    PartieService partieService;
    @Autowired
    JoueurService joueurService;
    
    Long idPartie;
    Joueur joueurALaMain, joueurPrincipal;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        idPartie = (Long) req.getSession().getAttribute("idPartieRejoint");
        
        Partie partie = partieService.getPartie(idPartie);

        
        
        req.setAttribute("partie", partie);
        
        joueurALaMain = partieService.getJoueurALaMain(idPartie);
        req.setAttribute("joueurALaMain", joueurALaMain);
        
        Long idJoueurPrincipal = (Long) req.getSession().getAttribute("idJoueurPrincipal");
        joueurPrincipal = partieService.getJoueur(idJoueurPrincipal);
        
        req.setAttribute("joueurPricipal", joueurPrincipal);
        
        req.getRequestDispatcher("PLATEAU_PARTIE.jsp").forward(req, resp);
    }
}
