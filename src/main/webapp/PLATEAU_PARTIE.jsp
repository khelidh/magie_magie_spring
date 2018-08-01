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

            <br>
            <br>
            <p>Plateau</p>
            <br>
            <br>
            <header>
                <c:import url="BLOC_INFORMATION.jsp"/>
            </header>


            <main>
                <c:import url="BLOC_JOUEURS.jsp"/>
                <c:import url="BLOC_MAIN_JOUEUR.jsp"/>
                <input class="bouton boutonLancer" type="button" value="Lancer" name="LancerSORT"/>
                <input class="bouton boutonPasser" type="button" value="Passer" name="PasserTOUR"/>

            </main>
        </div>

        <c:import url="_FOOTER.jsp"/>
        <c:import url="_JAVASCRIPT.jsp"/>
    </body>
</html>