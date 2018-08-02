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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Administrateur
 */
@WebServlet(name = "RejoindrePartieServlet", urlPatterns = {"/rejoindre-partie-servlet"})
public class RejoindrePartieServlet extends AutowireServlet {
    
    @Autowired
    PartieService partieService;
    
    Long idPartie;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        idPartie = Long.parseLong(req.getParameter("idPartie"));
        System.out.println("Id Partie = " + idPartie);
        Partie partie = partieService.getPartie(idPartie);
        
        
        req.setAttribute("partieRejoint", partie);
        req.getRequestDispatcher("REJOINDRE_PARTIE.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long idJoueurPrincipal = partieService.rejoindrePartie(req.getParameter("pseudoJoueur"), req.getParameter("avatarJoueur"), idPartie);
        
        req.getSession().setAttribute("idPartieRejoint", idPartie);
        req.getSession().setAttribute("idJoueurPrincipal", idJoueurPrincipal);
        
        resp.sendRedirect("lobby-partie-servlet");
    }
}