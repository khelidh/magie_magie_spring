$(document).ready(function (e) {

    var selection1 = null, selection2 = null;

    $('.carte').click(function (e) {

        if (selection1 === null) {
            selection1 = $(this);
            $(this).addClass('selectionCarte');
        } else if (selection1 === $(this)) {
            selection1 = null;
            $(this).add('selectionCarte');

        } else {
            if (selection2 === null) {
                selection2 = $(this);
                $(this).addClass('selectionCarte');
            } else if (selection2 === $(this)) {
                selection2 = null;
                $(this).removeClass('selectionCarte');
            }
        }
    });

    $('.boutonLancer').click(function (e) {

        if (selection1 !== null && selection2 !== null) {
            $.get('lancer-sort-servlet?selection1=' + selection1 + "&selection2=" + selection2);
            alert("-> s1 : " + selection1 + "  ---  s2 : " + selection2);
        }
    });
    
    
    

//    $('.carte img').click(function (e) {
//        if ($(this).hasClass('selectionCarte')){
//            $(this).removeClass('selectionCarte');
//        } else {
//            $(this).addClass('selectionCarte');
//        }
//    });
});