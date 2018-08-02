package atos.magiemagie.service;

import atos.magiemagie.dao.CarteDaoCrud;
import atos.magiemagie.dao.JoueurDaoCrud;
import atos.magiemagie.dao.PartieDAO;
import atos.magiemagie.dao.PartieDaoCrud;
import atos.magiemagie.entity.Carte;
import atos.magiemagie.entity.Carte.TypeCarte;
import atos.magiemagie.entity.Joueur;
import atos.magiemagie.entity.Joueur.EtatJoueur;
import atos.magiemagie.entity.Partie;
import atos.magiemagie.entity.Partie.EtatPartie;
import java.util.List;
import java.util.Scanner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author mama
 */
@Transactional
@Service
public class PartieService {
    //////////////////////
    //  CONSTANTES
    /////////////////////
    public static final String SORT_INVISIBILITE = "INVISIBILITE";
    public static final String SORT_HYPSNOSE = "HYPSNOSE";
    public static final String SORT_DIVINATION = "DIVINATION";
    public static final String SORT_SOMMEIL_PROFOND = "SOMMEIL PROFOND";
    public static final String SORT_FILTRE_AMOUR = "FILTRE D'AMOUR";
    public static final String SORT_FAILED = "SORT FAILED";

    public static final int POSITION_DE_DEPART = 0;

    public static final int NOMBRE_MINIMAL_JOUEURS_PAR_PARTIE = 2;
    private static final int NOMBRE_CARTES_VOLEES_HYPNOSE = 3;

    //////////////////////
    //  DAO & Services utilisés 
    /////////////////////
    @Autowired
    private PartieDaoCrud partieDaoCrud;
    
    @Autowired
    private JoueurDaoCrud joueurDaoCrud;
    
    @Autowired
    private CarteDaoCrud carteDaoCrud;
    
    @Autowired
    CarteService serviceCarte;
    
    @Autowired
    JoueurService serviceJoueur;
    
    @Autowired
    CarteService carteService;

    ///////////////////////
    //  Gérance de la création & de l'état de la partie
    /////////////////////
    public Partie creer(String partieNom, String pseudo, String avatar) {
        Partie partie = new Partie(partieNom);
        
        Joueur joueur = joueurDaoCrud.findOneByPseudo(pseudo);

        joueur.setAvatar(avatar);
        joueur.setEtat(EtatJoueur.EN_ATTENTE);
        joueur.setPosition(0);
        
        partieDaoCrud.save(partie);

        partie.getJoueurs().add(joueur);
        joueur.setPartie(partie);

        if (joueur.getId() == null) {
            serviceJoueur.insertJoueur(joueur);
        } else {
            serviceJoueur.updateJoueur(joueur);
        }
        
        return partie;
    }

    public void demarrer(Long idPartie) {
        Partie partie = getPartie(idPartie);
        if ((joueurDaoCrud.findNombreJoueurEnAttente(idPartie) > 1) && (partie.getEtat() == EtatPartie.EN_PREPARATION)) {
            partie.setEtat(EtatPartie.EN_COURS);
            updatePartie(partie);

            List<Joueur> joueurs = joueurDaoCrud.findJoueursOrderByPosition(idPartie);
            joueurs.get(POSITION_DE_DEPART).setEtat(EtatJoueur.A_LA_MAIN);
            serviceJoueur.updateJoueur(joueurs.get(POSITION_DE_DEPART));

            for (int i = 1; i < joueurs.size(); i++) {
                joueurs.get(i).setEtat(EtatJoueur.PAS_LA_MAIN);
                serviceJoueur.updateJoueur(joueurs.get(i));
            }
        }
    }

    public Long rejoindrePartie(String pseudo, String avatar, Long idPartie) {
        // si le pseudo n'existe pas, renvoie un objet Joueur avec le pseudo donné
        Joueur joueur = joueurDaoCrud.findOneByPseudo(pseudo);

        // On choisit un avatar lorque l'on rejoint la partie
        joueur.setAvatar(avatar);

        // Initialise les attributs du nouveau joueur
        joueur.setEtat(EtatJoueur.EN_ATTENTE);
        Long ordre = joueurDaoCrud.findLastPositionWithMax(idPartie);
        joueur.setPosition(ordre + 1);

        Partie partie = partieDaoCrud.findOne(idPartie);

        joueur.setPartie(partie);
        
        // Pas besoin de faire ça -> même résultat car on ne l'utilise plus après : [ la liste
        // n'est pas update]
        //partie.getJoueurs().add(joueur);

        if (joueur.getId() == null) {
            joueurDaoCrud.save(joueur);
        } else {
            joueurDaoCrud.save(joueur);
        }
        return joueur.getId();
    }

    public void terminerPartie(Long idPartie) {
        Partie partie = getPartie(idPartie);
        System.out.println("La partie " + partie.getNom() + " est terminée !");
        //MAJ partie gagnées etc....
    }

    ///////////////////////
    //  Gérance des joueurs pendant la partie
    ///////////////////// 
    /**
     * A partir du dealer actuel, cherche le prochaine joueur toujours en jeu.
     * S'il est en sommeil, on modifie son état et on cherche le prochain joueur
     * jusqu'à trouver un joueur avec un etatJoueur = PAS_LA_MAIN
     *
     * @param Long
     * @return Joueur
     */
    public Joueur getNextDealer(Long idJoueur) {
        Joueur dealer = getJoueur(idJoueur);
        dealer.setEtat(EtatJoueur.PAS_LA_MAIN);
        serviceJoueur.updateJoueur(dealer);

        Joueur nextDealer = joueurDaoCrud.findNextDealer(dealer.getId(), dealer.getPosition());
        do {

            if (nextDealer.getEtat() == EtatJoueur.EN_SOMMEIL) {
                nextDealer.setEtat(EtatJoueur.PAS_LA_MAIN);
                serviceJoueur.updateJoueur(nextDealer);
            } else if (nextDealer.getEtat() == EtatJoueur.PAS_LA_MAIN) {
                nextDealer.setEtat(EtatJoueur.A_LA_MAIN);
                serviceJoueur.updateJoueur(nextDealer);
                return nextDealer;
            }
            nextDealer = joueurDaoCrud.findNextDealer(dealer.getId(), dealer.getPosition());
        } while (true);
    }

    public void eliminerJoueur(Long idJoueur) {
        Joueur joueur = getJoueur(idJoueur);
        joueur.setEtat(EtatJoueur.ELIMINE);

        System.out.println("Le joueur " + joueur.getPseudo() + " est éliminé ! Dommage ! ");
        System.out.println("Il reste " + joueurDaoCrud.findNombreJoueurEnLice(joueur.getPartie().getId()) + " dans la partie !");

        if (joueur.getCartes().size() > 0) {
            for (Carte carte : joueur.getCartes()) {
                carte.setJoueur(null);
                serviceCarte.deleteCarte(carte);
            }
        }
        
        serviceJoueur.updateJoueur(joueur);
    }

    ///////////////////////
    //  Gérance des cartes
    /////////////////////
    public void distribuer(Long idPartie) {
        for (Joueur joueur : getJoueurs(idPartie)) {
            for (int i = 0; i < Partie.PARTIE_NOMBRE_CARTE_DEBUT; i++) {
                Carte carte = carteService.tirer(joueur);
                carteDaoCrud.save(carte);
                // Pas besoin d'update liste et bdd joueur car c'est déjà fait avec la carte 
                //joueur.getCartes().add(carte);
                //serviceJoueur.updateJoueur(joueur);
            }
        }
    }

    public void volerCarteAleatoireFromJoueur(Long idJoueur, Long idCible) {
        Joueur cible = getJoueur(idCible);
        Joueur joueur = getJoueur(idJoueur);

        List<Carte> cartesCible = cible.getCartes();
        int nombreCartes = cartesCible.size();
        int indiceCarteVolee = (int) (Math.random() * nombreCartes);
        
//        System.out.println("- 0 -");
//        System.out.println("Joueur size cartes : " + joueur.getCartes().size());
//        System.out.println("Cible size cartes : " + cible.getCartes().size());
//        System.out.println("Joueur daoGet cartes : " + getJoueur(idJoueur).getCartes().size());
//        System.out.println("Cible daoGet cartes : " + getJoueur(idCible).getCartes().size());

        Carte carteVolee = cartesCible.get(indiceCarteVolee);
        carteVolee.setJoueur(joueur);
        serviceCarte.updateCarte(carteVolee);
        
    }

    ///////////////////////
    //  Gérance du tour
    /////////////////////
    public void commencer(Long idPartie) {
        Partie partie = getPartie(idPartie);
        if (partie.getEtat() == EtatPartie.EN_COURS) {
            Long idPremierJoueur = joueurDaoCrud.findDealerId(idPartie);
            jouer(idPremierJoueur);
        } else {
            System.out.println("La partie " + partie.getNom() + " a déjà commencée !");
        }
    }

    //OK - passer tour = piocher carte
    public void piocherCarte(Long idJoueur) {
        Joueur joueur = getJoueur(idJoueur);
        Carte carte = serviceCarte.tirer(joueur);
        
        // Insert dans la base de la carte
        carteDaoCrud.save(carte);
    }
    
    public void passerTour(Long idJoueur){
        piocherCarte(idJoueur);
        getNextDealer(idJoueur);
    }

    public void jouer(Long idJoueur) {
        try {
            Long idPartie = partieDaoCrud.findPartieIDFromJoueurID(idJoueur);
            affichage(idPartie);
            if (isPartieTerminee(idPartie)) {
                terminerPartie(idPartie);
            } else {
                jouerTour(idJoueur);
            }
        } catch (NullPointerException e) {
            System.out.println("La partie n'a pas commencé ou est terminée ! Ou peut-être un bug qui n'est pas de notre faute...");
        }
    }

    // jouerTour avec sout pour version console
    public void jouerTour(Long idJoueur) {
        System.out.println("");
        System.out.println("A vous de jouer " + getJoueur(idJoueur).getPseudo());
        System.out.println("");
        int action = selectionAction();

        if (action == 1) {
            System.out.println("Vous êtes chaude comme la lave du Mont Blanc ! Morse & Odeur à vous !");
            jouerSort(idJoueur);
            isJoueurStillAlive(idJoueur);
            jouer(getNextDealer(idJoueur).getId());
        } else if (action == 2) {
            System.out.println("Vous avez choisi de passer votre tour et donc de piocher une carte !");
            piocherCarte(idJoueur);
            // récupère prochain joueur en état de jouer (en faisant les mofifications 
            //nécessaires pour les joueurs en sommeil) -> puis le fait jouer
            jouer(getNextDealer(idJoueur).getId());
        } else if (action == 3) {
            System.out.println("Ce n'était pas très clair, désolé... Voici une meilleure explication !");
            jouer(idJoueur);
        } else {
            System.out.println("Vous avez rippé(e), accrochez-vous et donner une réponse valide ! Vous pouvez le faire !");
            jouerTour(idJoueur);
        }
    }

    // JouerSort avec sout
    public void jouerSort(Long idJoueur) {
        Joueur joueur = getJoueur(idJoueur);

        System.out.println("Préparez vous à mourir ! Lancez un sort pour votre survie !");
        //System.out.println(serviceJoueur.mainToString(joueur));

        int indiceCarte1 = selectionCarte(idJoueur);
        int indiceCarte2 = selectionCarte(idJoueur);

        if (indiceCarte1 == indiceCarte2) {
            System.out.println("Vous avez sélectionné la même carte. Choississez en une autre !");
            indiceCarte2 = selectionCarte(idJoueur);
        }

        Carte carte1 = joueur.getCartes().get(indiceCarte1);
        Carte carte2 = joueur.getCartes().get(indiceCarte2);

        TypeCarte type1 = carte1.getType();
        TypeCarte type2 = carte2.getType();

        System.out.println("Vous avez sélectionné les cartes suivantes : ");
        System.out.println("Carte " + type1);
        System.out.println("Carte " + type2);

        if (selectionVerification()) {
            String sort = determinerSort(type1, type2);
            serviceJoueur.supprimerCartes(joueur, carte1, carte2);
            lancerSort(idJoueur, sort);
        } else {
            jouerSort(idJoueur);
        }
    }
    
    //jouerSort modifié
    public void jouerSortPANEL(Long idJoueur, TypeCarte type1, TypeCarte type2) {
        String sort = determinerSort(type1, type2);
        supprimerDeuxCartes(idJoueur, type1, type2);
        //lancerSortPANEL(idJoueur, idCible, idJoueur, sort);
    }

    public String determinerSort(TypeCarte type1, TypeCarte type2) {
        String sort = SORT_FAILED;

        switch (type1) {
            case AILE_DE_CHAUVE_SOURIS:
                if (type2.equals(TypeCarte.MANDRAGORE)) {
                    sort = SORT_SOMMEIL_PROFOND;
                } else if (type2.equals(TypeCarte.LAPIS_LAZULI)) {
                    sort = SORT_DIVINATION;
                }   break;
            case MANDRAGORE:
                if (type2.equals(TypeCarte.AILE_DE_CHAUVE_SOURIS)) {
                    sort = SORT_SOMMEIL_PROFOND;
                } else if (type2.equals(TypeCarte.CORNE_DE_LICORNE)) {
                    sort = SORT_FILTRE_AMOUR;
                }   break;
            case CORNE_DE_LICORNE:
                if (type2.equals(TypeCarte.MANDRAGORE)) {
                    sort = SORT_FILTRE_AMOUR;
                } else if (type2.equals(TypeCarte.BAVE_DE_CRAPAUD)) {
                    sort = SORT_INVISIBILITE;
                }   break;
            case BAVE_DE_CRAPAUD:
                if (type2.equals(TypeCarte.CORNE_DE_LICORNE)) {
                    sort = SORT_INVISIBILITE;
                } else if (type2.equals(TypeCarte.LAPIS_LAZULI)) {
                    sort = SORT_HYPSNOSE;
                }   break;
            case LAPIS_LAZULI:
                if (type2.equals(TypeCarte.BAVE_DE_CRAPAUD)) {
                    sort = SORT_HYPSNOSE;
                } else if (type2.equals(TypeCarte.AILE_DE_CHAUVE_SOURIS)) {
                    sort = SORT_DIVINATION;
                }   break;
            default:
                break;
        }
        return sort;
    }

    public void lancerSort(Long idJoueur, String sort) {
        switch (sort) {
            case (SORT_FAILED): {
                System.out.println("Malheureusement le combo n'a rien donné ! Bien essayé tout de même ;)");
                break;
            }
            case (SORT_INVISIBILITE): {
                lancerSortInvisibilite(idJoueur);
                break;
            }
            case (SORT_HYPSNOSE): {
                System.out.println("Vous avez lancé un sort d'HYPNOSE !");
                Long idCible = selectionCible(idJoueur);
                lancerSortHypnose(idJoueur, idCible);
                break;
            }
            case (SORT_DIVINATION): {
                System.out.println("Un sort de DIVINATION SUPREME a été déclenché ! Quel est l'utilisateur de cette magie noire ?!");
                System.out.println("Les cartes des victimes sont dévoilées ! Quel manque de fair-play. C'pas sport ça !");
                lancerSortDivination(idJoueur);
                break;
            }
            case (SORT_SOMMEIL_PROFOND): {
                System.out.println("Vous avez lancé un sort de SOMMEIL PROFOND !");
                Long idCible = selectionCible(idJoueur);

                System.out.println("Un sortitlège de SOMMEIL PROFOND a été lancé"
                        + " par la fabuleuse sorcière " + getJoueur(idJoueur).getPseudo()
                        + " sur le faiblard cerveau de l'hérétique " + getJoueur(idCible).getPseudo());

                lancerSortSommeil(idCible);
                break;
            }
            case (SORT_FILTRE_AMOUR): {
                System.out.println("Vous vous apprétez à amorcer un puissant filtre d'amour !");
                Long idCible = selectionCible(idJoueur);
                lancerSortFiltreAmour(idJoueur, idCible);

                break;
            }
        }
    }
    public void lancerSortPANEL(Long idJoueur, Long idCible, Long idCarteJetee, String sort) {
        switch (sort) {
            case (SORT_FAILED): {
                break;
            }
            case (SORT_INVISIBILITE): {
                lancerSortInvisibilite(idJoueur);
                break;
            }
            case (SORT_HYPSNOSE): {
                lancerSortHypnosePANEL(idJoueur, idCible, idCarteJetee);
                break;
            }
            case (SORT_DIVINATION): {
                lancerSortDivination(idJoueur);
                break;
            }
            case (SORT_SOMMEIL_PROFOND): {
                lancerSortSommeil(idCible);
                break;
            }
            case (SORT_FILTRE_AMOUR): {
                lancerSortFiltreAmour(idJoueur, idCible);
                break;
            }
        }
    }

    public void lancerSortInvisibilite(Long idJoueur) {
        Joueur joueur = getJoueur(idJoueur);
        List<Joueur> listeJoueurs = joueurDaoCrud.findAllByPartieIdAndIdNot(joueur.getId(), idJoueur);

        for(Joueur cible : listeJoueurs) {
            volerCarteAleatoireFromJoueur(idJoueur, cible.getId());
        }
    }

    public void lancerSortDivination(Long idJoueur) {
        Joueur joueur = getJoueur(idJoueur);
        List<Joueur> listeJoueurs = joueurDaoCrud.findAllByPartieIdAndIdNot(joueur.getId(), idJoueur);
        for (Joueur cible : listeJoueurs) {
            System.out.println(serviceJoueur.mainToString(cible));
        }
    }

    public void lancerSortSommeil(Long idCible) {
        Joueur cible = getJoueur(idCible);
        cible.setEtat(EtatJoueur.EN_SOMMEIL);
        serviceJoueur.updateJoueur(cible);
    }

    public void lancerSortHypnose(Long idJoueur, Long idCible) {
        boolean cibleEliminee = false;

        Joueur joueur = getJoueur(idJoueur);
        Joueur cible = getJoueur(idCible);

        long indiceCarteJoueur = (long) selectionCarte(idJoueur);
        Carte carteJoueur = joueur.getCartes().get((int) indiceCarteJoueur);
        joueur.getCartes().remove(carteJoueur);

        for (int i = 0; i < NOMBRE_CARTES_VOLEES_HYPNOSE; i++) {
            int nombreCarteCible = cible.getCartes().size();

            if (nombreCarteCible > 0) {
                int indiceCarte = (int) Math.random() * nombreCarteCible;

                Carte carteVolee = cible.getCartes().remove(indiceCarte);
                carteVolee.setJoueur(joueur);
                joueur.getCartes().add(carteVolee);
                System.out.println("La carte subtilisée n°" + (i + 1) + " :" + carteVolee.getType());
            } else {
                cibleEliminee = true;
                break;
            }
        }

        if (cibleEliminee) {
            //modifier la desctruction de la carte
            carteJoueur.setJoueur(null);
            serviceCarte.deleteCarte(carteJoueur);
            eliminerJoueur(idCible);
            System.out.println("La carte de " + carteJoueur.getType() + " de " + joueur.getPseudo()
                    + " a donc été détruite ! Quel gaspillage !");
        } else {
            System.out.println("La carte de " + carteJoueur.getType() + " de " + joueur.getPseudo()
                    + " est donc cédée à la pauvre sorcière " + cible.getPseudo() + " !");
            carteJoueur.setJoueur(cible);
            cible.getCartes().add(carteJoueur);
            serviceCarte.updateCarte(carteJoueur);
        }

        serviceJoueur.updateJoueur(cible);
        serviceJoueur.updateJoueur(joueur);
    }

    // SANS SOUT et selectionCible // Interface
    public void lancerSortHypnosePANEL(Long idJoueur, Long idCible, Long idCarte) {
        boolean cibleEliminee = false;

        Joueur joueur = getJoueur(idJoueur);
        Joueur cible = getJoueur(idCible);

        Carte carteJoueur = carteDaoCrud.findOneByTypeAndJoueurId(idJoueur, TypeCarte.AILE_DE_CHAUVE_SOURIS);
        
        int nombreCarteCible = cible.getCartes().size();
        
        for (int i = 0; i < NOMBRE_CARTES_VOLEES_HYPNOSE; i++) {
            

            if (nombreCarteCible > 0) {
                int indiceCarte = (int) (Math.random() * nombreCarteCible);

                Carte carteVolee = cible.getCartes().remove(indiceCarte);
                carteVolee.setJoueur(joueur);
                joueur.getCartes().add(carteVolee);
                nombreCarteCible--;
                serviceCarte.updateCarte(carteVolee);
            } else {
                cibleEliminee = true;
                break;
            }
        }

        if (cibleEliminee) {
            carteJoueur.setJoueur(null);
            serviceCarte.deleteCarte(carteJoueur);
            eliminerJoueur(idCible);
        } else {
            carteJoueur.setJoueur(cible);
            cible.getCartes().add(carteJoueur);
            serviceCarte.updateCarte(carteJoueur);
        }
    }
    
    public void lancerSortFiltreAmour(Long idJoueur, Long idCible) {

        Joueur joueur = getJoueur(idJoueur);
        Joueur cible = getJoueur(idCible);

        int moitieCarteCible = (int) (cible.getCartes().size() / 2);

        if (moitieCarteCible == 0) {
            System.out.println("Un filtre d'amour surgit et conquit la belle " + cible.getPseudo() + " !"
                    + " Elle n'a qu'une pauvre carte à offrir"
                    + " à " + joueur.getPseudo() + ". Quelle tristesse ! Elle est morte d'amour et de honte "
                    + " en entrainant son faible bien ! Dommage !");
            eliminerJoueur(cible.getId());
        } else {
            System.out.println("Un filtre d'amour surgit et conquit la belle " + cible.getPseudo() + " !"
                    + " Elle donne " + moitieCarteCible + " de ses cartes afin de dévoiler son coeur "
                    + " à " + joueur.getPseudo() + ". Fabuleux !");
            for (int i = 0; i < moitieCarteCible; i++) {
                int indice = (int) Math.random() * cible.getCartes().size();

                Carte carteVolee = cible.getCartes().get(indice);
                System.out.println("La carte " + carteVolee.getType() + " est donnée à " + joueur.getPseudo() + " par " + cible.getPseudo());
                cible.getCartes().remove(carteVolee);
                carteVolee.setJoueur(joueur);
                joueur.getCartes().add(carteVolee);
                serviceCarte.updateCarte(carteVolee);
            }
            serviceJoueur.updateJoueur(joueur);
            serviceJoueur.updateJoueur(cible);
        }
    }
    
     public void lancerSortFiltreAmourPANEL(Long idJoueur, Long idCible) {

        Joueur joueur = getJoueur(idJoueur);
        Joueur cible = getJoueur(idCible);

        int moitieCarteCible = (int) (cible.getCartes().size() / 2);

        if (moitieCarteCible == 0) {
            eliminerJoueur(cible.getId());
        } else {
            for (int i = 0; i < moitieCarteCible; i++) {
                int indice = (int) (Math.random() * cible.getCartes().size());

                Carte carteVolee = cible.getCartes().remove(indice);
                carteVolee.setJoueur(joueur);
                serviceCarte.updateCarte(carteVolee);
            }
//            serviceJoueur.updateJoueur(joueur);
//            serviceJoueur.updateJoueur(cible);
        }
    }

    ///////////////////////
    //  CHECKs
    /////////////////////
    public boolean isPartieTerminee(Long idPartie) {
        return joueurDaoCrud.findNombreJoueurEnLice(idPartie) == 1;
    }

    // Peut-etre faire une requete directe au lieu de prendre l'objet et faire une opération dessus
    public void isJoueurStillAlive(Long idJoueur) {
        Joueur joueur = getJoueur(idJoueur);
        if (joueur.getCartes().isEmpty()) {
            eliminerJoueur(idJoueur);
        }
    }
    ///////////////////////
    //  SELECTIONS
    /////////////////////
    public Long selectionCible(Long idJoueur) {
        System.out.println("Vous devez choisir votre cible entre :");
        Joueur player = getJoueur(idJoueur);
        int numero = 0;
        List<Joueur> joueurs = joueurDaoCrud.findAllByPartieIdAndIdNot(player.getId(), idJoueur);

        for (Joueur joueur : joueurs) {
            numero++;

            System.out.println(numero + "- Joueur " + joueur.getPseudo()
                    + " avec " + joueur.getCartes().size() + " cartes !");
        }

        Scanner scanner = new Scanner(System.in);
        String txt = scanner.nextLine();

        int indice;
        try {
            indice = Integer.parseInt(txt);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return selectionCible(idJoueur);
        }

        if (indice >= 1 && indice <= numero) {
            return joueurs.get(indice - 1).getId();
        }
        return selectionCible(idJoueur);
    }

    public int selectionAction() {
        System.out.println("C'est à votre tour de jouer ! Que voulez-vous faire");
        System.out.println("SELECTION ACTION : 1- Jouer Combo");
        System.out.println("SELECTION ACTION : 2- Piocher & passer");
        System.out.println("SELECTION ACTION : 3- C'est pas faux !");

        Scanner scanner = new Scanner(System.in);
        String txt = scanner.nextLine();
        int indice = 0;
        try {
            indice = Integer.parseInt(txt);
        } catch (java.lang.NumberFormatException e) {
            System.out.println("Soyez attentif !");
            return selectionAction();
        }
        return indice;
    }

    public int selectionCarte(Long idJoueur) {

        Joueur joueur = getJoueur(idJoueur);
        int nombreCarte = joueur.getCartes().size();

        System.out.println("- Choississez une carte !");
        System.out.println(serviceJoueur.mainToString(joueur));
        System.out.println("Entre 1 et " + nombreCarte);

        Scanner scanner = new Scanner(System.in);
        String txt = scanner.nextLine();
        int indice = 0;
        try {
            indice = Integer.parseInt(txt);
            indice--;
        } catch (java.lang.NumberFormatException e) {
            return selectionCarte(idJoueur);
        }

        if (indice >= 0 && indice < nombreCarte) {
            return indice;
        }
        System.out.println("Indice de carte invalide. Veuillez essayer à nouveau !");
        return selectionCarte(idJoueur);
    }

    public boolean selectionVerification() {
        System.out.println("Vous êtes sûr de votre choix ? - yes/y - no");
        Scanner scanner = new Scanner(System.in);
        String txt = scanner.nextLine();

        if (txt.equals("yes") || (txt.equals("y"))) {
            return true;
        }
        return false;
    }

    ///////////////////////
    //  Affichage
    /////////////////////
    public void affichage(Long idPartie) {
        Partie partie = getPartie(idPartie);
        String chaine = "Partie " + partie.getNom() + "\n";
        List<Joueur> joueurs;
        if (partie.getEtat() == EtatPartie.EN_PREPARATION) {
            joueurs = getAllJoueurs(idPartie);
        } else {
            joueurs = getJoueurs(idPartie);
        }

        for (Joueur joueur : joueurs) {
            chaine += serviceJoueur.mainToString(joueur) + "\n";
        }
        System.out.println(chaine);
    }

    ///////////////////////
    //  GET
    /////////////////////
    public Partie getPartie(Long idPartie) {
        return partieDaoCrud.findOne(idPartie);
    }

    private List<Joueur> getAllJoueurs(Long idPartie) {
        return getPartie(idPartie).getJoueurs();
    }

    public List<Joueur> getJoueurs(Long idPartie) {
        return joueurDaoCrud.findJoueursEnLice(idPartie);
    }
    
    public Joueur getJoueurALaMain(Long idPartie){
        return joueurDaoCrud.findOneByPartieIdAndEtat(idPartie, EtatJoueur.A_LA_MAIN);
    }

    public Joueur getJoueur(Long idJoueur) {
        return joueurDaoCrud.findOne(idJoueur);
    }
    
    public List<Partie> getPartiesEnPrepapration(){
        return partieDaoCrud.listerPartieEnPreparation();
    }
    
    public Joueur getJoueurByPosition(Long idPartie, Long position){
        return joueurDaoCrud.findOneByPartieIdAndPosition(idPartie, position);
    }
    public Joueur getJoueurFirstPosition(Long idPartie){
        return joueurDaoCrud.findOneByPartieIdAndEtat(idPartie, EtatJoueur.A_LA_MAIN);
    }

    ///////////////////////
    //  UPDATE
    /////////////////////
    public void updatePartie(Partie partie) {
        partieDaoCrud.save(partie);
    }

    /////////////////////////
    // FONCTION MAIN SERVICE
    public int selectionActionDebutPartie() {

        System.out.println("Bienvenue sur Magie Magie !");
        System.out.println("Nous somme heureux de vous avoir avec nous !"
                + "\nQue voulez-vous faire ?");
        System.out.println("1 - Créer une partie"
                + "\n2 - Rejoindre une partie"
                + "\n3 - Aller dans son espace personnel"
                + "\n4 - Quitter le jeu");

        Scanner scanner = new Scanner(System.in);
        int indice;
        String txt;

        try {
            txt = scanner.nextLine();
            indice = Integer.parseInt(txt);

        } catch (IllegalArgumentException e) {
            return selectionActionDebutPartie();
        }
        if (indice > 0 && indice <= 4) {
            return indice;
        }
        return selectionActionDebutPartie();
    }

    public void application() {
        boolean boucle = true;
        while (boucle) {

            int indice = selectionActionDebutPartie();

            switch (indice) {
                case 1:
                    String pseudo = serviceJoueur.selectionPseudo();
                    String avatar = serviceJoueur.selectionAvatar();

                    Partie partie = creer("Partie spéciale", pseudo, avatar);
                    System.out.println(pseudo + " vient de créer la partie " + partie.getNom() + " !");
                    updatePartie(partie);
                    break;

                case 2:
                    Scanner scanner = new Scanner(System.in);
                    String txt;
                    int i = 1;

                    List<Partie> parties = getPartiesEnPrepapration();

                    if (parties.size() != 0) {
                        for (Partie p : parties) {
                            System.out.println(i + " - La partie " + p.getNom() + " est en préparation. Il y a " + p.getJoueurs().size()
                                    + " joueur(s) pour le moment !");
                            i++;
                        }
                        System.out.println("Quelle partie souhaitez-vous rejoindre ?");
                        txt = scanner.nextLine();

                        try {
                            indice = Integer.parseInt(txt);
                        } catch (IllegalArgumentException e) {
                            indice = 1;
                        }

                        Partie partieChoisie = parties.get(indice - 1);
                        Long idPartie = partieChoisie.getId();
                        rejoindrePartie(serviceJoueur.selectionPseudo(), serviceJoueur.selectionAvatar(), idPartie);
                        //updatePartie(partieChoisie);
                        System.out.println("Vous avez rejoint la partie " + partieChoisie.getNom());
                        System.out.println("Voulez-vous faire débuter la partie à votre entrée ? y/n");

                        try {
                            txt = scanner.nextLine();
                        } catch (IllegalArgumentException e) {
                            txt = "non";
                        }

                        if (txt.equals("y") || txt.equals("yes")) {
                            demarrer(idPartie);
                            distribuer(idPartie);
                            commencer(idPartie);
                        } else {
                            System.out.println("Vous êtes en attente dans une partie. Elle commencera lorque un des joueurs lancera celle-ci !"
                                    + "\nATTENTE !");
                            break;
                        }
                    } else {
                        System.out.println("Il n'y a pas de partie disponible ! Créez-en une si vous voulez jouer ! ");
                    }
                    break;
                case 3:
                    System.out.println("ESPACE PERSONNEL ! SUPER !");
                    break;
                case 4:
                    System.out.println("Vous aller quitter le jeu !");
                    if (selectionVerification()) {
                        boucle = false;
                        System.out.println("Le jeu va se fermer ! Revenez-nous rapidement :)");
                    }

                default:
                    return;
            }

        }

    }

    public void listerPartieEnPreparation() {
        int indice = 0;
        for (Partie partie : getPartiesEnPrepapration()) {
            ++indice;
            System.out.println(indice + " - La partie " + partie.getNom() + " est en préparation. Il y a " + partie.getJoueurs().size()
                    + " joueurs pour le moment !");
        }
    }

    public long getNombreCartesJoueur(Long idJoueur) {
        return carteDaoCrud.countByJoueurId(idJoueur);
    }
    
    public void supprimerDeuxCartes(Long idJoueur, TypeCarte selection1, TypeCarte selection2) {
        Joueur joueur = getJoueur(idJoueur);
        
        Carte carte1 = carteDaoCrud.findOneByTypeAndJoueurId(idJoueur, selection1);
        Carte carte2 = carteDaoCrud.findOneByTypeAndJoueurId(idJoueur, selection2);
        
        System.out.println("supprimerDeuxCartes - CARTE 1 : " + carte1.getType());
        System.out.println("supprimerDeuxCartes - CARTE 2 : " + carte2.getType());

        carte1.setJoueur(null);
        carte2.setJoueur(null);
      
        serviceCarte.deleteCarte(carte1);
        serviceCarte.deleteCarte(carte2);
        
        //serviceJoueur.updateJoueur(joueur);
    }
    
}
