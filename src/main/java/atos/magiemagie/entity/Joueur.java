/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package atos.magiemagie.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 *
 * @author Administrateur
 */
@Entity
public class Joueur implements Serializable {
    //////////////////////////////////////////////
    ///////  Constructeur(s)    
    //////////////////////
    public Joueur(){}
    public Joueur(String pseudo) {
        this.etat = EtatJoueur.EN_ATTENTE;
        this.position = 0;
        this.partie = null;
        this.pseudo = pseudo;
        this.pass = "pass" + pseudo;
        this.avatar = "avatar" + pseudo;
        this.partiesGagnees = 0;
        this.partiesJouees = 0;
    }
    /////////////////////////////////
    // AutoIncrement ID
    /////////////////////////////////
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //////////////////////////////
    //  enum
    public enum EtatJoueur {
        EN_ATTENTE,
        A_LA_MAIN,
        PAS_LA_MAIN,
        EN_SOMMEIL,
        ELIMINE,
        SPECTATEUR
    }
     //////////////////////////////////////////////
    //Attributs
    ////////////////////////
    @Column(name = "Type", nullable = false)
    @Enumerated(EnumType.STRING)
    private EtatJoueur etat;
    private long position;
    private String pseudo;
    private String pass;
    private String avatar;
    private long partiesGagnees ;
    private long partiesJouees ;
    
    //////////////////////////////////////////////
    ///////  Relations    
    //////////////////////
    @JoinColumn
    @ManyToOne
    private Partie partie;
    
    @OneToMany(mappedBy = "joueur")
    private List<Carte> cartes = new ArrayList<>();
    //////////////////////////////////////////////
    ///////  GET & SET  
    //////////////////////
    public EtatJoueur getEtat() {
        return etat;
    }
    public void setEtat(EtatJoueur etat) {
        this.etat = etat;
    }
    public String getPseudo() {
        return pseudo;
    }
    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }
    public String getPass() {
        return pass;
    }
    public void setPass(String pass) {
        this.pass = pass;
    }
    public String getAvatar() {
        return avatar;
    }
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
    public long getPartiesGagnees() {
        return partiesGagnees;
    }
    public void setPartiesGagnees(long partiesGagnees) {
        this.partiesGagnees = partiesGagnees;
    }
    public long getPartiesJouees() {
        return partiesJouees;
    }
    public void setPartiesJouees(long partiesJouees) {
        this.partiesJouees = partiesJouees;
    }
    public Partie getPartie() {
        return partie;
    }
    public void setPartie(Partie partie) {
        this.partie = partie;
    }
    public List<Carte> getCartes() {
        return cartes;
    }
    public void setCartes(List<Carte> cartes) {
        this.cartes = cartes;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public long getPosition() {
        return position;
    }
    public void setPosition(long position) {
        this.position = position;
    }

    /////////////////////////////////////////////
    ///////  @Override -
    //////////////////////
    @Override
    public String toString() {
        return "atos.main.entity.Joueur[ id=" + id + " ]";
    }
}
