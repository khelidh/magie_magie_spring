/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package atos.magiemagie.servlet;

import atos.magiemagie.entity.Partie;
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
@WebServlet(name = "CreerPartieServlet", urlPatterns = {"/creer-partie-servlet"})
public class CreerPartieServlet extends AutowireServlet {
    
    PartieService partieService = new PartieService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("CREER_PARTIE.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        
        
        
        String nomPartie = req.getParameter("nomPartie");
        String pseudoJoueur = req.getParameter("pseudoJoueur");
        String avatarJoueur = req.getParameter("avatarJoueur");
        
        Partie partie = partieService.creer(nomPartie, pseudoJoueur, avatarJoueur);
        req.getSession().setAttribute("idPartieRejoint", partie.getId());
        req.getSession().setAttribute("idJoueurPrincipal", partie.getJoueurs().get(0));
        
        resp.sendRedirect("lister-partie-servlet");
    }
    
    
    
    
}
