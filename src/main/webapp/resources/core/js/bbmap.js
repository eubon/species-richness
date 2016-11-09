/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
var drawnRectangles = [];

function clearRectangles() {
    if (drawnRectangles.length === 1) {
        drawnRectangles[0].setMap(null);
    } else if (drawnRectangles.length > 1) {
        console.log("More than one rectangle in array");
    }
    drawnRectangles = [];
}

function writeBounds(rectangle) {
    $("[id$='bbNorth']").val(rectangle.getBounds().getNorthEast().lat());
    $("[id$='bbEast']").val(rectangle.getBounds().getNorthEast().lng());
    $("[id$='bbSouth']").val(rectangle.getBounds().getSouthWest().lat());
    $("[id$='bbWest']").val(rectangle.getBounds().getSouthWest().lng());
}

$(document).ready(function () {
    var map = new google.maps.Map($("#map").get(0), {
        zoom: 3,
        center: {lat: 50.5, lng: 15},
        mapTypeId: google.maps.MapTypeId.TERRAIN
    });

    var drawingManager = new google.maps.drawing.DrawingManager({
        drawingMode: google.maps.drawing.OverlayType.MARKER,
        drawingControl: true,
        drawingControlOptions: {
            position: google.maps.ControlPosition.BOTTOM_CENTER,
            drawingModes: [
                google.maps.drawing.OverlayType.RECTANGLE
            ]
        },
        rectangleOptions: {
            editable: true,
            draggable: true
        }
    });

    var defaultRec = new google.maps.Rectangle({
        bounds: new google.maps.LatLngBounds(new google.maps.LatLng(29.0, -10.0), new google.maps.LatLng(72.0, 40.0)),
        editable: true,
        draggable: true,
        map: map
    });
    drawnRectangles.push(defaultRec);
    defaultRec.addListener('bounds_changed', function () {
        writeBounds(this);
    });
    drawingManager.addListener('rectanglecomplete', function (rect) {
        writeBounds(rect);
        clearRectangles();
        rect.addListener('bounds_changed', function () {
            writeBounds(this);
        });
        drawnRectangles.push(rect);
    });
    drawingManager.setMap(map);
});
