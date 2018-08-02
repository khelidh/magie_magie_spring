package atos.magiemagie.service;

import atos.magiemagie.dao.CarteDaoCrud;
import atos.magiemagie.dao.JoueurDAO;
import atos.magiemagie.dao.JoueurDaoCrud;
import atos.magiemagie.dao.PartieDAO;
import atos.magiemagie.dao.PartieDaoCrud;
import atos.magiemagie.entity.Carte;
import atos.magiemagie.entity.Joueur;
import java.util.Scanner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author mama
 */

@Transactional
@Service
public class JoueurService {  
    
    @Autowired
    private JoueurDaoCrud joueurDaoCrud;
    
    CarteService serviceCarte = new CarteService();
       
    public String mainToString(Joueur joueur){
        int indice = 1;
        String chaine = "Main de " + joueur.getAvatar() + "  :";
        for (Carte carte : joueur.getCartes()){
             chaine += "  " + indice + " - " + carte.getType() + "  |";
             indice++;
        }
        return chaine;
    }
    
    public void afficher(Joueur joueur){
        String chaine = ""
                + "Avatar : " + joueur.getAvatar()
                + "     Pseudo : " + joueur.getPseudo()
                + "\n Main : " + mainToString(joueur);
        
        System.out.println(chaine);
    }
    
    @Transactional
    public void insertJoueur(Joueur joueur){
        joueurDaoCrud.save(joueur);
    }
    
    @Transactional
    public void updateJoueur(Joueur joueur){
        joueurDaoCrud.save(joueur);
    }
    
    public void supprimerCartes(Joueur joueur, Carte carte1, Carte carte2){
        //Joueur joueur = carte1.getJoueur();
        
        joueur.getCartes().remove(carte1);
        joueur.getCartes().remove(carte2);
        
        carte1.setJoueur(null);
        carte2.setJoueur(null);
      
        serviceCarte.deleteCarte(carte1);
        serviceCarte.deleteCarte(carte2);
        updateJoueur(joueur);
    }
    
    public String selectionPseudo(){
        String txt;
        do {
        System.out.println("Veuillez choisir un pseudonyme !");
        Scanner scanner = new Scanner(System.in);
        txt = scanner.nextLine();    
        } while (txt.length() < 3);
            
        
        System.out.println("Votre pseudo est " + txt);
        return txt;
        
    }
    
    public String selectionAvatar(){ 
        int avatar;
        Scanner scanner = new Scanner(System.in);
        do{
        System.out.println("Choississez un avatar :");
        System.out.println("1 - Avatar 1");
        System.out.println("2 - Avatar 2");
        System.out.println("3 - Avatar 3");
            try {
                avatar = scanner.nextInt();
            } catch (RuntimeException e) {
                System.out.println("Vous avez surement fait une faute de frappe ! On vous remet ça ;)");
                avatar = 0;
            }
        
        } while (avatar < 1 && avatar > 3);
        
        switch (avatar){
            case 1 :
                return "Avatar 1";
            case 2 :
                return "Avatar 2";
            case 3:
                return "Avatar 3";
        }
        return "Avatar inconnu";
    }
}
