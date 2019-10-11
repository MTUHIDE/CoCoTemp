$(function () {
var markersGroups = null;
    //The infocards.
    function populateInfocards() {
        $.ajax({
            type: 'post',
            url: '/cocotemp/dashboard/data.json',
            success: function (data) {
                $('#site-count').text(data.siteCount);
                $('#record-count').text(data.recordCount);
                $('#upload-count').text(data.uploadCount);
                $('#device-count').text(data.deviceCount)
            },
            error: function (results) {
            }
        });
    }

    var myMap = createMap();
    $('#basemaps').on('change', function() {
        changeBasemap(myMap, this.value);
        markersGroups.addTo(myMap);
    });
    markersGroups = new L.MarkerClusterGroup({
        maxClusterRadius: function(zoom) { return 50; }
    });
    markersGroups.addTo(myMap);
    populateSites();

    function populateSites() {
        var siteMarkers = [];

        $.ajax({
            method: 'post',
            url: '/cocotemp/dashboard/sites.json',
            success: function (data) {
                if (data.length === 0) {
                    return;
                }

                for (var i = 0; i < data.length; i++) {
                    //Add the station locations to the map.
                    var myMarker = L.marker([data[i].siteLatitude, data[i].siteLongitude]).addTo(myMap);
                    myMarker.bindPopup('<a href="site/' + data[i].id + '">' + data[i].siteName + '</a>');
                    siteMarkers.push(myMarker);
                    markersGroups.addLayer(myMarker);
                }
                markersGroups.addTo(myMap)
                //Fit to show all markers on the map.
                var myGroup = new L.featureGroup(siteMarkers);
                myMap.fitBounds(myGroup.getBounds())
            },
            error: function (results) {
            }
        });
    }

    _.defer(populateInfocards);
    _.defer(createMap);
});