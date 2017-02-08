
var views = {
    occurences: {
        allowZero: false,
        colours: [{key: 100, value: "#f7ec75"}, {key: 500, value: "#f7da37"}, {key: 1000, value: "#f7c775"}, {key: 3000, value: "#f7b036"},
            {key: 5000, value: "#f79536"}, {key: 10000, value: "#f76836"}, {key: Number.MAX_VALUE, value: "#f73f36"}]
    },
    species: {
        allowZero: false,
        colours: [{key: 5, value: "#d9ed96"}, {key: 10, value: "#cce227"}, {key: 20, value: "#8cd701"}, {key: 40, value: "#5bbf21"},
            {key: 80, value: "#339e36"}, {key: 100, value: "#007a3d"}, {key: Number.MAX_VALUE, value: "#235033"}]
    },
    ratio: {
        allowZero: false,
        colours: [{key: 0.1, value: "#d6e7e5"}, {key: 0.2, value: "#bedad9"}, {key: 0.3, value: "#a7d0ce"},
            {key: 0.4, value: "#8bc6cb"}, {key: 0.5, value: "#68bcc8"}, {key: 0.6, value: "#38b2c6"},
            {key: 0.7, value: "#00a9c3"}, {key: 0.8, value: "#009fb8"}, {key: 0.9, value: "#0097af"}, {key: 2.0, value: "#01899e"}]
    }
};
var opacity = 0.85;
var detailLocked = false;

function detailContent(property, geometry, locked) {
    var corners = [];
    geometry.forEachLatLng(function (latlng) {
        corners.push(latlng);
    });
    var content = '<div class="larger">' + property.label + '<strong>' + property.records + '</strong></div>\n\
                        <div class="smaller cell">\n\
                            <h5>Cell:</h5>\n\
                            <p>Bottom left: ' + corners[0].lat() + '° ' + corners[0].lng() + '°<br/>\n\
                            Top right: ' + corners[2].lat() + '° ' + corners[2].lng() + '°</p>\n\
                        </div>';
    if ("specieslist" in property) {
        content += '<div class="smaller cell">\n\
                            <h5>Species List:</h5>';
        content += speciesList(property.specieslist);
        content += '</div>';
    }
    if (locked) {
        content += '<div><a class="btn btn-default dismiss">Back to hover mode</a></div>';
        $("#details-info").on("click", ".dismiss", function (event) {
            detailLocked = false;
            $("#details-info").html("");
        });
    }
    $("#details-info").html(content);
}

function createGeoJSONs(layers) {
    var geoJSONs = [];
    $.each(layers, function (index, layer) {
        var featureCollection = {
            type: "FeatureCollection",
            features: []
        };
        $.each(layer.cells, function (i, c) {
            var feature = {
                type: "Feature",
                geometry: {
                    type: "Polygon",
                    coordinates: [[
                            [c.bounds.bottomLeft.longitude, c.bounds.bottomLeft.latitude], //GeoJSON coordinate is easting, northing !!!!!
                            [c.bounds.topRight.longitude, c.bounds.bottomLeft.latitude],
                            [c.bounds.topRight.longitude, c.bounds.topRight.latitude],
                            [c.bounds.bottomLeft.longitude, c.bounds.topRight.latitude],
                            [c.bounds.bottomLeft.longitude, c.bounds.bottomLeft.latitude]
                        ]]
                },
                properties: {
                    occurences: {records: c.numOccurences, label: "Occurences: "},
                    species: {records: c.numSpecies, label: "Species: ", specieslist: c.species},
                    ratio: {records: c.taxonRatio === 0 ? 0 : (c.taxonRatio).toFixed(3), label: "Ratio of selected species occurences to higher taxon occurences: "}
                }
            };
            featureCollection.features.push(feature);
        });
        geoJSONs.push({year: layer.year,
            collection: featureCollection
        });
    });
    return geoJSONs;
}

function showLayer(position, layers, map, property, view, opacity) {
    //clean features
    map.data.forEach(function (feature) {
        map.data.remove(feature);
    });
    //load new features
    map.data.addGeoJson(layers[position].collection);
    map.data.setStyle(function (feature) {
        var r = feature.getProperty(property).records;
        var colour = chooseValue(view.colours, r, view.allowZero);
        if (colour === 0) {
            colour = "#777";
        }
        return {
            fillOpacity: opacity,
            fillColor: colour,
            strokeWeight: 0.5
        };
    });
    map.data.addListener("mouseover", function (event) {
        if (!detailLocked) {
            detailContent(event.feature.getProperty(property), event.feature.getGeometry(), false);
        }
        /*
         infowindow.setContent(content);
         infowindow.setPosition(event.latLng);
         infowindow.setOptions({pixelOffset: new google.maps.Size(0, -30)});
         infowindow.open(map);
         */
    });
    map.data.addListener("mouseout", function (event) {
        if (!detailLocked) {
            $("#details-info").html("");
        }
    });
    map.data.addListener("click", function (event) {
        detailLocked = true;
        detailContent(event.feature.getProperty(property), event.feature.getGeometry(), detailLocked);
    });
}

function initPlayer(years, geoJSONs, map, view, colours) {
    if (years.length === 0) {
        return;
    }
    showLayer(0, geoJSONs, map, view, colours[view], opacity);
    $("#player").player({
        keys: years,
        step: function (data) {
            showLayer(data.position, geoJSONs, map, view, colours[view], opacity);
        }
    });
}

/**
 * Initialises map and layers
 * @param {type} results cell grids of occurences, fed by jsf bean
 * @param {type} lat latitude of map center
 * @param {type} lon longitude of map center
 * @returns {undefined}
 */
function init(results, lat, lon) {
    //init map
    var map = new google.maps.Map($("#map").get(0), {
        zoom: 3,
        center: {lat: lat, lng: lon},
        mapTypeId: google.maps.MapTypeId.TERRAIN
    });

    var items = results.layers;
    var years = [];
    if (items.length !== 0) {
        $.each(items, function (index, layer) {
            years.push(layer.year);
        });
        //show first layer as default
        var geoJSONs = createGeoJSONs(items);
        //console.log(geoJSONs);
        initPlayer(years, geoJSONs, map, 'occurences', views);
    } else {
        $("#player").find("button").addClass("disabled");
        $("#player").find("select").prop("disabled", true);
        $("#details-info").html("No results found");
    }

    $("#slider").slider({
        max: 100,
        min: 0,
        step: 1,
        value: 85,
        slide: function (event, ui) {
            opacity = ui.value / 100;
            $("#slider-value").text(ui.value + " %");
            map.data.forEach(function (feature) {
                map.data.overrideStyle(feature, {fillOpacity: opacity});
            });
        }
    });

    $("#view-tabs a").click(function () {
        var view = $(this).attr("href").replace("#", "");
        initPlayer(years, geoJSONs, map, view, views);
        detailLocked = false;
        $("#details-info").html("");
    });
}
