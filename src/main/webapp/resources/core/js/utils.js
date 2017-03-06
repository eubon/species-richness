
function range(low, high) {
    var l = [];
    for (var i = low; i <= high; i++) {
        l.push(i);
    }
    return l;
}

/**
 * 
 * @param {array} values
 * @param {int} current
 * @returns {undefined}
 */
function chooseValue(values, current, allowZero) {
    if ((current === 0.0 && !allowZero) || values.length === 0) {
        return 0;
    }
    var result = 0.0;
    $.each(values, function (k, v) {
        if (current < v.key) {
            result = v.value;
            return false;
        }
    });
    return result;
}

function speciesList(values) {
    var out = '<ul>';
    $.each(values, function (k, v) {
        out += '<li><a href="http://www.gbif.org/species/' + v.key + '" title="show in gbif" target="_blank">' + v.name + '</a></li>';
    });
    return out + '</ul>';
}