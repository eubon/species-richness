function resetAuto($el) {
    $el.val("").prop("disabled", true);
    $el.siblings("input[type='hidden']").val("");
}

function speciesValue() {
    var higherTaxonId = $("#index-form\\:auto-higher-taxon_hinput").val();
    if (higherTaxonId && parseInt(higherTaxonId)) {
        $("#index-form\\:auto-species_input").removeProp("disabled").attr("placeholder", "Start by typing");
    } else {
        resetAuto($("#index-form\\:auto-species_input"));
    }
}

$(document).ready(function () {
    $('[data-toggle="tooltip"]').tooltip();
    $('[data-toggle="popover"]').popover();

    speciesValue(); //default

    $("#index-form\\:auto-higher-taxon_input").bind("click select", function () {
        speciesValue();
    });

});