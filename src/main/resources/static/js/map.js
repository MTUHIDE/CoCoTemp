

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

function populateNOAASites(myMap,markerCluster,offset,sitesLeft,firstTime,FIPS)
{
    var siteMarkers = [];
    markerCluster.clearLayers();

    populateSites(myMap,markerCluster,0);
        $.ajax({
            method: 'get',
            datatype: 'json',
            headers: {"Token": "uZqEMqAJLHUBrZwgzdJvIdLodhoGWMKJ"},
            url: 'https://www.ncdc.noaa.gov/cdo-web/api/v2/stations?limit=1000&startdate=2001-01-01&datacategoryid=TEMP&offset='+offset+'&locationid=FIPS:'+FIPS,
            success: function (data) {
                if (data.metadata.resultset.count == 0) {
                    return;
                }
                if(firstTime)
                {
                    sitesLeft=data.metadata.resultset.count;
                    firstTime=0;
                }
                var limit =1000;
                var actualStations = limit;
                if (sitesLeft< limit) {
                    actualStations = sitesLeft;
                    sitesLeft=sitesLeft-sitesLeft;
                }
                else
                {
                    sitesLeft = sitesLeft-limit;
                }
                offset = offset+actualStations;
                var NOAAIcon = L.icon({
                    iconUrl: '/cocotemp/images/NOAA-map-marker.png',

                    iconSize: [25, 41], // size of the icon
                    iconAnchor: [22, 94], // point of the icon which will correspond to marker's location
                    popupAnchor: [-3, -76] // point from which the popup should open relative to the iconAnchor
                });
                for (var i = 0; i < actualStations; i++) {
                    //Add the station locations to the map.
                    var myMarker = L.marker([data.results[i].latitude, data.results[i].longitude], {icon: NOAAIcon});
                    myMarker.bindPopup('<a href="NOAASite/' + data.results[i].id + '">' + data.results[i].name + '</a>');
                    siteMarkers.push(myMarker);
                    markerCluster.addLayer(myMarker);
                }
                markerCluster.addTo(myMap);
                if(sitesLeft>0)
                {
                    populateNOAASites(myMap,markerCluster,offset,sitesLeft,0,FIPS);
                }
            },
        });
}


/*
 * Add all site pins to map
 * @param {Leaflet Map} myMap
 */
function populateSites(myMap,markerCluster,init) {
    var siteMarkers = [];

    if(init) {
        L.control.slideMenu('<div style="color: white; background-color:#00395E"><h1><b>NOAA Sites By state</b></h1>' +
            '<input type="radio" id="Alabama" name="state" value="01"/> <label style="padding-right: 5px;" for="Alabama">Alabama</label><br>' +
            '<input type="radio" id ="Alaska"  name="state" value="02"><label style="padding-right: 5px;" for="Alaska">Alaska</label> <br>' +
            '<input type="radio" id ="Arizona"  name="state" value="04"><label style="padding-right: 5px;" for="Arizona">Arizona</label> <br>' +
            '<input type="radio" id ="Arkansas"  name="state" value="05"><label style="padding-right: 5px;" for="Arkansas">Arkansas</label><br>' +
            '<input type="radio" id ="California"  name="state" value="06"><label style="padding-right: 5px;" for="California">California</label><br>' +
            '<input type="radio" id ="Colorado"  name="state" value="08"><label style="padding-right: 5px;" for="Colorado">Colorado</label><br>' +
            '<input type="radio" id ="Connecticut"  name="state" value="09"><label for="Connecticut">Connecticut</label> <br>' +
            '<input type="radio" id ="Delaware"  name="state" value="10"><label style="padding-right: 5px;" for="Delaware">Delaware</label><br>' +
            '<input type="radio" id ="District of Columbia"  name="state" value="11"><label style="padding-right: 5px;" for="District of Columbia">District of Columbia</label><br>' +
            '<input type="radio" id ="Florida"  name="state" value="12"><label style="padding-right: 5px;" for="Florida">Florida</label><br>' +
            '<input type="radio" id ="Georgia"  name="state" value="13"><label style="padding-right: 5px;" for="Georgia">Georgia</label><br>' +
            '<input type="radio" id ="Hawaii"  name="state" value="15"><label style="padding-right: 5px;" for="Hawaii">Hawaii</label><br>' +
            '<input type="radio" id ="Idaho"  name="state" value="16"><label style="padding-right: 5px;" for="Idaho">Idaho</label><br>' +
            '<input type="radio" id ="Illinois"  name="state" value="17"><label style="padding-right: 5px;" for="Illinois">Illinois</label><br>' +
            '<input type="radio" id ="Indiana"  name="state" value="18"><label style="padding-right: 5px;" for="Indiana">Indiana</label><br>' +
            '<input type="radio" id ="Iowa"  name="state" value="19"><label style="padding-right: 5px;" for="Iowa">Iowa</label><br>' +
            '<input type="radio" id ="Kansas"  name="state" value="20"><label style="padding-right: 5px;" for="Kansas">Kansas</label><br>' +
            '<input type="radio" id ="Kentucky"  name="state" value="21"><label style="padding-right: 5px;" for="Kentucky">Kentucky</label><br>' +
            '<input type="radio" id ="Louisiana"  name="state" value="22"><label style="padding-right: 5px;" for="Louisiana">Louisiana</label><br>' +
            '<input type="radio" id ="Maine"  name="state" value="23"><label style="padding-right: 5px;" for="Maine">Maine</label><br>' +
            '<input type="radio" id ="Maryland"  name="state" value="24"><label style="padding-right: 5px;" for="Maryland">Maryland</label><br>' +
            '<input type="radio" id ="Massachusetts"  name="state" value="25"><label style="padding-right: 5px;" for="Massachusetts">Massachusetts</label><br>' +
            '<input type="radio" id ="Michigan"  name="state" value="26"><label style="padding-right: 5px;" for="Michigan">Michigan</label><br>' +
            '<input type="radio" id ="Minnesota"  name="state" value="27"><label style="padding-right: 5px;" for="Minnesota">Minnesota</label><br>' +
            '<input type="radio" id ="Mississippi"  name="state" value="28"><label style="padding-right: 5px;" for="Mississippi">Mississippi</label><br>' +
            '<input type="radio" id ="Missouri"  name="state" value="29"><label style="padding-right: 5px;" for="Missouri">Missouri</label><br>' +
            '<input type="radio" id ="Montana"  name="state" value="30"><label style="padding-right: 5px;" for="Montana">Montana</label><br>' +
            '<input type="radio" id ="Nebraska"  name="state" value="31"><label style="padding-right: 5px;" for="Nebraska">Nebraska</label><br>' +
            '<input type="radio" id ="Nevada"  name="state" value="32"><label style="padding-right: 5px;" for="Nevada">Nevada</label><br>' +
            '<input type="radio" id ="New Hampshire"  name="state" value="33"><label style="padding-right: 5px;" for="New Hampshire">New Hampshire</label><br>' +
            '<input type="radio" id ="New Jersey"  name="state" value="34"><label style="padding-right: 5px;" for="New Jersey">New Jersey</label><br>' +
            '<input type="radio" id ="New Mexico"  name="state" value="35"><label style="padding-right: 5px;" for="New Mexico">New Mexico</label><br>' +
            '<input type="radio" id ="New York"  name="state" value="36"><label style="padding-right: 5px;" for="New York">New York</label><br>' +
            '<input type="radio" id ="North Carolina"  name="state" value="37"><label style="padding-right: 5px;" for="North Carolina">North Carolina</label><br>' +
            '<input type="radio" id ="North Dakota"  name="state" value="38"><label style="padding-right: 5px;" for="North Dakota">North Dakota</label><br>' +
            '<input type="radio" id ="Ohio"  name="state" value="39"><label style="padding-right: 5px;" for="Ohio">Ohio</label><br>' +
            '<input type="radio" id ="Oklahoma"  name="state" value="40"><label style="padding-right: 5px;" for="Oklahoma">Oklahoma</label><br>' +
            '<input type="radio" id ="Oregon"  name="state" value="41"><label style="padding-right: 5px;" for="Oregon">Oregon</label><br>' +
            '<input type="radio" id ="Pennsylvania"  name="state" value="42"><label style="padding-right: 5px;" for="Pennsylvania">Pennsylvania</label><br>' +
            '<input type="radio" id ="Rhode Island"  name="state" value="44"><label style="padding-right: 5px;" for="Rhode Island">Rhode Island</label><br>' +
            '<input type="radio" id ="South Carolina"  name="state" value="45"><label style="padding-right: 5px;" for="South Carolina">South Carolina</label><br>' +
            '<input type="radio" id ="South Dakota"  name="state" value="46"><label style="padding-right: 5px;" for="South Dakota">South Dakota</label><br>' +
            '<input type="radio" id ="Tennessee"  name="state" value="47"><label style="padding-right: 5px;" for="Tennessee">Tennessee</label><br>' +
            '<input type="radio" id ="Texas"  name="state" value="48"><label style="padding-right: 5px;" for="Texas">Texas</label><br>' +
            '<input type="radio" id ="Utah"  name="state" value="49"><label style="padding-right: 5px;" for="Utah">Utah</label><br>' +
            '<input type="radio" id ="Vermont"  name="state" value="50"><label style="padding-right: 5px;" for="Vermont">Vermont</label><br>' +
            '<input type="radio" id ="Virginia"  name="state" value="51"><label style="padding-right: 5px;" for="Virginia">Virginia</label><br>' +
            '<input type="radio" id ="Washington"  name="state" value="53"><label style="padding-right: 5px;" for="Washington">Washington</label><br>' +
            '<input type="radio" id ="West Virginia"  name="state" value="54"><label style="padding-right: 5px;" for="West Virginia">West Virginia</label><br>' +
            '<input type="radio" id ="Wisconsin"  name="state" value="55"><label style="padding-right: 5px;" for="Wisconsin">Wisconsin</label><br>' +
            '<input type="radio" id ="Wyoming"  name="state" value="56"><label style="padding-right: 5px;" for="Wyoming">Wyoming</label><br>' +
            '</div>', {width: '150px'}).addTo(myMap);
        document.getElementById("Alabama").onclick = function(){populateNOAASites(myMap, markerCluster, 1, 0, 1, document.getElementById("Alabama").value);}
        document.getElementById("Alaska").onclick = function(){populateNOAASites(myMap, markerCluster, 1, 0, 1, document.getElementById("Alaska").value);}
        document.getElementById("Arizona").onclick = function(){populateNOAASites(myMap, markerCluster, 1, 0, 1, document.getElementById("Arizona").value);}
        document.getElementById("Arkansas").onclick = function(){populateNOAASites(myMap, markerCluster, 1, 0, 1, document.getElementById("Arkansas").value);}
        document.getElementById("California").onclick = function(){populateNOAASites(myMap, markerCluster, 1, 0, 1, document.getElementById("California").value);}
        document.getElementById("Colorado").onclick = function(){populateNOAASites(myMap, markerCluster, 1, 0, 1, document.getElementById("Colorado").value);}
        document.getElementById("Connecticut").onclick = function(){populateNOAASites(myMap, markerCluster, 1, 0, 1, document.getElementById("Connecticut").value);}
        document.getElementById("Delaware").onclick = function(){populateNOAASites(myMap, markerCluster, 1, 0, 1, document.getElementById("Delaware").value);}
        document.getElementById("Florida").onclick = function(){populateNOAASites(myMap, markerCluster, 1, 0, 1, document.getElementById("Florida").value);}
        document.getElementById("Georgia").onclick = function(){populateNOAASites(myMap, markerCluster, 1, 0, 1, document.getElementById("Georgia").value);}
        document.getElementById("Hawaii").onclick = function(){populateNOAASites(myMap, markerCluster, 1, 0, 1, document.getElementById("Hawaii").value);}
        document.getElementById("Idaho").onclick = function(){populateNOAASites(myMap, markerCluster, 1, 0, 1, document.getElementById("Idaho").value);}
        document.getElementById("Illinois").onclick = function(){populateNOAASites(myMap, markerCluster, 1, 0, 1, document.getElementById("Illinois").value);}
        document.getElementById("Indiana").onclick = function(){populateNOAASites(myMap, markerCluster, 1, 0, 1, document.getElementById("Indiana").value);}
        document.getElementById("Iowa").onclick = function(){populateNOAASites(myMap, markerCluster, 1, 0, 1, document.getElementById("Iowa").value);}
        document.getElementById("Kansas").onclick = function(){populateNOAASites(myMap, markerCluster, 1, 0, 1, document.getElementById("Kansas").value);}
        document.getElementById("Kentucky").onclick = function(){populateNOAASites(myMap, markerCluster, 1, 0, 1, document.getElementById("Kentucky").value);}
        document.getElementById("Louisiana").onclick = function(){populateNOAASites(myMap, markerCluster, 1, 0, 1, document.getElementById("Louisiana").value);}
        document.getElementById("Maine").onclick = function(){populateNOAASites(myMap, markerCluster, 1, 0, 1, document.getElementById("Maine").value);}
        document.getElementById("Maryland").onclick = function(){populateNOAASites(myMap, markerCluster, 1, 0, 1, document.getElementById("Maryland").value);}
        document.getElementById("Massachusetts").onclick = function(){populateNOAASites(myMap, markerCluster, 1, 0, 1, document.getElementById("Massachusetts").value);}
        document.getElementById("Michigan").onclick = function(){populateNOAASites(myMap, markerCluster, 1, 0, 1, document.getElementById("Michigan").value);}
        document.getElementById("Minnesota").onclick = function(){populateNOAASites(myMap, markerCluster, 1, 0, 1, document.getElementById("Minnesota").value);}
        document.getElementById("Mississippi").onclick = function(){populateNOAASites(myMap, markerCluster, 1, 0, 1, document.getElementById("Mississippi").value);}
        document.getElementById("Missouri").onclick = function(){populateNOAASites(myMap, markerCluster, 1, 0, 1, document.getElementById("Missouri").value);}
        document.getElementById("Montana").onclick = function(){populateNOAASites(myMap, markerCluster, 1, 0, 1, document.getElementById("Montana").value);}
        document.getElementById("Nebraska").onclick = function(){populateNOAASites(myMap, markerCluster, 1, 0, 1, document.getElementById("Nebraska").value);}
        document.getElementById("Nevada").onclick = function(){populateNOAASites(myMap, markerCluster, 1, 0, 1, document.getElementById("Nevada").value);}
        document.getElementById("New Hampshire").onclick = function(){populateNOAASites(myMap, markerCluster, 1, 0, 1, document.getElementById("New Hampshire").value);}
        document.getElementById("New Jersey").onclick = function(){populateNOAASites(myMap, markerCluster, 1, 0, 1, document.getElementById("New Jersey").value);}
        document.getElementById("New Mexico").onclick = function(){populateNOAASites(myMap, markerCluster, 1, 0, 1, document.getElementById("New Mexico").value);}
        document.getElementById("New York").onclick = function(){populateNOAASites(myMap, markerCluster, 1, 0, 1, document.getElementById("New York").value);}
        document.getElementById("North Carolina").onclick = function(){populateNOAASites(myMap, markerCluster, 1, 0, 1, document.getElementById("North Carolina").value);}
        document.getElementById("North Dakota").onclick = function(){populateNOAASites(myMap, markerCluster, 1, 0, 1, document.getElementById("North Dakota").value);}
        document.getElementById("Ohio").onclick = function(){populateNOAASites(myMap, markerCluster, 1, 0, 1, document.getElementById("Ohio").value);}
        document.getElementById("Oklahoma").onclick = function(){populateNOAASites(myMap, markerCluster, 1, 0, 1, document.getElementById("Oklahoma").value);}
        document.getElementById("Oregon").onclick = function(){populateNOAASites(myMap, markerCluster, 1, 0, 1, document.getElementById("Oregon").value);}
        document.getElementById("Pennsylvania").onclick = function(){populateNOAASites(myMap, markerCluster, 1, 0, 1, document.getElementById("Pennsylvania").value);}
        document.getElementById("Rhode Island").onclick = function(){populateNOAASites(myMap, markerCluster, 1, 0, 1, document.getElementById("Rhode Island").value);}
        document.getElementById("South Carolina").onclick = function(){populateNOAASites(myMap, markerCluster, 1, 0, 1, document.getElementById("South Carolina").value);}
        document.getElementById("South Dakota").onclick = function(){populateNOAASites(myMap, markerCluster, 1, 0, 1, document.getElementById("South Dakota").value);}
        document.getElementById("Tennessee").onclick = function(){populateNOAASites(myMap, markerCluster, 1, 0, 1, document.getElementById("Tennessee").value);}
        document.getElementById("Texas").onclick = function(){populateNOAASites(myMap, markerCluster, 1, 0, 1, document.getElementById("Texas").value);}
        document.getElementById("Utah").onclick = function(){populateNOAASites(myMap, markerCluster, 1, 0, 1, document.getElementById("Utah").value);}
        document.getElementById("Vermont").onclick = function(){populateNOAASites(myMap, markerCluster, 1, 0, 1, document.getElementById("Vermont").value);}
        document.getElementById("Virginia").onclick = function(){populateNOAASites(myMap, markerCluster, 1, 0, 1, document.getElementById("Virginia").value);}
        document.getElementById("Washington").onclick = function(){populateNOAASites(myMap, markerCluster, 1, 0, 1, document.getElementById("Washington").value);}
        document.getElementById("West Virginia").onclick = function(){populateNOAASites(myMap, markerCluster, 1, 0, 1, document.getElementById("West Virginia").value);}
        document.getElementById("Wisconsin").onclick = function(){populateNOAASites(myMap, markerCluster, 1, 0, 1, document.getElementById("Wisconsin").value);}
        document.getElementById("Wyoming").onclick = function(){populateNOAASites(myMap, markerCluster, 1, 0, 1, document.getElementById("Wyoming").value);}
        document.getElementById("District of Columbia").onclick = function(){populateNOAASites(myMap, markerCluster, 1, 0, 1, document.getElementById("District of Columbia").value);}


    }

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


