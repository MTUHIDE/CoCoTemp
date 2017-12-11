$(document).ready(function() {

    // Search functionality
    $('#searchButton').click(function() {
        var locQuery;
        var latQuery = $('#latitude-query').val();
        var lonQuery = $('#longitude-query').val();
        if (latQuery === null || latQuery === '' || lonQuery === null || lonQuery === '') {
            locQuery = '';
        } else {
            locQuery = latQuery + "," + lonQuery;
        }
        populateSearchResults($('#search-query').val(), locQuery, $('#range-selector').val());
    });

    // Create map
    var myMap = createMap();
    $('#basemaps').on('change', function() {
        changeBasemap(myMap, this.value);
    });
    populateMapWithSites(myMap);

    //Add autocomplete to location search
    var $location = document.getElementById('location-query');
    var autocomplete = new google.maps.places.Autocomplete($location);
    autocomplete.addListener("place_changed", function () {
        var location = autocomplete.getPlace().geometry.location;
        $("#latitude-query").val(location.lat());
        $("#longitude-query").val(location.lng());
    });

    createChart();
});

function myFunction () {
    $("#sidebar").toggleClass("collapsed");
    $("#content").toggleClass("col-md-12 col-md-9");
};

function populateSearchResults(query, location, range) {
    var urlString = "/cocotemp/search.json?type=site"
    if(query !== null && query !== '') {
        urlString = urlString + "&query=" + query;
    }
    if(location !== null && location !== '') {
        urlString = urlString + "&location=" + location;
    }
    if(range !== null && range !== '') {
        urlString = urlString + "&range=" + range;
    }
    var table = $("#search-results");
    table.find("tr:gt(0)").remove();
    $.ajax({
        method: 'post',
        url: urlString,
        success: function (data) {
            if (data.length === 0) {
                return;
            }
            for (var i = 0; i < data.length; i++) {
                var row = $("<tr>")
                    .append($("<td>").text(data[i].siteName))
                    .append($("<td>").text(data[i].siteDescription));
                table.append(row);
            }
        }
    });



    // function populateChart() {
    //
    //     $.ajax({
    //         method: 'get',
    //         url: "/cocotemp/site/" + siteID + "/temperature.json",
    //         success: function (data) {
    //             data.forEach(function (datum) {
    //                 dates.push(new Date(datum['dateTime']));
    //                 temperature.push(datum['temperature']);
    //             });
    //         }
    //     });
    //
    //     var collectedTemps = {
    //         x: dates,
    //         y: temperature,
    //         name: 'site\'s temperature',
    //         type: 'scatter'
    //     };
    //     var data = [collectedTemps];
    //
    //
    // }
}

var data = [];
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

    var indexTemp = [0, 31, 35, 56];
    var indexColors = ['rgb(0, 0, 255)', 'rgb(255, 255, 51)', 'rgb(255, 215, 0)', 'rgb(255, 0, 0)'];
    var indexName = ['Freezing','Caution','Ex. Caution','Danger'];
    for(var j = 0; j < indexTemp.length; j++){

        var lines = {
            type: 'line',
            xref: 'paper',
            yref: 'y',
            x0: 0,
            y0: indexTemp[j],
            x1: 1,
            y1: indexTemp[j],
            line: {
                color: indexColors[j],
                width: 1
            }
        };
        layout.shapes.push(lines);

        var annotations = {
            xref: 'paper',
            x: 1,
            y: indexTemp[j],
            xanchor: 'left',
            yanchor: 'middle',
            text: indexName[j],
            showarrow: false,
            font: {
                family: 'Segoe UI',
                size: 14,
                color: '#7f7f7f'
            }
        };
        layout.annotations.push(annotations);
    }

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
                //myMarker.bindPopup('<a href="site/' + data[i].id + '">' + data[i].siteName + '</a>');
                siteMarkers.push(myMarker);
            }
        }
    });
}

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
                type: 'scatter'
            };
            data.push(collectedTemps);
            var chart = document.getElementById('temperature-chart');
            Plotly.newPlot(chart, data, layout);

            var greenIcon = new L.Icon({
                iconUrl: 'https://cdn.rawgit.com/pointhi/leaflet-color-markers/master/img/marker-icon-2x-green.png',
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