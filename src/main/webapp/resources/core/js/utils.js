
function range(low, high) {
    var l = [];
    for (var i = low; i <= high; i++) {
        l.push(i);
    }
    return l;
}

/**
 * 
 * @param {type} values
 * @param {type} current
 * @returns {undefined}
 */
function chooseValue(values, current) {
    var result = 0;
    $.each(values, function (k, v) {
        if (current <= v.key) {
            result = v.value;
            return false;
        }
    });
    return result;
}