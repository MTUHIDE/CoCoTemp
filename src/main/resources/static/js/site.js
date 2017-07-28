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
            method: 'post',
            url: "/cocotemp/site/" + siteID + "/temperature.json",
            success: function (data) {
                if(data.length == 0){
                    return;
                }

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

            var data = [{
                x: dates,
                y: temperature,
                name: 'site\'s temperature',
                type: 'scatter',
                line: {shape: 'spline'}
            }];

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
                }
            };



            Plotly.plot(gd, data, layout, {modeBarButtonsToRemove: ['sendDataToCloud','hoverCompareCartesian', 'hoverClosestCartesian', 'resetScale2d' ,'toggleSpikelines','zoom2d','select2d','lasso2d',]});

            window.onresize = function() {
                Plotly.Plots.resize(gd);
            };
        }
    }

    _.defer(createMap);
    _.defer(populateSite);
    _.defer(populateChart);
});