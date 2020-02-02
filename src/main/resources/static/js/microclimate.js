microclimateMapNameSpace = function(){
    var myMap = null;
    var markersGroup = null;
    var siteMarkers = [];



    var mapMarkerUrls = [
        ["black", "/cocotemp/images/marker-icon-2x-black.png", 0],
        ["green", "/cocotemp/images/marker-icon-2x-green.png", 0],
        ["red", "/cocotemp/images/marker-icon-2x-red.png", 0],
        ["violet", "/cocotemp/images/marker-icon-2x-violet.png", 0],
        ["yellow", "/cocotemp/images/marker-icon-2x-yellow.png", 0]
    ];

    function init(){

        // Create map of sites
        myMap = createMap();
        $('#basemaps').on('change', function() {
            changeBasemap(myMap, this.value);
            markersGroup.addTo(myMap);
        });
        markersGroup = new L.MarkerClusterGroup({
            maxClusterRadius: function(zoom) { return 50; }
        });
        markersGroup.addTo(myMap);
    }

    function getAvailableMarker() {
        for (var i = 0; i < mapMarkerUrls.length; i++) {
            if (mapMarkerUrls[i][2] === 0) {
                return mapMarkerUrls[i];
            }
        }
        return null;
    }

    function makeMarkerAvailable(color) {
        for (var i = 0; i < mapMarkerUrls.length; i++) {
            if (mapMarkerUrls[i][0] === color) {
                mapMarkerUrls[i][2] = 0;
            }
        }
    }

    /*
     * Add given site pins to map
     * @param {Leaflet Map} myMap
     */
    function populateMapWithSites(sites) {

        clearMapOfMarkers();

        siteMarker = L.Marker.extend({
            options: {
                siteID: 'Site ID',
                siteName: 'Site Name',
                onChart: false,
                color: null,
                metadata: null
            }
        });

        for(var i = 0; i < sites.length; i++) {

            var site = sites[i][0];

            //Don't readd sites already on graph and map
            var sitesOnGraph = microclimateGraphNameSpace.getSitesOnGraph();
            if(sitesOnGraph.indexOf(site.id) >= 0) {
                continue;
            }
            var metadata = sites[i][1];
            var point = L.latLng([site.siteLatitude, site.siteLongitude]);
            var myMarker = new siteMarker(point,{options: {siteID: site.id, siteName: site.siteName, onChart: false, metadata: metadata} });

            // Create container with site details and ability to add site data to graph
            var container = $('<div />');
            container.html('<a href="site/' + site.id + '" target="_blank">View Site: ' + site.siteName + '</a><br/>');
            var link = $('<a href="#"">Add to Graph</a>').click({marker: myMarker}, function(event) {
                markerClick(event.data.marker, event.target);
                $('.nav-tabs a[href="#graph-filters"]').tab("show");
            })[0];
            container.append(link);

            // Insert the container into the popup
            myMarker.bindPopup(container[0]);
            markersGroup.addLayer(myMarker);
            siteMarkers.push(myMarker);
        }
        markersGroup.addTo(myMap);
    }

    function populateNOAASites(offset,sitesLeft,firstTime,FIPS)
    {
        clearMapOfMarkers();
        var target= document.getElementById("temperature-chart");
        var opts={
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

        var siteMarkers=[];


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


        $.ajax({
            method: 'get',
            datatype: 'json',
            headers: {"Token": "uZqEMqAJLHUBrZwgzdJvIdLodhoGWMKJ"},
            url: 'https://cors-anywhere.herokuapp.com/https://www.ncei.noaa.gov/access/services/search/v1/data?dataset=global-hourly&startDate='+year_ago+'&endDate='+today+'&dataTypes=TMP&limit=1000&offset='+offset+'&bbox='+north+','+west+','+south+','+east,
            success: function (data) {
                if (data.count == 0) {
                    return;
                }

                $("#map-popup").remove();
                $("#map-mask").remove();

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
                siteMarker = L.Marker.extend({
                    options: {
                        siteID: 'Site ID',
                        siteName: 'Site Name',
                        onChart: false,
                        color:null,
                        metadata:null
                    }
                });

                for (var i = 0; i <actualStations-1; i++) {
                    //Add the station locations to the map.
                    var metadata={
                        metadataId:data.results[i].stations[0].id,
                        siteID:data.results[i].stations[0].id,
                        site:data.results[i].stations[0].id,
                        environment: "Natural",
                        nearestWater: null,
                        waterDistance: 'no Data ',
                        waterDirection: 'no Data ',
                        maxNightTime: null,
                        minNightTime: null,
                        purpose: "Park or Greenbelt",
                        heightAboveGround: 1.8288,
                        heightAboveFloor: 0,
                        enclosurePercentage: 0,
                        nearestAirflowObstacle: 200,
                        nearestObstacleDegrees: 11,
                        obstacleType: null,
                        areaAroundSensor: 200,
                        riparianArea: false,
                        canopyType: "No canopy",
                        slope: 0,
                        slopeDirection: 0,
                        skyViewFactor: 100
                    }

                    var myMarker = new siteMarker([data.results[i].boundingPoints[0].point[1], data.results[i].boundingPoints[0].point[0]],{options:{siteID:data.results[i].stations[0].id,siteName:data.results[i].stations[0].name,onChart:false,metadata:metadata}});
                    myMarker.setIcon(NOAAIcon);
                    // var myMarker = new siteMarker(point,{options: {siteID: site.id, siteName: site.siteName, onChart: false, metadata: metadata} });

                    var container = $('<div />');
                    container.html('<a href="NOAASite/' + myMarker.options.options.siteID + '" target="_blank">View Site: ' +  myMarker.options.options.siteName + '</a><br/>');
                    var link = $('<a href="#"">Add to Graph</a>').click({marker: myMarker}, function(event) {
                        populateGraphWithNOAAData(event.data.marker, event.target);
                        $('.nav-tabs a[href="#graph-filters"]').tab("show");
                    })[0];
                    container.append(link);

                    // Insert the container into the popup
                    myMarker.bindPopup(container[0]);

                    container.append(link);
                    siteMarkers.push(myMarker);
                    markersGroup.addLayer(myMarker);
                }
                markersGroup.addTo(myMap);
                if(sitesLeft>0)
                {
                    populateNOAASites(offset,sitesLeft,0,FIPS);
                }
                spinner.stop();
            },
        });
    }

    function populateGraphWithNOAAData(marker,popupText) {
        var target= document.getElementById("temperature-chart");
        var opts={
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

        if(marker.options.options.onChart==true){

            var NOAAIcon = L.icon({
                iconUrl: '/cocotemp/images/NOAA-map-marker.png',

                iconSize: [25, 41], // size of the icon
                iconAnchor: [25, 41], // point of the icon which will correspond to marker's location
                popupAnchor: [-25, -41] // point from which the popup should open relative to the iconAnchor
            });
            // Set to default icon and mark as not on map
            microclimateMapNameSpace.makeMarkerAvailable(marker.options.options.color);
            marker.setIcon(NOAAIcon);
            marker.options.options.onChart = false;
            microclimateMapNameSpace.removeMarkerFromMainMap(marker);

            // Remove data from hash
            microclimateGraphNameSpace.removeTemperatureData(marker.options.options.siteID);

            microclimateComparisonNameSpace.removeSiteFromComparisonTool(marker.options.options.siteID);

            // Replot graph
            Plotly.newPlot('temperature-chart', microclimateGraphNameSpace.getTemperatureData(), microclimateGraphNameSpace.getLayout(),{responsive:true});

            // Change text on popup
            popupText.text = "Add to Graph";
            spinner.stop();
            return;
        }
        var today = new Date();
        var dd = String(today.getDate()).padStart(2, '0');
        var mm = String(today.getMonth() + 1).padStart(2, '0');
        var yyyy = today.getFullYear();
        var year_ago = new Date();
        year_ago.setFullYear(today.getFullYear()-1);
        var olddd = String(year_ago.getDate()).padStart(2, '0');
        var oldmm = String(year_ago.getMonth() + 1).padStart(2, '0');
        var oldyyyy = year_ago.getFullYear();

        var station = marker.options.options.siteID;

        today = yyyy+'-'+mm+'-'+dd;
        year_ago = oldyyyy+'-'+oldmm+'-'+olddd;


        var help;

        $.ajax({
            method: 'get',
            datatype: 'json',
            async:true,
            headers: {"Token": "uZqEMqAJLHUBrZwgzdJvIdLodhoGWMKJ"},
            url: 'https://cors-anywhere.herokuapp.com/https://www.ncei.noaa.gov/access/services/data/v1?startDate='+year_ago+'&endDate='+today+'&dataset=global-hourly&dataTypes=TMP&stations='+station+'&format=json&units=metric&includeStationName=1&includeStationLocation=1&includeAttributes=1',
            success: function (data) {
                if (data.length === 0) {
                    return;
                }
                var numberOfDataPoints = Object.keys(data).length;
                var temperature=[];
                var dates=[];
                var tempF=[];
                for (var i = 0; i < numberOfDataPoints; i++) {
                    var tmp = data[i].TMP;
                    var tempSplit = tmp.split(',');
                    var nonconvertedtemp = tempSplit[0];
                    var convertedTemp = nonconvertedtemp / 10;
                    help = convertedTemp;
                    if (convertedTemp != 999.9) {
                        dates.push(new Date(data[i].DATE));
                        temperature.push(parseFloat(convertedTemp.toFixed(1)));
                        tempF.push(parseFloat((convertedTemp * (9 / 5) + 32).toFixed(1)));
                    }
                }
                var iconMarker = microclimateMapNameSpace.getAvailableMarker();
                if (iconMarker === null) {
                    // Warn user that you can't add more sites to graph
                    toastr.options = {
                        "closeButton": true,
                        "positionClass": "toast-top-center"
                    };
                    toastr.warning("Sorry, you cannot add more than 5 sites");
                    return;
                }


                var customIcon = new L.Icon({
                    iconUrl: iconMarker[1],
                    shadowUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/0.7.7/images/marker-shadow.png',
                    iconSize: [25, 41],
                    iconAnchor: [12, 41],
                    popupAnchor: [1, -34],
                    shadowSize: [41, 41]
                });
                iconMarker[2] = 1;
                marker.options.options.color = iconMarker[0];
                marker.setIcon(customIcon);
                microclimateMapNameSpace.addMarkerToMainMap(marker);
                marker.options.options.onChart=true;

                var collectedTemps=null;
                var otherTemps=null;
                if(tempStandard=="C") {
                    collectedTemps = {
                        x: dates,
                        y: temperature,
                        name: marker.options.options.siteName,
                        type: 'scatter',
                        marker: {
                            color: iconMarker[0]
                        },
                        hoverinfo: "y+x"
                    };
                    otherTemps = {
                        x: dates,
                        y: tempF,
                        name: marker.options.options.siteName,
                        type: 'scatter',
                        marker: {
                            color: iconMarker[0]
                        },
                        hoverinfo: "y+x"
                    };
                }
                else
                {
                    collectedTemps = {
                        x: dates,
                        y: tempF,
                        name: marker.options.options.siteName,
                        type: 'scatter',
                        marker: {
                            color: iconMarker[0]
                        },
                        hoverinfo: "y+x"
                    };
                    otherTemps = {
                        x: dates,
                        y: temperature,
                        name: marker.options.options.siteName,
                        type: 'scatter',
                        marker: {
                            color: iconMarker[0]
                        },
                        hoverinfo: "y+x"
                    };
                }
                microclimateGraphNameSpace.addTemperatureData(marker.options.options.siteID,collectedTemps,otherTemps);
                microclimateComparisonNameSpace.addSiteToComparisonTool([marker.options.options.siteID, marker.options.options.siteName, marker.options.options.metadata]);

                Plotly.newPlot('temperature-chart', microclimateGraphNameSpace.getTemperatureData(), microclimateGraphNameSpace.getLayout(),{responsive:true});
                spinner.stop();
            }
        });
        popupText.text = "Remove from Graph";


    }

    function clearMapOfMarkers() {
        markersGroup.clearLayers();
        siteMarkers = [];
    }

    function addMarkerToMainMap(marker) {
        markersGroup.removeLayer(marker);
        myMap.addLayer(marker);
    }

    function removeMarkerFromMainMap(marker) {
        myMap.removeLayer(marker);
        markersGroup.addLayer(marker);
    }

    function resizeMap() {
        myMap.invalidateSize();
    }

    return{
        init:init,
        getAvailableMarker:getAvailableMarker,
        makeMarkerAvailable:makeMarkerAvailable,
        populateMapWithSites:populateMapWithSites,
        clearMapOfMarkers:clearMapOfMarkers,
        resizeMap:resizeMap,
        addMarkerToMainMap:addMarkerToMainMap,
        removeMarkerFromMainMap:removeMarkerFromMainMap,
        populateNOAASites:populateNOAASites

    }
}();

microclimateGraphNameSpace = function(){
    var layout = null;
    var data = [];
    var dataC = [];

    var thresholds=null;

    var celsisusThresholds= [
        {thresholdValue:"0", thresholdName:"Freezing"},
        {thresholdValue:"35", thresholdName:"<a href='https://www.weather.gov/ama/heatindex'>NWS Heat Caution</a>"},
        {thresholdValue:"56", thresholdName:"<a href='https://www.weather.gov/ama/heatindex'>NWS Heat Danger</a>"}
    ];
    var fahrenheitThresholds = [
        {thresholdValue:"32", thresholdName:"Freezing"},
        {thresholdValue:"95", thresholdName:"<a href='https://www.weather.gov/ama/heatindex'>NWS Heat Caution</a>"},
        {thresholdValue:"132.8", thresholdName:"<a href='https://www.weather.gov/ama/heatindex'>NWS Heat Danger</a>"}
    ];

    if(tempStandard=='C'){
        thresholds = celsisusThresholds;
    }
    else{
        thresholds=fahrenheitThresholds;
    }

    // Create site temperature line graph
    function init() {
        createGraph();
    }

    function createGraph() {

        // Graph layout

        if(tempStandard=='F') {
            layout = {
                xaxis: {
                    fixedrange: false,
                    title: 'Time (24hrs)',
                    titlefont: {
                        family: 'Segoe UI',
                        size: 12,
                        color: '#7f7f7f'
                    }
                },
                yaxis: {
                    title: 'F°',
                    titlefont: {
                        family: 'Segoe UI',
                        size: 16,
                        color: '#7f7f7f'
                    }
                },
                shapes: [],
                annotations: [],
                showlegend: true,
                legend: {
                    x: 1.2,
                    y: 1
                },
                margin: {r: 200}
            };

        }
        else{
            layout = {
                xaxis: {
                    fixedrange: false,
                    title: 'Time (24hrs)',
                    titlefont: {
                        family: 'Segoe UI',
                        size: 12,
                        color: '#7f7f7f'
                    }
                },
                yaxis: {
                    title: 'C°',
                    titlefont: {
                        family: 'Segoe UI',
                        size: 16,
                        color: '#7f7f7f'
                    }
                },
                shapes: [],
                annotations: [],
                showlegend: true,
                legend: {
                    x: 1.2,
                    y: 1
                },
                margin: {r: 200}
            };
        }
        updateThresholds();
        var temperature=[];
        var dates=[];

        var collectedTemps = {
            x: dates,
            y: temperature,
            name: 'site\'s temperature',
            type: 'scatter',
            hoverinfo:"y"
        };
        var data = [collectedTemps];

        Plotly.newPlot('temperature-chart', microclimateGraphNameSpace.getTemperatureData(), microclimateGraphNameSpace.getLayout(), {responsive: true});

    }


    function removeThresholdFromGraph(element) {

        celsisusThresholds=celsisusThresholds.filter(function (t) { return t.thresholdValue != element; });
        fahrenheitThresholds=fahrenheitThresholds.filter(function (t) { return t.thresholdValue != (element*(9 / 5) + 32); });
        updateThresholds();
    }

    function addThresholdsToGraph(threshold) {
        celsisusThresholds.push(threshold);

       fahrenheitThresholds.push({thresholdValue: threshold.thresholdValue*(9 / 5) + 32,thresholdName: threshold.thresholdName});

        updateThresholds();
    }


    function updateThresholds() {
        layout.shapes = [];
        layout.annotations = [];

        if(tempStandard=='F'){

            thresholds=fahrenheitThresholds;
        }
        else{
            thresholds=celsisusThresholds;
        }
        thresholds.forEach(function(threshold) {
            var lines = null;
            if(threshold.thresholdValue=='0'||threshold.thresholdValue=='32'){
                lines = {
                    type: 'line',
                    xref: 'paper',
                    yref: 'y',
                    x0: 0,
                    y0: threshold.thresholdValue,
                    x1: 1,
                    y1: threshold.thresholdValue,
                    line: {
                        color: 'rgb(51, 175, 255)',
                        width: 1
                    }
                };
            }
            else if(threshold.thresholdValue=='35'||threshold.thresholdValue=='95'){
                lines = {
                    type: 'line',
                    xref: 'paper',
                    yref: 'y',
                    x0: 0,
                    y0: threshold.thresholdValue,
                    x1: 1,
                    y1: threshold.thresholdValue,
                    line: {
                        color: 'rgb(255,102,0)',
                        width: 1
                    }
                };
            }
            else if(threshold.thresholdValue=='56'||threshold.thresholdValue=='132.8'){
                lines = {
                    type: 'line',
                    xref: 'paper',
                    yref: 'y',
                    x0: 0,
                    y0: threshold.thresholdValue,
                    x1: 1,
                    y1: threshold.thresholdValue,
                    line: {
                        color: 'rgb(206,0,1)',
                        width: 1
                    }
                };
            }
            else{
                lines = {
                    type: 'line',
                    xref: 'paper',
                    yref: 'y',
                    x0: 0,
                    y0: threshold.thresholdValue,
                    x1: 1,
                    y1: threshold.thresholdValue,
                    line: {
                        color: 'rgb(0, 0, 255)',
                        width: 1
                    }
                };
            }

            layout.shapes.push(lines);

            var annotations={
                xref: 'paper',
                x: 1,
                y: threshold.thresholdValue,
                xanchor: 'left',
                yanchor: 'middle',
                text: threshold.thresholdName,
                showarrow: false,
                font: {
                    family: 'Segoe UI',
                    size: 14,
                    color: '#7f7f7f'
                }
            };
            layout.annotations.push(annotations);
        });
    }

    function getLayout() {


        if(tempStandard=='F') {
            layout = {
                xaxis: {
                    fixedrange: false,
                    title: 'Time (24hrs)',
                    titlefont: {
                        family: 'Segoe UI',
                        size: 12,
                        color: '#7f7f7f'
                    }
                },
                yaxis: {
                    title: 'F°',
                    titlefont: {
                        family: 'Segoe UI',
                        size: 16,
                        color: '#7f7f7f'
                    }
                },
                shapes: layout.shapes,
                annotations: layout.annotations,
                showlegend: true,
                legend: {
                    x: 1.2,
                    y: 1
                },
                margin: {r: 200}
            };

        }
        else{
            layout = {
                xaxis: {
                    fixedrange: false,
                    title: 'Time (24hrs)',
                    titlefont: {
                        family: 'Segoe UI',
                        size: 12,
                        color: '#7f7f7f'
                    }
                },
                yaxis: {
                    title: 'C°',
                    titlefont: {
                        family: 'Segoe UI',
                        size: 16,
                        color: '#7f7f7f'
                    }
                },
                shapes: layout.shapes,
                annotations: layout.annotations,
                showlegend: true,
                legend: {
                    x: 1.2,
                    y: 1
                },
                margin: {r: 200}
            };
        }

        return layout;
    }

    function getSitesOnGraph() {
        var keys = Object.keys(data);
        return keys;
    }

    function getTemperatureData() {


        if (tempStandard == 'F') {
            var keys = Object.keys(data);
            var temperatureData = keys.map(function (v) {
                return data[v];
            });
            return temperatureData;
        } else {
                var keys = Object.keys(dataC);
                var temperatureData = keys.map(function (v) {
                    return dataC[v];
                });
                return temperatureData;
        }
    }
    function addTemperatureData(siteId, collectedTemps,otherTemps) {
        if(tempStandard=='F'){
            data[siteId] = collectedTemps;
            dataC[siteId] = otherTemps;
        }
        else{
            dataC[siteId] = collectedTemps;
            data[siteId]= otherTemps;
        }
    }

    function removeTemperatureData(siteId) {
        delete data[siteId];
        delete dataC[siteId]
    }

    function removeAllTempData() {
        data = [];
        dataC= [];
    }

    return{
        init:init,
        addThresholdsToGraph:addThresholdsToGraph,
        removeThresholdFromGraph:removeThresholdFromGraph,
        getLayout:getLayout,
        getTemperatureData:getTemperatureData,
        addTemperatureData:addTemperatureData,
        removeTemperatureData:removeTemperatureData,
        getSitesOnGraph:getSitesOnGraph,
        removeAllTempData:removeAllTempData,
        updateThresholds:updateThresholds
    }
}();

microclimateComparisonNameSpace = function () {

    var sitesOnPlot = [];

    function addToSiteTable(label, id, table, sites, unitLabel) {
        var tr = document.createElement('tr');
        var td = document.createElement('td');
        var text = document.createTextNode(label);
        td.appendChild(text);
        tr.appendChild(td);
        for(var i = 0; i < sites.length; i++) {
            var td = document.createElement('td');
            var text = document.createTextNode(sites[i][2][id] + unitLabel);
            td.appendChild(text);
            tr.appendChild(td);
        }
        table.appendChild(tr);
    }

    function createComparisonToolTable() {
        var sites = sitesOnPlot;
        var comparisonToolDiv = $("#sitesOnPlot");

        // No sites in comparison tool. Add relevant message
        if (sites.length === 0) {
            comparisonToolDiv.html("<div><b>No sites added!</b> Add a site by selecting one through the map.</div>");
        } else {
            var table = document.createElement('table');
            table.setAttribute('class', 'comparisonTable');
            var tr = document.createElement('tr');
            var td = document.createElement('td');
            var text = document.createTextNode("Sites On Plot");
            td.appendChild(text);
            td.style.fontSize = "16px";
            tr.appendChild(td);
            for(var i = 0; i < sites.length; i++) {
                var td = document.createElement('td');
                var text = document.createTextNode(sites[i][1]);
                td.appendChild(text);
                tr.appendChild(td);
            }
            table.appendChild(tr);

            addToSiteTable("Area Around Sensor", "areaAroundSensor", table, sites, 'm');
            addToSiteTable("Canopy Type", "canopyType", table, sites, '');
            addToSiteTable("Distance To Water", "waterDistance", table, sites, 'm');
            addToSiteTable("Environment", "environment", table, sites, '');
            addToSiteTable("Height Above Floor", "heightAboveFloor", table, sites, 'm');
            addToSiteTable("Height Above Ground", "heightAboveGround", table, sites, 'm');
            addToSiteTable("Percent Enclosed", "enclosurePercentage", table, sites, '%');
            addToSiteTable("Nearest Airflow Obstacle", "nearestAirflowObstacle", table, sites, 'm');
            addToSiteTable("Nearest Obstacle Direction", "nearestObstacleDegrees", table, sites, '°');
            addToSiteTable("Riparian Area", "riparianArea", table, sites, '');
            addToSiteTable("Site Purpose", "purpose", table, sites, '');
            addToSiteTable("Sky View Factor", "skyViewFactor", table, sites, '%');
            addToSiteTable("Slope Of Site", "slope", table, sites, '%');

            comparisonToolDiv.html(table);
        }
    }

    function addSiteToComparisonTool(newSite) {
        sitesOnPlot.push(newSite);
        createComparisonToolTable();
    }

    function removeSiteFromComparisonTool(siteId) {
        sitesOnPlot = sitesOnPlot.filter(function (t) { return t[0] !== siteId; });
        createComparisonToolTable();
    }

    return{
        addSiteToComparisonTool:addSiteToComparisonTool,
        removeSiteFromComparisonTool:removeSiteFromComparisonTool
    }
}();

$(document).ready(function() {

    microclimateMapNameSpace.init();
    microclimateGraphNameSpace.init();

    // Remove map popup when sites added
    var removeMapPopup = function() {
        var result = $('#builder-basic').queryBuilder('getRules');
        if (!$.isEmptyObject(result)) {
            $("#map-popup").remove();
            $("#map-mask").remove();
        }
    };

    $("#btn-get").on( "click", removeMapPopup);
    document.getElementById('btn-NOAA-get').onclick=function(){
        microclimateMapNameSpace.populateNOAASites(1, 1, 1, document.getElementById("state-select").value);
    }
    document.getElementById('temperature-select').onchange=function(){
            updateTempStandard(document.getElementById('temperature-select').value);
    }

    // Expand/close side menu
    $("#sidemenu-popup-bar").on('click', function(event) {
        $("#filter-menu").toggleClass("collapsed");
        $("#sidemenu-popup").toggleClass("side-menu-flat side-menu-expand");
        $("#sidemenu-popup-bar").toggleClass("glyphicon-menu-right glyphicon-menu-left");
        $("#filter-menu").toggle();
        $("#content").toggleClass("content-flat content-expand");
        microclimateMapNameSpace.resizeMap();

        // Replot graph
        Plotly.newPlot('temperature-chart', microclimateGraphNameSpace.getTemperatureData(), microclimateGraphNameSpace.getLayout(),{responsive:true});

    });

    // Show tutorial on click
    $("#tutorial-link").on('click', function (event) {
        if ($("#filter-menu").hasClass("collapsed")) {
            $("#sidemenu-popup-bar").trigger("click");
        }
        // var test = $('#help-tab-link a[href="#help-tab"]');
        $('#help-tab-link').tab('show');
    });

    // Query builder
    var rules_basic = {
        condition: 'AND',
        rules: [{
            id: '-1'
        }]
    };
    $('#builder-basic').queryBuilder({
        allow_empty: true,
        lang: {
            add_rule: 'Add filter'
        },
        plugins: [
            'filter-description'
        ],
        filters: [{
            id: 'environment',
            label: 'Environment',
            type: 'string',
            input: 'select',
            values: {
                Natural: 'Natural',
                Urban: 'Urban'
            },
            color: 'primary',
            description: 'Natural or Urban Context?',
            operators: ['equal', 'not_equal']
        }, {
            id: 'purpose',
            label: 'Site Purpose',
            type: 'string',
            input: 'select',
            values: {
                'Commercial Offices': 'Commercial Offices',
                'Retail': 'Retail',
                'Restaurant': 'Restaurant',
                'Industrial': 'Industrial',
                'Construction Site': 'Construction Site',
                'School': 'School',
                'Single Family Residential': 'Single Family Residential',
                'Multi Family Residential': 'Multi Family Residential',
                'Park or Greenbelt': 'Park or Greenbelt',
                'Sports Facility': 'Sports Facility',
                'Recreational Pool': 'Recreational Pool',
                'Promenade or Plaza': 'Promenade or Plaza',
                'Bike or Walking Path': 'Bike or Walking Path',
                'Roadway or Parking Lot': 'Roadway or Parking Lot'
            },
            description: 'What is the primary purpose of this site (urban sites)?',
            operators: ['equal', 'not_equal']
        }, {
            id: 'heightAboveGround',
            label: 'Height Above Ground Surface',
            type: 'integer',
            validation: {
                min: 0,
                max: 10000,
                step: 1
            },
            operators: ['equal', 'not_equal', 'less', 'greater', 'between'],
            description: 'What is the height of the sensor above the ground surface (NOT the floor)?',
            data: "meters"
        }, {
            id: 'heightAboveFloor',
            label: 'Height Above Floor Surface',
            type: 'integer',
            validation: {
                min: 0,
                max: 10000,
                step: 1
            },
            operators: ['equal', 'not_equal', 'less', 'greater', 'between'],
            description: 'If the sensor is on a porch or in a building, what is the height of the sensor above the floor surface?',
            data: "meters"
        }, {
            id: 'enclosurePercentage',
            label: 'Percent Enclosed',
            type: 'integer',
            validation: {
                min: 0,
                max: 10000,
                step: 1
            },
            operators: ['equal', 'not_equal', 'less', 'greater', 'between'],
            description: 'What is the percentage this sites is enclosed? (ie Indoors/full-enclosed=100%)',
            data: "%"
        }, {
            id: 'nearestAirflowObstacle',
            label: 'Nearest Airflow Obstacle',
            type: 'integer',
            validation: {
                min: 0,
                max: 10000,
                step: 1
            },
            operators: ['equal', 'not_equal', 'less', 'greater', 'between'],
            description: 'If the sensor is outdoors, how far is the sensor from the nearest major airflow obstacle in meters (for example a wall, hedgerow, or building that is taller than the sensor’s height)?',
            data: "meters"
        }, {
            id: 'nearestObstacleDegrees',
            label: 'Nearest Obstacle Direction',
            type: 'integer',
            validation: {
                min: 0,
                max: 360,
                step: 1
            },
            operators: ['equal', 'not_equal', 'less', 'greater', 'between'],
            description: 'In what direction is the nearest obstacle located, from the sensor? (degrees east of north, 0 = north, 90 = east, 180 = south, 270 = west)',
            data: "°"
        }, {
            id: 'areaAroundSensor',
            label: 'Flat Area Around Sensor',
            type: 'integer',
            validation: {
                min: 0,
                max: 360,
                step: 1
            },
            operators: ['equal', 'not_equal', 'less', 'greater', 'between'],
            description: 'Roughly how large/wide is the area in which the sensor is located, before walls or\n' +
            'obstacles are encountered? (meters)',
            data: "°"
        }, {
            id: 'riparianArea',
            label: 'Located in Riparian Area',
            type: 'integer',
            input: 'radio',
            values: {
                true: 'Yes',
                false: 'No'
            },
            description: 'Is the sensor located in a depression or riparian area where water can collect?',
            operators: ['equal']
        }, {
            id: 'canopyType',
            label: 'Canopy Type Above Sensor',
            type: 'string',
            input: 'select',
            values: {
                'No Canopy': 'No Canopy',
                'Tree/Vegetation': 'Tree/Vegetation',
                'Shade Sail': 'Shade Sail',
                'Metal Roof': 'Metal Roof',
                'Pergola/Ramada': 'Pergola/Ramada',
                'Other Solid Roof': 'Other Solid Roof'
            },
            description: 'Canopy type above the sensor (near enough to be captured in an upward photo)',
            operators: ['equal', 'not_equal']
        }, {
            id: 'distanceToWater',
            label: 'Distance To Water',
            type: 'integer',
            validation: {
                min: 0,
                max: 10000,
                step: 1
            },
            operators: ['equal', 'not_equal', 'less', 'greater', 'between'],
            description: 'How far away is this water from the sensor?',
            data: "meters"
        }, {
            id: 'slope',
            label: 'Slope Of Site',
            type: 'integer',
            validation: {
                min: 0,
                max: 100,
                step: 1
            },
            operators: ['equal', 'not_equal', 'less', 'greater', 'between'],
            description: 'What is the slope % on this site, measured as the ratio of rise over run (for example one meter of rise in fifteen meters of run is 6.7%)',
            data: "%"
        }, {
            id: 'skyViewFactor',
            label: 'Sky View Factor',
            type: 'integer',
            validation: {
                min: 0,
                max: 100,
                step: 1
            },
            operators: ['equal', 'not_equal', 'less', 'greater', 'between'],
            description: 'If you stand in the location of the sensor and look straight up, roughly what percentage of the sky is visible (i.e. the Sky View Factor, for example 75%)',
            data: "%"
        }


        ],

        rules: rules_basic
    });
    $('#builder-basic').on('afterUpdateRuleFilter.queryBuilder', function(e, rule) {
        rule.$el.children("label").remove();
        if (rule.filter.data != null) {
            var label = $("<label>").text(rule.filter.data);
            label.addClass("unit-label");
            rule.$el.append(label);
        }
    });

    $('#btn-reset').on('click', function() {
        $('#builder-basic').queryBuilder('reset');
        microclimateMapNameSpace.clearMapOfMarkers();
        microclimateGraphNameSpace.removeAllTempData();
        Plotly.newPlot('temperature-chart', microclimateGraphNameSpace.getTemperatureData(), microclimateGraphNameSpace.getLayout(),{responsive:true});
    });

    $('#btn-get').on('click', function() {
        var result = $('#builder-basic').queryBuilder('getRules');

        if (!$.isEmptyObject(result)) {
            $('#btn-get').off("click", removeMapPopup);
            $.ajax({
                method: 'post',
                url: "/cocotemp/sitefilter.json",
                contentType: "application/json; charset=utf-8",
                async:false,
                data: JSON.stringify(result),
                success: function (z) {
                    // Notify user if there are no sites with given filters
                    if(z.length <= 0) {
                        toastr.options = {
                            "closeButton": true,
                            "positionClass": "toast-top-center"
                        };
                        toastr.info("No sites found with the given filter(s)");
                    }
                    microclimateMapNameSpace.populateMapWithSites(z);
                }
            });
        }
    });


    /*** Threshold actions start ***/

    var elt = $('#thresholdTags');
    elt.tagsinput({
        itemValue: 'value',
        itemText: 'text'
    });
    elt.tagsinput('add', { "value": "0" , "text": "Freezing"   , "color": "#7f7f7f"    });
    elt.tagsinput('add', { "value": "35" , "text": "NWS Heat Caution"  , "color": "#7f7f7f"   });
    elt.tagsinput('add', { "value": "56" , "text": "NWS Heat Danger"      , "color": "#7f7f7f" });

    // When new threshold value added, replot graph with new threshold
    $("#thresholdTags").on('itemAdded', function(event) {
        microclimateGraphNameSpace.addThresholdsToGraph({thresholdValue:event.item.value, thresholdName:event.item.text});
        Plotly.newPlot('temperature-chart', microclimateGraphNameSpace.getTemperatureData(), microclimateGraphNameSpace.getLayout(),{responsive:true});
    });
    // When threshold value removed, replot graph without threshold
    $("#thresholdTags").on('itemRemoved', function(event) {
        microclimateGraphNameSpace.removeThresholdFromGraph(event.item.value);
        Plotly.newPlot('temperature-chart', microclimateGraphNameSpace.getTemperatureData(), microclimateGraphNameSpace.getLayout(),{responsive:true});
    });
    $("#addThresholdButton").on('click', function (event) {
        var thresholdTemp = $("#thresholdInput").val();
        // var thresholdColor = $("#thresholdColor").val();
        var thresholdLabel = $("#thresholdLabel").val();
        if (thresholdTemp === '' || thresholdLabel === '') {
            return;
        }
        $("#thresholdTags").tagsinput('add', {value: thresholdTemp, text : thresholdLabel});
        $("#thresholdInput").val('');
        $("#thresholdLabel").val('');
    });
    /*** Threshold actions end ***/


});


function updateTempStandard(temp) {

    if(temp=='F'){

        tempStandard='F';
        microclimateGraphNameSpace.updateThresholds();
        Plotly.newPlot('temperature-chart', microclimateGraphNameSpace.getTemperatureData(), microclimateGraphNameSpace.getLayout(),{responsive:true});
    }
    else{
        tempStandard='C';
        microclimateGraphNameSpace.updateThresholds();
        Plotly.newPlot('temperature-chart', microclimateGraphNameSpace.getTemperatureData(), microclimateGraphNameSpace.getLayout(),{responsive:true});
    }
}

/*
 * Action upon click of map marker. Adds and removes data/line to line chart.
 */
function markerClick(marker, popupText) {

    // Marker is already on map. Remove it from chart and map.
    if (marker.options.options.onChart === true) {

        // Set to default icon and mark as not on map
        microclimateMapNameSpace.makeMarkerAvailable(marker.options.options.color);
        marker.setIcon(new L.Icon.Default());
        marker.options.options.onChart = false;
        microclimateMapNameSpace.removeMarkerFromMainMap(marker);

        // Remove data from hash
        microclimateGraphNameSpace.removeTemperatureData(marker.options.options.siteID);

        microclimateComparisonNameSpace.removeSiteFromComparisonTool(marker.options.options.siteID);

        // Replot graph
        Plotly.newPlot('temperature-chart', microclimateGraphNameSpace.getTemperatureData(), microclimateGraphNameSpace.getLayout(),{responsive:true});


        // Change text on popup
        popupText.text = "Add to Graph";

        return;
    }
    $.ajax({
        method: 'get',
        url: "/cocotemp/site/" + marker.options.options.siteID + "/temperature.json",
        success: function (z) {
            var dates = [], temperature = [], tempF=[];
            z.forEach(function (datum) {
                dates.push(new Date(datum['dateTime']));
                temperature.push(datum['temperature'].toFixed(1));
                tempF.push(parseFloat((datum['temperature'] * (9 / 5) + 32).toFixed(1)));
            });

            // Get available icon color/url. Return if none available
            var iconMarker = microclimateMapNameSpace.getAvailableMarker();
            if (iconMarker === null) {
                // Warn user that you can't add more sites to graph
                toastr.options = {
                    "closeButton": true,
                    "positionClass": "toast-top-center"
                };
                toastr.warning("Sorry, you cannot add more than 5 sites");
                return;
            }
            marker.options.options.color = iconMarker[0];

            var collectedTemps=null;
            var otherTemps=null
            if(tempStandard=='C'){
                collectedTemps = {
                    x: dates,
                    y: temperature,
                    name: marker.options.options.siteName,
                    type: 'scatter',
                    marker: {
                        color: iconMarker[0]
                    },
                    hoverinfo: "y+x"
                };
                otherTemps = {
                    x: dates,
                    y: tempF,
                    name: marker.options.options.siteName,
                    type: 'scatter',
                    marker: {
                        color: iconMarker[0]
                    },
                    hoverinfo: "y+x"
                };
            }
            else{
                collectedTemps = {
                    x: dates,
                    y: tempF,
                    name: marker.options.options.siteName,
                    type: 'scatter',
                    marker: {
                        color: iconMarker[0]
                    },
                    hoverinfo: "y+x"
                };
                otherTemps = {
                    x: dates,
                    y: temperature,
                    name: marker.options.options.siteName,
                    type: 'scatter',
                    marker: {
                        color: iconMarker[0]
                    },
                    hoverinfo: "y+x"
                };
            }


            // Add temperature for site to saved hash
            microclimateGraphNameSpace.addTemperatureData(marker.options.options.siteID, collectedTemps,otherTemps);

            microclimateComparisonNameSpace.addSiteToComparisonTool([marker.options.options.siteID, marker.options.options.siteName, marker.options.options.metadata]);

            // Replot graph
            Plotly.newPlot('temperature-chart', microclimateGraphNameSpace.getTemperatureData(), microclimateGraphNameSpace.getLayout(),{responsive:true});

            // Create custom icon with its own color
            var customIcon = new L.Icon({
                iconUrl: iconMarker[1],
                shadowUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/0.7.7/images/marker-shadow.png',
                iconSize: [25, 41],
                iconAnchor: [12, 41],
                popupAnchor: [1, -34],
                shadowSize: [41, 41]
            });
            marker.options.options.onChart = true;
            iconMarker[2] = 1;
            marker.setIcon(customIcon);
            microclimateMapNameSpace.addMarkerToMainMap(marker);


            // Change text on popup
            popupText.text = "Remove from Graph";
        }
    });
}
