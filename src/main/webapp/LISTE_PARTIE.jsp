<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta http-equiv="Cache-Control" content="no-cache">
        <title>Template of Centurion</title>

        <!-- #Import Font-->
        <link href="https://fonts.googleapis.com/css?family=Eater" rel="stylesheet">
        <link href="https://fonts.googleapis.com/css?family=Gloria+Hallelujah" rel="stylesheet">

        <!-- #Import Image-->
        <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.1.0/css/all.css" integrity="sha384-lKuwvrZot6UHsBSfcMvOkWwlCMgc0TaWr+30HWe3a4ltaBwTZhyTEggF5tJv8tbt"
              crossorigin="anonymous">
        
        <c:import url="_STYLESHEET.jsp"/>
        
    </head>
    <body>
        <c:import url="_HEADER.jsp"/>

        <div id="contenu">     

            <main id="main">
                <!-- Entete avec titre de la page et profil joueur  --> 
                <section id="enteteMain"> 
                    <h1 id="titrePage" class="titre">Liste de Parties</h1>

                    <figure id="profil" >
                        <img id="photo" src="image/photo.png" alt="photo de profil">
                     
                        <figcaption>
                            <h3 id="pseudo">Trunks</h3>
                            <p id="desc">Roi des Sorciers</p>
                        </figcaption>

                    </figure>
                </section>



                <!-- Partie Création Partie -->
                <section class="creerPartie">

                    <h2 class="titre titreDomaine" id="titreCreerPartie">Créer votre partie</h2>
                    <p>Lorem ipsum dolor enati laboriosam assumenda voluptatibus, quia alias nesciunt aliquid consectetur dolores.</p>

                    <a class="lienBouton" id="boutonCreer" href="<c:url value='/creer-partie-servlet'/>">Créer une partie</a>

                </section>



                <!-- Liste des parties en cours de préparation (en attente de joueurs) et bouton pour créer une partie  --> 
                <section id="rejoindrePartie">

                    <h2 class="titre titreDomaine" id="titreRejoindrePartie"><a href="#enteteListe">Rejoindre une partie</a></h2>
                    <p>Lorem ipsum dolor enati laboriosam assumenda voluptatibus, quia alias nesciunt aliquid consectetur dolores.</p>


                    <div>
                        <div id="enteteListe">
                            <p class="cellule">Partie</p>
                            <p class="cellule">Participants</p>
                            <p class="cellule">Créateur</p>
                            <p class="cellule">Rejoindre</p>
                        </div>
                        <c:if test="${parties ne null}">

                            <c:forEach items="${parties}" var="partie">

                                <div class="listePartie partieImpair">
                                    <p class="cellule">${partie.nom}</p>
                                    <p class="cellule">${partie.joueurs.size()}</p>
                                    <p class="cellule">Créateur de la partie</p>
                                    <div class="cellule">
                                        <a class="boutonRejoindre"href="<c:url value='/rejoindre-partie-servlet?idPartie=${partie.id}'/>">Rejoindre cette partie</a>
                                    </div>
                                </div>
                                    
                            </c:forEach>
                        </c:if>

                    </div>

                </section>
                <!--fin liste de parties  --> 


                <!--fin MAIN  --> 
            </main>

        </div>

        <c:import url="_FOOTER.jsp"/>
        <c:import url="_JAVASCRIPT.jsp"/>



    </body>
</html>
