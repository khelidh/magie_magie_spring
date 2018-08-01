/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package atos.magiemagie.servlet.updateServlet;

import atos.magiemagie.entity.Carte;
import atos.magiemagie.entity.Joueur;
import atos.magiemagie.service.CarteService;
import atos.magiemagie.service.JoueurService;
import atos.magiemagie.service.PartieService;
import atos.magiemagie.spring.AutowireServlet;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Administrateur
 */
@WebServlet(name = "AfficherCartesServlet", urlPatterns = {"/afficher-cartes-servlet"})
public class AfficherCartesServlet extends AutowireServlet {
    PartieService partieService = new PartieService();
    JoueurService joueurService = new JoueurService();
    CarteService carteService = new CarteService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long idJoueur = (Long) req.getSession().getAttribute("idJoueurPrincipal");
        
        for (Carte.TypeCarte value : Carte.TypeCarte.values()) {
            req.setAttribute(value.toString(), carteService.getNombreCarte(idJoueur, value));
        }
        
        req.getRequestDispatcher("PLATEAU_PARTIE.jsp").forward(req, resp);
    }
    
    
    
}
