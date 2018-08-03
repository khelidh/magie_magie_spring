/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package atos.magiemagie.controller;

import atos.magiemagie.dto.JoueurQuiALaMainDTO;
import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author Administrateur
 */
@Controller
public class AjaxController {
    
    @RequestMapping(value = "ajax-dealer", method = RequestMethod.GET)
    @ResponseBody
    public JoueurQuiALaMainDTO determineDealer(HttpSession session){
        JoueurQuiALaMainDTO joueur = new JoueurQuiALaMainDTO(1L, "Jojo");
        return joueur;
    }
}
