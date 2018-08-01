/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package atos.magiemagie.dao;

import atos.magiemagie.entity.Partie;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author Administrateur
 */
public interface PartieDaoCrud extends CrudRepository<Partie, Long>{
    
    
    //public List<Partie> findByEtat(Partie.EtatPartie etat);
    
    
    @Query("SELECT p FROM Partie p"
                + " WHERE p.etat = atos.magiemagie.entity.Partie.EtatPartie.EN_PREPARATION")
    public List<Partie> listerPartieEnPreparation();
    
}
