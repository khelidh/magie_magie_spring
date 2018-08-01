$(document).ready(function (e) {
    $('#mainJoueur').load('BLOC_MAIN_JOUEUR.jsp');
    $('#listeJoueurs').load('BLOC_JOUEURS.jsp');
    $('#informationPartie').load('BLOC_INFORMATION.jsp');
    

//    var refresh = setInterval(function () {
//        $.get("/refresh-servlet", function (data) {
//            $('PLATEAU_PARTIE.jsp #mainJoueur').html('BLOC_MAIN_JOUEUR.jsp');
//        });
//    }, 2000);
});