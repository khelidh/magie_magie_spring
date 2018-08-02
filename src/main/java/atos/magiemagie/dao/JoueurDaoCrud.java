/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package atos.magiemagie.dao;

import atos.magiemagie.entity.Joueur;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author Administrateur
 */
public interface JoueurDaoCrud extends CrudRepository<Joueur, Long>{
    
    public Joueur findOneByPseudo(String pseudo);
    public List<Joueur> findAllByPartieIdAndEtat(Long idPartie, Joueur.EtatJoueur etat);  
    public List<Joueur> findAllByPartieIdAndEtatOrderByPositionAsc(Long idPartie, Joueur.EtatJoueur etat );  
    public List<Joueur> findAllByPartieIdAndIdNot(Long idPartie, Long idJoueur);
    public Joueur findOneByPartieIdAndPosition(Long idPartie, Long position);
    public Joueur findOneByPartieIdAndEtat(Long idPartie, Joueur.EtatJoueur etat);
    public Long countByPartieIdAndEtat(Long idPartie, Joueur.EtatJoueur etat);
    
    @Query("SELECT j.id FROM Joueur j"
            + " JOIN j.partie p"
            + " WHERE p.id = ?1"
            + " AND j.etat = atos.magiemagie.entity.Joueur.EtatJoueur.A_LA_MAIN")
    public Long findDealerId(Long idPartie);
    
    @Query("SELECT COUNT(j) FROM Joueur j JOIN j.partie p"
            + " WHERE j.etat = atos.magiemagie.entity.Joueur.EtatJoueur.EN_ATTENTE"
            + " AND p.id = ?1")
    public Long findNombreJoueurEnAttente(Long idPartie);
    
    @Query("SELECT COUNT(j) FROM Joueur j JOIN j.partie p"
            + " WHERE (j.etat = atos.magiemagie.entity.Joueur.EtatJoueur.PAS_LA_MAIN"
            + " OR j.etat = atos.magiemagie.entity.Joueur.EtatJoueur.A_LA_MAIN"
            + " OR j.etat = atos.magiemagie.entity.Joueur.EtatJoueur.EN_SOMMEIL)"
            + " AND p.id = ?1")
    public Long findNombreJoueurEnLice(Long idPartie);
    
    @Query("SELECT j FROM Joueur j JOIN j.partie p"
            + " WHERE (j.etat = atos.magiemagie.entity.Joueur.EtatJoueur.PAS_LA_MAIN"
            + " OR j.etat = atos.magiemagie.entity.Joueur.EtatJoueur.A_LA_MAIN"
            + " OR j.etat = atos.magiemagie.entity.Joueur.EtatJoueur.EN_SOMMEIL)"
            + " AND p.id = ?1")
    public List<Joueur> findJoueursEnLice(Long idPartie);
    
    @Query( "SELECT j"
            + "     FROM Joueur j"
            + "     JOIN j.partie p"
            + "     WHERE p.id = ?1"
            + "     AND j.etat = atos.magiemagie.entity.Joueur.EtatJoueur.EN_ATTENTE"
            + "     ORDER BY j.position ASC")
    public List<Joueur> findJoueursOrderByPosition(Long idPartie);
    
    @Query("SELECT j FROM Joueur j"
            + " JOIN j.partie p"
            + " WHERE p.id = ?1"
            + " AND j.id != ?2"
            + " AND (j.etat = atos.magiemagie.entity.Joueur.EtatJoueur.A_LA_MAIN"
            + " OR j.etat = atos.magiemagie.entity.Joueur.EtatJoueur.PAS_LA_MAIN"
            + " OR j.etat = atos.magiemagie.entity.Joueur.EtatJoueur.EN_SOMMEIL"
            + ")")
    public List<Joueur> findAllFromPartieExceptOne(Long idJoueur);

    @Query("SELECT MAX(j.position) FROM Joueur j"
            + " JOIN j.partie p"
            + " WHERE p.id = ?1")
    public Long findLastPositionWithMax(Long idPartie);
    
    @Query("     SELECT j"
                + "     FROM Joueur j"
            + "     JOIN j.partie p"
                + "     WHERE pe.id = ?1"
                + "     AND j.position > ?2"
                + "     AND (j.etat = atos.magiemagie.entity.Joueur.EtatJoueur.PAS_LA_MAIN"
                + "     OR j.etat = atos.magiemagie.entity.Joueur.EtatJoueur.EN_SOMMEIL)"
                + "     ORDER BY j.position ASC")
    public Joueur findNextDealer(Long idDealer, Long positionDealer);
}
