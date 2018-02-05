$(document).ready(function() {

    // Create map of sites
    var myMap = createMap();
    $('#basemaps').on('change', function() {
        changeBasemap(myMap, this.value);
    });
    populateMapWithSites(myMap);

    // Create site temperature line chart
    createChart();

    $("#thresholdTags").on('itemAdded', function(event) {
        thresholds.push({thresholdValue:event.item, thresholdName:"Custom"});
        addThresholdsToGraph();
        var chart = document.getElementById('temperature-chart');
        Plotly.newPlot(chart, data, layout);
    });

    $("#thresholdTags").on('itemRemoved', function(event) {
        thresholds = removeFromArray(thresholds, event.item);
        addThresholdsToGraph();
        var chart = document.getElementById('temperature-chart');
        Plotly.newPlot(chart, data, layout);
    });
});

function expandFilterSideMenu () {
    $("#filter-menu").toggleClass("collapsed");
    $("#sidemenu-popup").toggleClass("side-menu-flat side-menu-expand");
    $("#sidemenu-popup-bar").toggleClass("glyphicon-menu-right glyphicon-menu-left");
    //$("#content").toggleClass("content-flat content-expand");
};

function removeFromArray(array, element) {
    return array.filter(function (t) { return t.thresholdValue !== element; });
}

function addThreshold () {
    var thresholdValue = $("#thresholdInput").val();
    $("#thresholdTags").tagsinput('add', thresholdValue);
}

function addThresholdsToGraph () {
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

var data = [];
var thresholds = [{thresholdValue:"0", thresholdName:"Freezing"}, {thresholdValue:"35", thresholdName:"Caution"}, {thresholdValue:"56", thresholdName:"Danger"}];
var layout;
function createChart() {
    var d3 = Plotly.d3;

    var gd3 = d3.select('div[id=\'temperature-chart\']').append('div')
        .style({
            width: '100%',
            height: '100%'
        });

    var gd = gd3.node();

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

    addThresholdsToGraph();

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

/*
 * Add all site pins to map
 * @param {Leaflet Map} myMap
 */
function populateMapWithSites(myMap) {
    var siteMarkers = [];
    siteMarker = L.Marker.extend({
        options: {
            siteID: 'Site ID',
            siteName: 'Site Name',
            onChart: false,
            dataPos: -1
        }
    });

    $.ajax({
        method: 'post',
        url: '/cocotemp/sites.json',
        success: function (data) {
            if (data.length === 0) {
                return;
            }

            for (var i = 0; i < data.length; i++) {
                //Add the station locations to the map.
                var point = L.latLng([data[i].siteLatitude, data[i].siteLongitude]);
                var myMarker = new siteMarker(point,{options: {siteID: data[i].id, siteName: data[i].siteName, onChart: false} });
                myMarker.addTo(myMap).on('click',markerClick);
                siteMarkers.push(myMarker);
            }
        }
    });
}

/*
 * Action upon click of map marker. Adds and removes data/line to line chart.
 */
var colors = ['black', 'green', 'red', 'violet', 'yellow'];
function markerClick(e) {
    if (e.target.options.options.onChart === true) {
        e.target.setIcon(new L.Icon.Default() );
        e.target.options.options.onChart = false;
        data[e.target.options.options.dataPos] = [];
        var chart = document.getElementById('temperature-chart');
        Plotly.newPlot(chart, data, layout);
        e.target.options.options.dataPos = -1;
        return;
    }
    $.ajax({
        method: 'get',
        url: "/cocotemp/site/" + e.target.options.options.siteID + "/temperature.json",
        success: function (z) {
            var dates = [], temperature = [];
            z.forEach(function (datum) {
                dates.push(new Date(datum['dateTime']));
                temperature.push(datum['temperature']);
            });
            var collectedTemps = {
                x: dates,
                y: temperature,
                name: e.target.options.options.siteName,
                type: 'scatter',
                marker: {
                    color: colors[data.length]
                }
            };
            data.push(collectedTemps);
            var chart = document.getElementById('temperature-chart');
            Plotly.newPlot(chart, data, layout);

            var greenIcon = new L.Icon({
                iconUrl: "/cocotemp/images/marker-icon-2x-" + colors[(data.length - 1) % 5] + ".png",
                shadowUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/0.7.7/images/marker-shadow.png',
                iconSize: [25, 41],
                iconAnchor: [12, 41],
                popupAnchor: [1, -34],
                shadowSize: [41, 41]
            });
            e.target.options.options.dataPos = data.length - 1;
            e.target.options.options.onChart = true;
            e.target.setIcon(greenIcon);
        }
    });
}