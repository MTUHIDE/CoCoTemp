
    /*
     * Creates the default leaflet map
     * @return {Leaflet Map}
     */
    function createMap() {

    /* Limit map view to the USA */
    var maxBounds = L.latLngBounds(
        L.latLng(5.090944175, -179.44140625), //Southwest
        L.latLng(71.8014103014, -32.16796875)  //Northeast
    );

    /* Initialize Map */
    var map = L.map('map', {
        dragging: true,
        zoomControl: false,
        minZoom: 4
    });
    map.setView([38.240804, -100.692784], 4);
        map.setMaxBounds(maxBounds);

        //Zoom buttons
        L.control.zoom({
            position: 'bottomright'
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

    var siteMarkers=[];
    markerCluster.clearLayers();

    populateSites(myMap,markerCluster,0);

    var north =0;
    var east = 0;
    var south = 0;
    var west =0;


    switch(FIPS) {
        case "01":
            north=35;
            west=-88.46;
            south=30.225797;
            east=-84.906629;
            break;
        case "02":
            north=71.386475;
            west=-176.903822;
            south=51.351493;
            east=-130.009071;
            break;
        case "04":
            north=37.001310;
            west=-114.808007;
            south=31.333953;
            east=-109.046028;
            break;
        case "05":
            north=36.497909;
            west=-94.617911;
            south=33.013336;
            east=-89.647367;
            break;
        case "06":
            north=42.005747;
            west=-124.399836;
            south=32.536733;
            east=-114.133203;
            break;
        case "08":
            north=41.003444;
            west=-109.060253;
            south=36.992426;
            east=-102.041524;
            break;
        case "09":
            north=42.050587;
            west=-73.727775;
            south=40.980144;
            east=-71.786994;
            break;
        case "10":
            north=39.839007;
            west=-75.788658;
            south=38.451013;
            east=-75.048939;
            break;
        case "11":
            north=38.99511;
            west=-77.119759;
            south=38.791645;
            east=-76.909395;
            break;
        case "12":
            north=31.000888;
            west=-87.634938;
            south=24.523096;
            east=-80.031362;
            break;
        case "13":
            north=35.000659;
            west=-85.605165;
            south=30.357851;
            east=-80.839729;
            break;
        case "15":
            north=28.402123;
            west=-178.334698;
            south=18.910361;
            east=-154.806773;
            break;
        case "16":
            north=49.001146;
            west=-117.243027;
            south=41.988057;
            east=-111.043564;
            break;
        case "17":
            north=42.508481;
            west=-91.513079;
            south=36.970298;
            east=-87.494756;
            break;
        case "18":
            north=41.760592;
            west=-88.09776;
            south=37.771742;
            east=-84.784579;
            break;
        case "19":
            north=43.501196;
            west=-96.639704;
            south=40.375501;
            east=-90.140061;
            break;
        case "20":
            north=40.003162;
            west=-102.051744;
            south=36.993016;
            east=-94.588413;
            break;
        case "21":
            north=39.147458;
            west=-89.571509;
            south=36.497129;
            east=-81.964971;
            break;
        case "22":
            north=33.019457;
            west=-94.043147;
            south=28.928609;
            east=-88.817017;
            break;
        case "23":
            north=47.459686;
            west=-71.083924;
            south=42.977764;
            east=-66.949895;
            break;
        case "24":
            north=39.723043;
            west=-79.487651;
            south=37.911717;
            east=-75.048939;
            break;
        case "25":
            north=42.886589;
            west=-73.508142;
            south=41.237964;
            east=-69.928393;
            break;
        case "26":
            north=48.2388;
            west=-90.418136;
            south=41.696118;
            east=-82.413474;
            break;
        case "27":
            north=49.384358;
            west=-97.239209;
            south=43.499356;
            east=-89.491739;
            break;
        case "28":
            north=34.996052;
            west=-91.655009;
            south=30.173943;
            east=-88.097888;
            break;
        case "29":
            north=40.61364;
            west=-95.774704;
            south=35.995683;
            east=-89.098843;
            break;
        case "30":
            north=49.00139;
            west=-116.050003;
            south=44.358221	;
            east=-104.039138;
            break;
        case "31":
            north=43.001708;
            west=-104.053514;
            south=39.999998;
            east=-95.30829;
            break;
        case "32":
            north=42.002207;
            west=-120.005746;
            south=35.001857;
            east=-114.039648;
            break;
        case "33":
            north=45.305476;
            west=-72.557247;
            south=42.69699;
            east=-70.610621;
            break;
        case "34":
            north=41.357423;
            west=-75.559614;
            south=38.928519;
            east=-73.893979;
            break;
        case "35":
            north=37.000232;
            west=-109.050173;
            south=31.332301;
            east=-103.001964;
            break;
        case "36":
            north=45.01585;
            west=-79.762152;
            south=40.496103;
            east=-71.856214;
            break;
        case "37":
            north=36.588117;
            west=-84.321869;
            south=33.842316;
            east=-75.460621;
            break;
        case "38":
            north=49.000574;
            west=-104.0489;
            south=45.935054;
            east=-96.554507;
            break;
        case "39":
            north=41.977523;
            west=-84.820159;
            south=38.403202;
            east=-80.518693;
            break;
        case "40":
            north=37.002206;
            west=-103.002565;
            south=33.615833;
            east=-94.430662;
            break;
        case "41":
            north=46.292035;
            west=-124.566244;
            south=41.991794;
            east=-116.463504;
            break;
        case "42":
            north=42.26986;
            west=-80.519891;
            south=39.7198;
            east=-74.689516;
            break;
        case "44":
            north=42.018798;
            west=-71.862772;
            south=41.146339;
            east=-71.12057;
            break;
        case "45":
            north=35.215402;
            west=-83.35391;
            south=32.0346;
            east=-78.54203;
            break;
        case "46":
            north=45.94545;
            west=-104.057698;
            south=42.479635;
            east=-96.436589;
            break;
        case "47":
            north=36.678118;
            west=-90.310298;
            south=34.982972;
            east=-81.6469;
            break;
        case "48":
            north=36.500704;
            west=-106.645646;
            south=25.837377;
            east=-93.508292;
            break;
        case "49":
            north=42.001567;
            west=-114.052962;
            south=36.997968;
            east=-109.041058;
            break;
        case "50":
            north=45.016659;
            west=-73.43774;
            south=42.726853;
            east=-71.464555;
            break;
        case "51":
            north=39.466012;
            west=-83.675395;
            south=36.540738;
            east=-75.242266;
            break;
        case "53":
            north=49.002494;
            west=-124.763068;
            south=45.543541;
            east=-116.915989;
            break;
        case "54":
            north=40.638801;
            west=-82.644739;
            south=37.201483;
            east=-77.719519;
            break;
        case "55":
            north=47.080621;
            west=-92.888114;
            south=42.491983;
            east=-86.805415;
            break;
        case "56":
            north=45.005904;
            west=-111.056888;
            south=40.994746;
            east=-104.05216;
            break;
        default:
            return;
    }
    var today = new Date();
    var dd = String(today.getDate()).padStart(2, '0');
    var mm = String(today.getMonth() + 1).padStart(2, '0');
    var yyyy = today.getFullYear();
    var year_ago = new Date();
    year_ago.setFullYear(today.getFullYear()-1)
    var olddd = String(year_ago.getDate()).padStart(2, '0');
    var oldmm = String(year_ago.getMonth() + 1).padStart(2, '0');
    var oldyyyy = year_ago.getFullYear();


    today = yyyy+'-'+mm+'-'+dd;
    year_ago = oldyyyy+'-'+oldmm+'-'+olddd;
    var target = document.getElementById("map");

    var opts = {
        lines: 13, // The number of lines to draw
        length: 38, // The length of each line
        width: 17, // The line thickness
        radius: 45, // The radius of the inner circle
        scale: 1, // Scales overall size of the spinner
        corners: 1, // Corner roundness (0..1)
        color: '#000000', // CSS color or array of colors
        fadeColor: 'transparent', // CSS color or array of colors
        speed: 1, // Rounds per second
        rotate: 0, // The rotation offset
        animation: 'spinner-line-fade-quick', // The CSS animation name for the lines
        direction: 1, // 1: clockwise, -1: counterclockwise
        zIndex: 2e9, // The z-index (defaults to 2000000000)
        className: 'spinner', // The CSS class to assign to the spinner
        top: '50%', // Top position relative to parent
        left: '50%', // Left position relative to parent
        shadow: '0 0 1px transparent', // Box-shadow for the lines
        position: 'absolute' // Element positioning
    };
    var spinner = new Spinner(opts).spin(target);

        $.ajax({
            method: 'get',
            datatype: 'json',
            headers: {"Token": "uZqEMqAJLHUBrZwgzdJvIdLodhoGWMKJ"},
            url: 'https://cocotemp-proxy.herokuapp.com/https://www.ncei.noaa.gov/access/services/search/v1/data?dataset=global-hourly&startDate='+year_ago+'&endDate='+today+'&dataTypes=TMP&limit=1000&offset='+offset+'&bbox='+north+','+west+','+south+','+east,
            success: function (data) {
                if (data.count == 0) {
                    return;
                }
                if(firstTime)
                {
                    sitesLeft=data.count;
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
                    iconAnchor: [25, 41], // point of the icon which will correspond to marker's location
                    popupAnchor: [-25, -41] // point from which the popup should open relative to the iconAnchor
                });
                for (var i = 0; i <actualStations-1; i++) {
                    //Add the station locations to the map.
                    var myMarker = L.marker([data.results[i].boundingPoints[0].point[1], data.results[i].boundingPoints[0].point[0]], {icon: NOAAIcon});
                    myMarker.bindPopup('<a href="NOAASite/' + data.results[i].stations[0].id + '">' + data.results[i].stations[0].name + '</a>');
                    siteMarkers.push(myMarker);
                    markerCluster.addLayer(myMarker);
                }
                markerCluster.addTo(myMap);
                if(sitesLeft>0)
                {
                    populateNOAASites(myMap,markerCluster,offset,sitesLeft,0,FIPS);
                }
                spinner.stop();
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

        document.getElementById("state-select").onchange = function(){populateNOAASites(myMap, markerCluster, 1, 0, 1, document.getElementById("state-select").value);}

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
    var sidebar = L.control.sidebar({
        autopan: false,       // whether to maintain the centered map point when opening the sidebar
        closeButton: true,    // whether t add a close button to the panes
        container: 'sidebar',
        position: 'left'
    }).addTo(myMap);

}


