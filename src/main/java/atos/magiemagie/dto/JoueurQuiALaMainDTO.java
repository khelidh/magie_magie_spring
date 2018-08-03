/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package atos.magiemagie.dto;

import atos.magiemagie.entity.Joueur;

/**
 *
 * @author Administrateur
 */
public class JoueurQuiALaMainDTO {
    
    private long id;
    private String pseudo;

    public JoueurQuiALaMainDTO(long id, String pseudo) {
        this.id = id;
        this.pseudo = pseudo;
    }
    
    public JoueurQuiALaMainDTO(Joueur joueur) {
        this.id = joueur.getId();
        this.pseudo = joueur.getPseudo();
    }
    

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }
    
    
    
}
