package atos.magiemagie.service;

import atos.magiemagie.dao.CarteDAO;
import atos.magiemagie.dao.CarteDaoCrud;
import atos.magiemagie.entity.Carte;
import atos.magiemagie.entity.Carte.TypeCarte;
import atos.magiemagie.entity.Joueur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
/**
 * @author mama
 */

@Service
public class CarteService {
    CarteDAO carteDAO = new CarteDAO();
    
    @Autowired
    private CarteDaoCrud carteDaoCrud;
   
    public Carte tirer(Joueur joueur){
        int indiceType = (int) (Math.random()*TypeCarte.values().length);    
        return new Carte(TypeCarte.values()[indiceType], joueur);
    }
    
    @Transactional
    public void updateCarte(Carte carte) {
        carteDaoCrud.save(carte);
    }
    
    @Transactional
    public void deleteCarte(Carte carte) {
        carteDaoCrud.delete(carte);
    }
    
    public Carte getCarte(Long idJoueur, TypeCarte type){
        return carteDaoCrud.findOneByTypeAndJoueurId(idJoueur,type);
    }
    
    public Long getNombreCarte(Long idJoueur, TypeCarte type){
        return carteDaoCrud.countByTypeAndId(idJoueur, type);
    }
    
   
}
