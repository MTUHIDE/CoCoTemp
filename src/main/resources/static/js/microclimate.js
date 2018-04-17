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
        removeMarkerFromMainMap:removeMarkerFromMainMap
    }
}();

microclimateGraphNameSpace = function(){
    var layout = null;
    var data = [];
    var thresholds = [
        {thresholdValue:"0", thresholdName:"Freezing"},
        {thresholdValue:"35", thresholdName:"Caution"},
        {thresholdValue:"56", thresholdName:"Danger"}
    ];

    // Create site temperature line graph
    function init() {
        createGraph();
    }

    function createGraph() {

        // Get temperature chart element and initialize plotly
        var d3 = Plotly.d3;
        var gd3 = d3.select('div[id=\'temperature-chart\']').append('div')
            .style({
                width: '100%',
                height: '100%'
            });
        var gd = gd3.node();

        // Graph layout
        layout = {
            xaxis: {
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
                x: 1.1,
                y: 1 },
            margin: {r: 100}
        };

        updateThresholds();

        var dates = [], temperature = [];
        var collectedTemps = {
            x: dates,
            y: temperature,
            name: 'site\'s temperature',
            type: 'scatter'
        };
        var data = [collectedTemps];

        Plotly.plot(gd, data, layout);

        window.onresize = function() {
            Plotly.Plots.resize(gd);
        };
    }


    function removeThresholdFromGraph(element) {
        thresholds = thresholds.filter(function (t) { return t.thresholdValue !== element; });
        updateThresholds();
    }

    function addThresholdsToGraph(threshold) {
        thresholds.push(threshold);
        updateThresholds();
    }

    function updateThresholds() {
        layout.shapes = [];
        layout.annotations = [];
        thresholds.forEach(function(threshold) {
            var lines = {
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
            layout.shapes.push(lines);

            var annotations = {
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
        return layout;
    }

    function getSitesOnGraph() {
        var keys = Object.keys(data);
        return keys;
    }

    function getTemperatureData() {
        var keys = Object.keys(data);
        var temperatureData = keys.map(function(v) { return data[v]; });
        return temperatureData;
    }

    function addTemperatureData(siteId, collectedTemps) {
        data[siteId] = collectedTemps;
    }

    function removeTemperatureData(siteId) {
        delete data[siteId];
    }

    function removeAllTempData() {
        data = [];
    }

    return{
        init:init,
        addThresholdsToGraph:addThresholdsToGraph,
        removeThresholdFromGraph:removeThresholdFromGraph,
        getLayout:getLayout,
        getTemperatureData:getTemperatureData,
        addTemperatureData:addTemperatureData,
        removeTemperatureData:removeTemperatureData,
        removeAllTempData:removeAllTempData
    }
}();

microclimateComparisonNameSpace = function () {

    var sitesOnPlot = [];

    function addToSiteTable(label, id, table, sites) {
        var tr = document.createElement('tr');
        var td = document.createElement('td');
        var text = document.createTextNode(label);
        td.appendChild(text);
        tr.appendChild(td);
        for(var i = 0; i < sites.length; i++) {
            var td = document.createElement('td');
            var text = document.createTextNode(sites[i][2][id]);
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

            addToSiteTable("Area Around Sensor", "areaAroundSensor", table, sites);
            // addToSiteTable("Canopy Type", "canopyType", table, sites);
            addToSiteTable("Distance To Water", "distanceToWater", table, sites);
            addToSiteTable("Environment", "environment", table, sites);
            addToSiteTable("Height Above Floor", "heightAboveFloor", table, sites);
            addToSiteTable("Height Above Ground", "heightAboveGround", table, sites);
            addToSiteTable("Percent Enclosed", "enclosurePercentage", table, sites);
            addToSiteTable("Nearest Airflow Obstacle", "nearestAirflowObstacle", table, sites);
            addToSiteTable("Nearest Obstacle In Degrees", "nearestObstacleDegrees", table, sites);
            addToSiteTable("Riparian Area", "riparianArea", table, sites);
            // addToSiteTable("Site Purpose", "purpose", table, sites);
            addToSiteTable("Sky View Factor", "skyViewFactor", table, sites);
            addToSiteTable("Slope Of Site", "slope", table, sites);

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

    // Expand/close side menu
    $("#sidemenu-popup-bar").on('click', function(event) {
        $("#filter-menu").toggleClass("collapsed");
        $("#sidemenu-popup").toggleClass("side-menu-flat side-menu-expand");
        $("#sidemenu-popup-bar").toggleClass("glyphicon-menu-right glyphicon-menu-left");
        $("#filter-menu").toggle();
        $("#content").toggleClass("content-flat content-expand");
        microclimateMapNameSpace.resizeMap();

        // Replot graph
        var chart = document.getElementById('temperature-chart');
        Plotly.newPlot(chart, microclimateGraphNameSpace.getTemperatureData(), microclimateGraphNameSpace.getLayout());
        window.onresize = function() {
            Plotly.Plots.resize(gd);
        };
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
                Retail: 'Retail',
                Restaurant: 'Restaurant',
                Industrial: 'Industrial',
                Construction_Site: 'Construction Site',
                School: 'School',
                Single_Family_Residential: 'Single Family Residential',
                Multi_Family_Residential: 'Multi Family Residential',
                Park_or_Greenbelt: 'Park or Greenbelt',
                Sports_Facility: 'Sports Facility',
                Recreational_Pool: 'Recreational Pool',
                Promenade_or_Plaza: 'Promenade or Plaza',
                Bike_or_Walking_Path: 'Bike or Walking Path',
                Roadway_or_Parking_Lot: 'Roadway or Parking Lot'
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
            label: 'Nearest Obstacle In Degrees',
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
                No_Canopy: 'No Canopy',
                Tree_Vegetation: 'Tree/Vegetation',
                Shade_Sail: 'Shade Sail',
                Metal_Roof: 'Metal Roof',
                'Pergola/Ramada': 'Pergola/Ramada',
                Other_Solid_Roof: 'Other Solid Roof'
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
            description: 'How far away is this water from the sensor?'
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
        // microclimateMapNameSpace.clearMapOfMarkers();
        // microclimateGraphNameSpace.removeAllTempData();
        // var chart = document.getElementById('temperature-chart');
        // Plotly.newPlot(chart, microclimateGraphNameSpace.getTemperatureData(), microclimateGraphNameSpace.getLayout());
    });

    $('#btn-get').on('click', function() {
        var result = $('#builder-basic').queryBuilder('getRules');

        if (!$.isEmptyObject(result)) {
            $('#btn-get').off("click", removeMapPopup);
            $.ajax({
                method: 'post',
                url: "/cocotemp/sitefilter.json",
                contentType: "application/json; charset=utf-8",
                data: JSON.stringify(result),
                success: function (z) {
                    microclimateMapNameSpace.populateMapWithSites(z);
                }
            });
        }
    });


    /*** Threshold actions start ***/
    var chart = document.getElementById('temperature-chart');

    var elt = $('#thresholdTags');
    elt.tagsinput({
        itemValue: 'value',
        itemText: 'text'
    });
    elt.tagsinput('add', { "value": "0" , "text": "Freezing"   , "color": "#7f7f7f"    });
    elt.tagsinput('add', { "value": "35" , "text": "Caution"  , "color": "#7f7f7f"   });
    elt.tagsinput('add', { "value": "56" , "text": "Danger"      , "color": "#7f7f7f" });

    // When new threshold value added, replot graph with new threshold
    $("#thresholdTags").on('itemAdded', function(event) {
        microclimateGraphNameSpace.addThresholdsToGraph({thresholdValue:event.item.value, thresholdName:event.item.text});
        Plotly.newPlot(chart, microclimateGraphNameSpace.getTemperatureData(), microclimateGraphNameSpace.getLayout());
        window.onresize = function() {
            Plotly.Plots.resize(gd);
        };
    });
    // When threshold value removed, replot graph without threshold
    $("#thresholdTags").on('itemRemoved', function(event) {
        microclimateGraphNameSpace.removeThresholdFromGraph(event.item.value);
        Plotly.newPlot(chart, microclimateGraphNameSpace.getTemperatureData(), microclimateGraphNameSpace.getLayout());
        window.onresize = function() {
            Plotly.Plots.resize(gd);
        };
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


/*
 * Action upon click of map marker. Adds and removes data/line to line chart.
 */
function markerClick(marker, popupText) {

    // Marker is already on map. Remove it from chart and map.
    if (marker.options.options.onChart === true) {

        // Set to default icon and mark as not on map
        microclimateMapNameSpace.makeMarkerAvailable(marker.options.options.color);
        marker.setIcon(new L.Icon.Default() );
        marker.options.options.onChart = false;
        microclimateMapNameSpace.removeMarkerFromMainMap(marker);

        // Remove data from hash
        microclimateGraphNameSpace.removeTemperatureData(marker.options.options.siteID);

        microclimateComparisonNameSpace.removeSiteFromComparisonTool(marker.options.options.siteID);

        // Replot graph
        var chart = document.getElementById('temperature-chart');
        Plotly.newPlot(chart, microclimateGraphNameSpace.getTemperatureData(), microclimateGraphNameSpace.getLayout());
        window.onresize = function() {
            Plotly.Plots.resize(chart);
        };

        // Change text on popup
        popupText.text = "Add to Graph";

        return;
    }
    $.ajax({
        method: 'get',
        url: "/cocotemp/site/" + marker.options.options.siteID + "/temperature.json",
        success: function (z) {
            var dates = [], temperature = [];
            z.forEach(function (datum) {
                dates.push(new Date(datum['dateTime']));
                temperature.push(datum['temperature']);
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

            var collectedTemps = {
                x: dates,
                y: temperature,
                name: marker.options.options.siteName,
                type: 'scatter',
                marker: {
                    color: iconMarker[0]
                }
            };

            // Add temperature for site to saved hash
            microclimateGraphNameSpace.addTemperatureData(marker.options.options.siteID, collectedTemps);

            microclimateComparisonNameSpace.addSiteToComparisonTool([marker.options.options.siteID, marker.options.options.siteName, marker.options.options.metadata]);

            // Replot graph
            var chart = document.getElementById('temperature-chart');
            Plotly.newPlot(chart, microclimateGraphNameSpace.getTemperatureData(), microclimateGraphNameSpace.getLayout());
            window.onresize = function() {
                Plotly.Plots.resize(chart);
            };

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
    $.ajax({
        method: 'get',
        url: "/cocotemp/site/" + marker.options.options.siteID + "/temperature.json",
        success: function (z) {

        }
    });
}