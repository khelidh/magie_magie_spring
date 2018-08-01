/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package atos.magiemagie.dao;

import atos.magiemagie.entity.Carte;
import atos.magiemagie.entity.Carte.TypeCarte;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.Query;

/**
 * @author mama
 */
public class CarteDAO {
    public EntityManager makeEM(){
        return Persistence.createEntityManagerFactory("PU").createEntityManager(); 
    }
    public void insert(Carte carte){
        EntityManager em = makeEM();
        em.getTransaction().begin();
        em.persist(carte);
        em.getTransaction().commit();  
    }
    public void delete(Carte carte){
        EntityManager em = makeEM();
        em.getTransaction().begin();
        em.remove(em.merge(carte));
        em.getTransaction().commit(); 
    }
    public void deleteWithFind(Long id){
        EntityManager em = makeEM();
        em.getTransaction().begin();
        Carte c = em.find(Carte.class, id);
        em.remove(c);
        em.getTransaction().commit();
    }
    public void update(Carte carte){
        EntityManager em = makeEM();
        em.getTransaction().begin();
        em.merge(carte);
        em.getTransaction().commit();  
    }
    
    public Carte findById(Long id){
        return makeEM().find(Carte.class, id);
    }
    public Long findNombreCarte(Long idJoueur, TypeCarte type){
        String requete = "SELECT COUNT(c) FROM Carte c"
                + "         JOIN c.joueur j"
                + "         WHERE j.id = :idJoueur"
                + "         AND c.type = :type";
        
        Query query = makeEM().createQuery(requete);
        query.setParameter("idJoueur", idJoueur);
        query.setParameter("type", type);
        
        Long res;
        try {
            res = (Long) query.getSingleResult();
        } catch (Exception e) {
            res = 0L;
        }
        return res;
    }
    
    public Carte getCarte(Long idJoueur, TypeCarte type){
        String requete = "SELECT c "
                + "     FROM Carte c"
                + "     JOIN c.joueur j"
                + "     WHERE j.id = :idJoueur"
                + "     AND c.type = :typeCarte";
        
        Query query = makeEM().createQuery(requete);
        query.setParameter("idJoueur", idJoueur);
        query.setParameter("typeCarte", type);
        
        return (Carte) query.setMaxResults(1).getSingleResult();
    }
}


