$(function () {

    var myMap;

    //Creates map object
    function createMap() {
        myMap = L.map('map').setView([37.0902, -95.7129], 4);
        L.tileLayer('https://api.mapbox.com/styles/v1/cjsumner/ciu0aibyr002p2iqd51spbo9p/tiles/256/{z}/{x}/{y}?access_token=pk.eyJ1IjoiY2pzdW1uZXIiLCJhIjoiY2lmeDhkMDB3M3NpcHUxbTBlZnoycXdyYyJ9.NKtr-pvthf3saPDsRDGTmw', {
            attribution: 'Map data &copy; <a href="http://openstreetmap.org">OpenStreetMap</a> contributors, <a href="http://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>, Imagery © <a href="http://mapbox.com">Mapbox</a>',
            maxZoom: 18,
            id: 'your.mapbox.project.id',
            accessToken: 'your.mapbox.public.access.token'
        }).addTo(myMap);
    }

    function populateSite() {
        $.ajax({
            method: 'post',
            url: "/cocotemp/site/" + siteID + "/info.json",
            success: function (data) {
                if (data.length == 0) {
                    return;
                }

                var myMarker = L.marker([data.siteLatitude, data.siteLongitude]).addTo(myMap);
                var group = L.featureGroup([myMarker]);
                myMap.fitBounds(group.getBounds());
            }
        });
    }

    function populateChart() {
        var dates = [], temperature = [];
        $.ajax({
            method: 'get',
            url: "/cocotemp/site/" + siteID + "/temperature.json",
            success: function (data) {

                data.forEach(function (datum) {
                    dates.push(new Date(datum['dateTime']));
                    temperature.push(datum['temperature']);
                });
                buildChart(dates, temperature);
            }
        });

        function buildChart(dates, temperature) {
            var d3 = Plotly.d3;

            var gd3 = d3.select('div[id=\'temperature-chart\']')
                .append('div')
                .style({
                    width: '100%',
                    height: '100%'
                });

            var gd = gd3.node();

            var collectedTemps = {
                x: dates,
                y: temperature,
                name: 'site\'s temperature',
                type: 'scatter'
            }

            var indexTemp = [0, 31, 35, 44, 56];
            var indexColors = ['rgb(0, 0, 255)', 'rgb(255, 255, 51)', 'rgb(255, 215, 0)', 'rgb(255, 140, 0)', 'rgb(255, 0, 0)'];
            var indexName = ['Freezing','Caution','Ex. Caution','Danger','Ex. Danger'];

            var data = [collectedTemps];

            var layout = {
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
                annotations: []
            };

            for(var i = 0; i < indexTemp.length; i++){
                var index = {
                    type: 'line',
                        xref: 'paper',
                    yref: 'y',
                    x0: 0,
                    y0: indexTemp[i],
                    x1: 1,
                    y1: indexTemp[i],
                    line: {
                    color: indexColors[i],
                        width: 1
                    }
                }
                layout.shapes.push(index);
            }

            for(var i = 0; i < indexTemp.length; i++){
                var index = {
                    xref: 'paper',
                    x: 1,
                    y: indexTemp[i],
                    xanchor: 'left',
                    yanchor: 'middle',
                    text: indexName[i],
                    showarrow: false,
                    font: {
                        family: 'Segoe UI',
                        size: 14,
                        color: '#7f7f7f'
                    }
                }
                layout.annotations.push(index);
            }


            Plotly.plot(gd, data, layout);

            window.onresize = function() {
                Plotly.Plots.resize(gd);
            };
        }
    }

    _.defer(createMap);
    _.defer(populateSite);
    _.defer(populateChart);
});