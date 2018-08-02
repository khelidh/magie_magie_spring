package atos.magiemagie.dao;

import atos.magiemagie.entity.Joueur;
import atos.magiemagie.entity.Joueur.EtatJoueur;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.Query;

/**
 * @author mama
 */
public class JoueurDAO {
    
    private EntityManager makeEM(){
        return Persistence.createEntityManagerFactory("PU").createEntityManager();
    }
    
    public void insert(Joueur joueur){
        EntityManager em = makeEM();
        em.getTransaction().begin();
        em.persist(joueur);
        em.getTransaction().commit();
    }
    
    public void delete(Joueur joueur){
        EntityManager em = makeEM();
        em.getTransaction().begin();
        em.remove(em.merge(joueur));
        em.getTransaction().commit();
    }
    
    public Joueur findById(long id){
        return (Joueur) makeEM().find(Joueur.class, id);
    }
    
    public Long findPartieIDFromJoueurID(Long idJoueur){
        String requete = "SELECT j.partie.id"
                + "     FROM Joueur j"
                + "     WHERE j.id = :idJoueur";
        Query query = makeEM().createQuery(requete);
        query.setParameter("idJoueur", idJoueur);
        return (Long) query.getSingleResult();
    }
    
    public List<Joueur> findAllFromPartieExceptOne(Joueur joueur){
        
        EntityManager em = makeEM();
        String requete = "SELECT j"
                + " FROM Joueur j"
                + " JOIN j.partie p"
                + " WHERE p.id = :idPartie"
                + " AND j.id != :idJoueur"
                + " AND j.etat in(:etatJoueur,:etatJoueur2, :etatJoueur3)";

        Query query = em.createQuery(requete);
        query.setParameter("idPartie", joueur.getPartie().getId());
        query.setParameter("idJoueur", joueur.getId());
        query.setParameter("etatJoueur", EtatJoueur.PAS_LA_MAIN);
        query.setParameter("etatJoueur2", EtatJoueur.EN_SOMMEIL);
        query.setParameter("etatJoueur3", EtatJoueur.A_LA_MAIN);
        return query.getResultList();
    }
    
    public void update(Joueur joueur){
        EntityManager em = makeEM();
        em.getTransaction().begin();
        em.merge(joueur);
        em.getTransaction().commit();
    }
    
    /**
     * Permet de trouver un joueur dans la BDD via son pseudo.
     * S'il n'existe pas, on renvoie un nouvel objet Joueur avec le pseudo.
     * @param pseudo
     * @return joueur
     */
    public Joueur findJoueurByPseudo(String pseudo){
        EntityManager em = makeEM();
        String requete = "SELECT j"
                + " FROM Joueur j"
                + " WHERE j.pseudo = :pseudo";
        
        Query query = em.createQuery(requete);
        query.setParameter("pseudo", pseudo);
        
        Joueur joueur;
        
        try {
            joueur = (Joueur) query.getSingleResult();
        } catch (Exception e) {
            joueur = new Joueur(pseudo);
        }
        
        return joueur;
    }
}
