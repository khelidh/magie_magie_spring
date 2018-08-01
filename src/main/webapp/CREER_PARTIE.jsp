<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta http-equiv="Cache-Control" content="no-cache">
        <title>Magie Magie - Créer sa partie</title>

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
            <div id="main">
                <h2 class="titrePage"> Créer sa partie</h2>

                <form class="formulaire" method="POST">
                    <label class="label">Nom de votre partie</label>
                    <br>
                    <input type="text" name="nomPartie" id="nomPartie">
                    <br>
                    <label class="label">Choississez un pseudonyme</label>
                    <br>
                    <input type="text" name="pseudoJoueur" id="nomPartie">
                    <br>
                    <label class="label">Choississez un avatar</label>
                    <br>
                    <input type="text" name="avatarJoueur" id="nomPartie">
                    <br>
                    <input class="boutonFormulaire" type="submit" value="Créer une partie">
                </form>

                <div class="lien">
                    <a class="liens" href="<c:url value='/lister-partie-servlet'/>">Retour</a>
                </div>



            </div>

            <c:import url="_FOOTER.jsp"/>
            <c:import url="_JAVASCRIPT.jsp"/>



    </body>
</html>
