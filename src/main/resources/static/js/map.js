/*
 * Creates the default leaflet map
 * @return {Leaflet Map}
 */
function createMap() {

    /* Limit map view to the USA */
    var maxBounds = L.latLngBounds(
        L.latLng(5.090944175, -172.44140625), //Southwest
        L.latLng(71.8014103014, -32.16796875)  //Northeast
    );

    /* Initialize Map */
    var map = L.map('map', {
        dragging: true,
        zoomControl: false,
        minZoom: 4
    });
    map.setView([37.0902, -95.7129], 4);
    map.setMaxBounds(maxBounds);

    //Zoom buttons
    L.control.zoom({
        position: 'topleft'
    }).addTo(map);

    var layer = L.esri.basemapLayer('Streets').addTo(map);

    return map;
}


/*
 * Takes a lat/lon coordinate and marks the location on the map
 * @param {Leaflet map} myMap
 * @param {Leaflet marker} marker - previous mark on map
 * @param {Number} latitudeInput
 * @param {Number} longitudeInput
 * @return {Leaflet marker} marker - new mark on map
 */
function markLocation(myMap, latitudeInput, longitudeInput) {

    if (latitudeInput === "" || longitudeInput === "") {
        return;
    }

    /* Remove markers from previous search */
    myMap.eachLayer(function(layer){
        if(layer instanceof L.Marker) {
            myMap.removeLayer(layer);
        }
    });

    var marker = L.marker([parseFloat(latitudeInput), parseFloat(longitudeInput)]).addTo(myMap);

    /* Zoom to new location */
    var zoomLevel = 12;
    if (myMap.getZoom() > zoomLevel) {
        zoomLevel = myMap.getZoom();
    }

    myMap.setView([parseFloat(latitudeInput), parseFloat(longitudeInput)], zoomLevel);

    return marker;
}

/*
 * Change the map layer
 * @param {String} The basemap layer ('Streets', 'Imagery')
 */
function changeBasemap(myMap, basemap) {
    /* Remove the current base layer maps */
    myMap.eachLayer(function(layer){
        if(!(layer instanceof L.Marker)) {
            myMap.removeLayer(layer);
        }
    });

    var layer = L.esri.basemapLayer(basemap);
    myMap.addLayer(layer);

    if (basemap === 'Imagery') {
        var layerLabels = L.esri.basemapLayer(basemap + 'Labels');
        myMap.addLayer(layerLabels);
    } else if (basemap === 'Streets') {

    }
}

/*
 * Add all site pins to map
 * @param {Leaflet Map} myMap
 */
function populateSites(myMap,markerCluster) {
    var siteMarkers = [];

    $.ajax({
        method: 'post',
        url: '/cocotemp/sites.json',
        success: function (data) {
            if (data.length === 0) {
                return;
            }

            for (var i = 0; i < data.length; i++) {
                //Add the station locations to the map.
                var myMarker = L.marker([data[i].siteLatitude, data[i].siteLongitude])
                myMarker.bindPopup('<a href="site/' + data[i].id + '">' + data[i].siteName + '</a>');
                siteMarkers.push(myMarker);
                markerCluster.addLayer(myMarker);
            }
            markerCluster.addTo(myMap)
        },
    });
}


