/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package atos.magiemagie.controller;

import atos.magiemagie.service.PartieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author Administrateur
 */
@Controller
public class PartieController {
    
    @Autowired
    private PartieService partieService;
    
    @RequestMapping(value = {"/lister-partie-servlet", "/"}, method = RequestMethod.GET)
    public String lister(Model model){
        
        model.addAttribute("parties", partieService.getPartiesEnPrepapration());
        
        return "LISTE_PARTIE.jsp";
    }
}