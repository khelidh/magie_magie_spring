/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package atos.magiemagie.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 *
 * @author Administrateur
 */
@Entity
public class Partie implements Serializable {

    public Partie() {
        this.nom = "Partie";
        this.etat = EtatPartie.EN_PREPARATION;
    }
    public Partie(String nom) {
        this.nom = nom;
        this.etat = EtatPartie.EN_PREPARATION;

    }
    public Partie(String nom, EtatPartie etat) {
        this.nom = nom;
        this.etat = etat;
    }
    //////////////////////
    //  Enum
    /////////////////////
    public enum EtatPartie {
        EN_PREPARATION,
        EN_COURS,
        TERMINEE,
        CRASHED
    }
    /////////////////////////////////
    // CONSTANTES
    /////////////////////////////////
    public static final int PARTIE_NOMBRE_CARTE_DEBUT = 7;
    
     /////////////////////////////////
    // AutoIncrement ID
    /////////////////////////////////
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    ////////////////////////////////////////////////
    /// Attribut(s)
    ////////////////////
    private String nom; 
    @Enumerated(EnumType.STRING)
    private EtatPartie etat;

    //////////////////////
    ///////  Relations    
    //////////////////////
    @OneToMany(mappedBy = "partie")
    private List<Joueur> joueurs = new ArrayList<>();
    
    /////////////////////////////////////////////
    ///////  GET & SET  
    //////////////////////
    public String getNom() {
        return nom;
    }
    public void setNom(String nom) {
        this.nom = nom;
    }
    public List<Joueur> getJoueurs() {
        return joueurs;
    }
    public void setJoueurs(List<Joueur> joueurs) {
        this.joueurs = joueurs;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public EtatPartie getEtat() {
        return etat;
    }
    public void setEtat(EtatPartie etat) {
        this.etat = etat;
    }
    /////////////////////////////////////////////
    ///////  @Override -
    //////////////////////
    @Override
    public String toString() {
        return "atos.main.entity.Partie[ id=" + id + " ]";
    }   
}
