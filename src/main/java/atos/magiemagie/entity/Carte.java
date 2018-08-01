package atos.magiemagie.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * @author Administrateur
 */
@Entity
public class Carte implements Serializable {
    //////////////////////////////////////////////
    ///////  Constructeur(s)    
    //////////////////////
    public Carte(){}
    public Carte(TypeCarte type) {
        this.type = type;
        this.joueur = null;
    }
    public Carte(TypeCarte type, Joueur joueur) {
        this.type = type;
        this.joueur = joueur;
    }
    
    /////////////////////////////////
    // AutoIncrement ID
    /////////////////////////////////
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /////////////////////////////////
    //  Enum
    /////////////////////////////////
    public enum TypeCarte {
    CORNE_DE_LICORNE,
    MANDRAGORE,
    BAVE_DE_CRAPAUD,
    LAPIS_LAZULI,
    AILE_DE_CHAUVE_SOURIS
    };
    
    ////////////////////////////////////////////////
    /// Attribut(s)
    ////////////////////
    @Column(name = "Type", nullable = false)
    @Enumerated(EnumType.STRING)
    private TypeCarte type;
    
    /////////////////////////////////////////
    ///////  Relations    
    //////////////////////
    @JoinColumn
    @ManyToOne
    private Joueur joueur;
    
    /////////////////////////////////////////////
    ///////  GET & SET  
    ////////////////////// 
    public TypeCarte getType() {
        return type;
    }
    public void setType(TypeCarte type) {
        this.type = type;
    }
    public Joueur getJoueur() {
        return joueur;
    }
    public void setJoueur(Joueur joueur) {    
        this.joueur = joueur;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    
    /////////////////////////////////////////////
    ///////  @Override -
    //////////////////////
    @Override
    public String toString() {
        return "atos.main.entity.Carte[ id=" + id + " ]";
    }
}
