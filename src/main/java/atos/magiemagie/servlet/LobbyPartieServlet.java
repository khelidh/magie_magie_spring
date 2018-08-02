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
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Administrateur
 */
@WebServlet(name = "LobbyPartieServlet", urlPatterns = {"/lobby-partie-servlet"})
public class LobbyPartieServlet extends AutowireServlet {

    @Autowired
    PartieService partieService;
    
    Long idPartie;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        idPartie = (Long) req.getSession().getAttribute("idPartieRejoint");
        Partie partie = partieService.getPartie(idPartie);

        req.setAttribute("partie", partie);

        req.getRequestDispatcher("LOBBY_PARTIE.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        idPartie = (Long) req.getSession().getAttribute("idPartieRejoint");
        partieService.demarrer(idPartie);
        partieService.distribuer(idPartie);
        
        resp.sendRedirect("plateau-partie-servlet");
    }

}
