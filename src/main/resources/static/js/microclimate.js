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
        populateMapWithAllSites(myMap);
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
     * Add all site pins to map
     * @param {Leaflet Map} myMap
     */
    function populateMapWithAllSites() {

        markersGroup = L.markerClusterGroup();

        siteMarker = L.Marker.extend({
            options: {
                siteID: 'Site ID',
                siteName: 'Site Name',
                onChart: false,
                color: null,
                metadata: null
            }
        });

        $.ajax({
            method: 'post',
            url: '/cocotemp/sites.json',
            success: function (ajaxData) {
                if (ajaxData.length === 0) {
                    return;
                }

                for (var i = 0; i < ajaxData.length; i++) {
                    //Add the station locations to the map.
                    var point = L.latLng([ajaxData[i].siteLatitude, ajaxData[i].siteLongitude]);
                    var myMarker = new siteMarker(point,{options: {siteID: ajaxData[i].id, siteName: ajaxData[i].siteName, onChart: false} });
                    markersGroup.addLayer(myMarker).on('click',markerClick);
                    siteMarkers.push(myMarker);
                }
                markersGroup.addTo(myMap);

                $.ajax({
                    method: 'get',
                    url: '/cocotemp/sitemetadata.json',
                    success: function (data) {
                        if (data.length === 0) {
                            return;
                        }

                        for (var j = 0; j < data.length; j++) {

                            for (var i = 0; i < siteMarkers.length; i++) {
                                if (siteMarkers[i].options.options.siteID === data[j].siteID) {
                                    var dict = {canopyType: data[j].canopyType, environment: data[j].environment};
                                    siteMarkers[i].options.options.metadata = dict;
                                    break;
                                }
                            }
                        }
                    }
                });
            }
        });
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
            var point = L.latLng([site.siteLatitude, site.siteLongitude]);
            var myMarker = new siteMarker(point,{options: {siteID: site.id, siteName: site.siteName, onChart: false} });
            markersGroup.addLayer(myMarker).on('click',markerClick);
            siteMarkers.push(myMarker);
        }
        markersGroup.addTo(myMap);
    }

    function clearMapOfMarkers() {
        markersGroup.clearLayers();
        siteMarkers = [];
    }

    // function applyMapFilter() {
    //     var filters = {};
    //     // Filter by environment
    //     var environment = $("#environmentTags").tagsinput('items');
    //     if (environment.length !== 0) {
    //         filters["environment"] = environment;
    //     }
    //
    //     var canopyType = $("#canopy-type :checked").map(function(){
    //         return $(this).val();
    //     }).get();
    //     if (canopyType.length !== 0) {
    //         filters["canopyType"] = canopyType;
    //     }
    //
    //     var keys = Object.keys(filters);
    //
    //     // If no filters applied, add all markers
    //     if(keys.length === 0) {
    //         for(var i = 0; i < siteMarkers.length; i++) {
    //                 markersGroup.addLayer(siteMarkers[i]);
    //         }
    //     }
    //     // Else filter by added filters
    //     else {
    //         siteMarkersLoop: for(var i = 0; i < siteMarkers.length; i++) {
    //             for(var j = 0; j < keys.length; j++) {
    //                 if (!filters[keys[j]].includes(siteMarkers[i].options.options.metadata[keys[j]])) {
    //                       continue siteMarkersLoop;
    //                 }
    //             }
    //             markersGroup.addLayer(siteMarkers[i]);
    //         }
    //     }
    //
    //     // filters.push(filter);
    //     // for(var i = 0; i < siteMarkers.length; i++) {
    //     //     for(var j = 0; j < filters.length; j++) {
    //     //         if (siteMarkers[i].options.options.metadata[filters[j][0]] === filters[j][1]) {
    //     //
    //     //         }
    //     //     }
    //     // }
    //     //
    //     // for(var i = 0; i < array.length; i++) {
    //     //     markersGroup.addLayer(array[i]);
    //     // }
    // }

    return{
        init:init,
        getAvailableMarker:getAvailableMarker,
        makeMarkerAvailable:makeMarkerAvailable,
        populateMapWithSites:populateMapWithSites,
        clearMapOfMarkers:clearMapOfMarkers
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

    function getTemperatureData() {
        var keys = Object.keys(data);
        var temperatureData = keys.map(function(v) { return data[v]; });
        return temperatureData;
    }

    function addTemperatureData(siteId ,collectedTemps) {
        data[siteId] = collectedTemps;
    }

    function removeTemperatureData(siteId) {
        delete data[siteId];
    }

    return{
        init:init,
        addThresholdsToGraph:addThresholdsToGraph,
        removeThresholdFromGraph:removeThresholdFromGraph,
        getLayout:getLayout,
        getTemperatureData:getTemperatureData,
        addTemperatureData:addTemperatureData,
        removeTemperatureData:removeTemperatureData
    }
}();

$(document).ready(function() {

    microclimateMapNameSpace.init();
    microclimateGraphNameSpace.init();

    // Expand/close side menu
    $("#sidemenu-popup-bar").on('click', function(event) {
        $("#filter-menu").toggleClass("collapsed");
        $("#sidemenu-popup").toggleClass("side-menu-flat side-menu-expand");
        $("#sidemenu-popup-bar").toggleClass("glyphicon-menu-right glyphicon-menu-left");
        //$("#content").toggleClass("content-flat content-expand");
    });


    // Query builder
    var rules_basic = {
        condition: 'AND',
        rules: [{
            id: 'environment',
            operator: 'less',
            value: "Natural"
        }]
    };

    $('#builder-basic').queryBuilder({
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
            description: 'What is the height of the sensor above the ground surface (NOT the floor)?'
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
            description: 'If the sensor is on a porch or in a building, what is the height of the sensor above the floor surface?'
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
            description: 'What is the percentage this sites is enclosed? (ie Indoors/full-enclosed=100%)'
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
            description: 'If the sensor is outdoors, how far is the sensor from the nearest major airflow obstacle in meters (for example a wall, hedgerow, or building that is taller than the sensor’s height)?'
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
            description: 'In what direction is the nearest obstacle located, from the sensor? (degrees east of north, 0 = north, 90 = east, 180 = south, 270 = west)'
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
            'obstacles are encountered? (meters)'
        }, {
            id: 'riparianArea',
            label: 'Located in Riparian Area',
            type: 'integer',
            input: 'radio',
            values: {
                1: 'Yes',
                0: 'No'
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
            description: 'What is the slope % on this site, measured as the ratio of rise over run (for example one meter of rise in fifteen meters of run is 6.7%)'
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
            description: 'If you stand in the location of the sensor and look straight up, roughly what percentage of the sky is visible (i.e. the Sky View Factor, for example 75%)'
        }


        ],

        rules: rules_basic
    });

    $('#btn-reset').on('click', function() {
        $('#builder-basic').queryBuilder('reset');
        microclimateMapNameSpace.clearMapOfMarkers();
    });

    $('#btn-get').on('click', function() {
        var result = $('#builder-basic').queryBuilder('getRules');

        if (!$.isEmptyObject(result)) {
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


    // // Filter when environment type added
    // $("#environmentFilterBtn").on('click', function(event) {
    //     var environmentType = $("#environmentInput").val();
    //     $("#environmentTags").tagsinput('add', environmentType);
    //     microclimateMapNameSpace.clearMapOfMarkers();
    //     microclimateMapNameSpace.applyMapFilter();
    // });
    // $("#environmentTags").on('itemRemoved', function(event) {
    //     microclimateMapNameSpace.clearMapOfMarkers();
    //     microclimateMapNameSpace.applyMapFilter();
    // });

    /*** Threshold actions start ***/
    var chart = document.getElementById('temperature-chart');
    // When new threshold value added, replot graph with new threshold
    $("#thresholdTags").on('itemAdded', function(event) {
        microclimateGraphNameSpace.addThresholdsToGraph({thresholdValue:event.item, thresholdName:"Custom"});
        Plotly.newPlot(chart, microclimateGraphNameSpace.getTemperatureData(), microclimateGraphNameSpace.getLayout());
    });
    // When threshold value removed, replot graph without threshold
    $("#thresholdTags").on('itemRemoved', function(event) {
        microclimateGraphNameSpace.removeThresholdFromGraph(event.item);
        Plotly.newPlot(chart, microclimateGraphNameSpace.getTemperatureData(), microclimateGraphNameSpace.getLayout());
    });
    $("#addThresholdButton").on('click', function (event) {
        var thresholdValue = $("#thresholdInput").val();
        $("#thresholdTags").tagsinput('add', thresholdValue);
    });
    /*** Threshold actions end ***/



    // Enclosure percentage slider
    // var percentEnclosedHandle1 = $( "#percent-enclosed-handle-min" );
    // var percentEnclosedHandle2 = $( "#percent-enclosed-handle-max" );
    // $( "#percent-enclosed-range" ).slider({
    //     range: true,
    //     min: 0,
    //     max: 100,
    //     values: [ 0, 100 ],
    //     create: function() {
    //         percentEnclosedHandle1.text( $( this ).slider( "values", 0 ) + "%" );
    //         percentEnclosedHandle2.text( $( this ).slider( "values", 1 ) + "%" );
    //     },
    //     slide: function( event, ui ) {
    //         percentEnclosedHandle1.text( ui.values[0] + "%" );
    //         percentEnclosedHandle2.text( ui.values[1] + "%" );
    //     }
    // });
    //
    // // Area Around Sensor Slider
    // var areaAroundHandle1 = $( "#area-around-handle-min" );
    // var areaAroundHandle2 = $( "#area-around-handle-max" );
    // $( "#area-around-sensor-range" ).slider({
    //     range: true,
    //     min: 0,
    //     max: 1000,
    //     values: [ 0, 1000 ],
    //     create: function() {
    //         areaAroundHandle1.text( $( this ).slider( "values", 0 ) + "m" );
    //         areaAroundHandle2.text( $( this ).slider( "values", 1 ) + "m" );
    //     },
    //     slide: function( event, ui ) {
    //         areaAroundHandle1.text( ui.values[0] + "m" );
    //         areaAroundHandle2.text( ui.values[1] + "m" );
    //     }
    // });


});


/*
 * Action upon click of map marker. Adds and removes data/line to line chart.
 */
function markerClick(e) {

    // Marker is already on map. Remove it from chart and map.
    if (e.layer.options.options.onChart === true) {

        // Set to default icon and mark as not on map
        microclimateMapNameSpace.makeMarkerAvailable(e.layer.options.options.color);
        e.layer.setIcon(new L.Icon.Default() );
        e.layer.options.options.onChart = false;

        // Remove data from hash
        microclimateGraphNameSpace.removeTemperatureData(e.layer.options.options.siteID);

        // Replot graph
        var chart = document.getElementById('temperature-chart');
        Plotly.newPlot(chart, microclimateGraphNameSpace.getTemperatureData(), microclimateGraphNameSpace.getLayout());

        return;
    }
    $.ajax({
        method: 'get',
        url: "/cocotemp/site/" + e.layer.options.options.siteID + "/temperature.json",
        success: function (z) {
            var dates = [], temperature = [];
            z.forEach(function (datum) {
                dates.push(new Date(datum['dateTime']));
                temperature.push(datum['temperature']);
            });

            // Get available icon color/url. Return if none available
            var iconMarker = microclimateMapNameSpace.getAvailableMarker();
            if (iconMarker === null) {
                return;
            }
            e.layer.options.options.color = iconMarker[0];

            var collectedTemps = {
                x: dates,
                y: temperature,
                name: e.layer.options.options.siteName,
                type: 'scatter',
                marker: {
                    color: iconMarker[0]
                }
            };

            // Add temperature for site to saved hash
            microclimateGraphNameSpace.addTemperatureData(e.layer.options.options.siteID, collectedTemps);

            var chart = document.getElementById('temperature-chart');
            Plotly.newPlot(chart, microclimateGraphNameSpace.getTemperatureData(), microclimateGraphNameSpace.getLayout());

            // Create custom icon with its own color
            var customIcon = new L.Icon({
                iconUrl: iconMarker[1],
                shadowUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/0.7.7/images/marker-shadow.png',
                iconSize: [25, 41],
                iconAnchor: [12, 41],
                popupAnchor: [1, -34],
                shadowSize: [41, 41]
            });
            e.layer.options.options.onChart = true;
            iconMarker[2] = 1;
            e.layer.setIcon(customIcon);
        }
    });
}