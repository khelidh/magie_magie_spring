<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div id="mainJoueur">
    <c:forEach items="${joueurPrincipal.cartes}" var="carte">
        ${carte.getType()}
        <c:choose>
            <c:when test="${carte.getType() eq 'AILE_DE_CHAUVE_SOURIS'}">
                <div class="carte">
                    <img class="cartes" src="image/carte_aile_de_chauve_souris.png" alt="aile chauve souris"/>
                    AILE_DE_CHAUVE_SOURIS
                </div>
            </c:when>

            <c:when test="${carte.getType() eq BAVE_DE_CRAPAUD}">
                <div class="carte">
                    div carte
                    <img class="cartes" src="image/carte_corne_de_licorne.png" alt="bave"/>
                    BAVE_DE_CRAPAUD
                </div>
            </c:when>
            
            <c:when test="${carte.getType() eq CORNE_DE_LICORNE}">
                <div class="carte">
                    <img class="cartes" src="image/carte_corne_de_licorne.png" alt="corne"/>
                    CORNE_DE_LICORNE
                </div>
            </c:when>
            
            <c:when test="${carte.getType() eq LAPIS_LAZULI}">
                <div class="carte">
                    <img class="cartes" src="image/carte_mandragore.png" alt="lapis"/>
                    LAPIS_LAZULI
                </div>
            </c:when>
            
            <c:when test="${carte.getType() eq MANDRAGORE}">
                <div class="carte">
                    <img class="cartes" src="image/carte_mandragore.png" alt="mandragore"/>
                    MANDRAGORE
                </div>
            </c:when>

        </c:choose>

    </c:forEach>

</div>

