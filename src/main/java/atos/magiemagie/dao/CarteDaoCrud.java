/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package atos.magiemagie.dao;

import atos.magiemagie.entity.Carte;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author Administrateur
 */
public interface CarteDaoCrud extends CrudRepository<Carte, Long>{
    
    
    public Carte findOneByTypeAndJoueurId(Long id, Carte.TypeCarte type);
    public Long countByTypeAndId(Long id, Carte.TypeCarte type);
    
}
