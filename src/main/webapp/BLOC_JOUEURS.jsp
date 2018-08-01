<div id="listeJoueurs">
    <c:forEach items="${partie.joueurs}" var="joueur">
        <c:if test="${joueur.id ne idJoueurPrincipal}">
            <div class="mainBot">
                <p>${joueur.pseudo}</p>
                <img class="dosCarte" src="image/dos_de_carte.png" width="300" height="500" alt="dos de carte"/>
                <p>${joueur.cartes.size()}</p>
            </div>

        </c:if>
    </c:forEach>
</div>
