
//var infowindow = new google.maps.InfoWindow();
var colours = {
    occurences: [{key: 100, value: "#f7ec75"}, {key: 500, value: "#f7da37"}, {key: 1000, value: "#f7c775"}, {key: 3000, value: "#f7b036"},
        {key: 5000, value: "#f79536"}, {key: 10000, value: "#f76836"}, {key: Number.MAX_VALUE, value: "#f73f36"}],
    species: [{key: 5, value: "#d9ed96"}, {key: 10, value: "#cce227"}, {key: 20, value: "#8cd701"}, {key: 40, value: "#5bbf21"},
        {key: 80, value: "#339e36"}, {key: 100, value: "#007a3d"}, {key: Number.MAX_VALUE, value: "#235033"}]
};
var opacity = 0.85;
var detailLocked = false;

function detailContent(property, geometry, locked) {
    var corners = [];
    geometry.forEachLatLng(function (latlng) {
        corners.push(latlng);
    });
    var content = '<div class="larger"><strong>' + property.records + '</strong> ' + property.label + '</div>\n\
                        <div class="smaller cell">\n\
                            <h5>Cell:</h5>\n\
                            <p>Bottom left: ' + corners[0].lat() + '° ' + corners[0].lng() + '°<br/>\n\
                            Top right: ' + corners[2].lat() + '° ' + corners[2].lng() + '°</p>\n\
                        </div>';
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
                    occurences: {records: c.numOccurences, label: "occurences"},
                    species: {records: c.numSpecies, label: "species"}
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

function showLayer(position, layers, map, property, colours, opacity) {
    //clean features
    map.data.forEach(function (feature) {
        map.data.remove(feature);
    });
    //load new features
    map.data.addGeoJson(layers[position].collection);
    map.data.setStyle(function (feature) {
        var r = feature.getProperty(property).records;
        var colour = chooseValue(colours, r);
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
        initPlayer(years, geoJSONs, map, 'occurences', colours);
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
        initPlayer(years, geoJSONs, map, view, colours);
        detailLocked = false;
        $("#details-info").html("");
    });
}
