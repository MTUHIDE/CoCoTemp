$(function () {

    var myMap;
    function buildMap() {
        myMap = createMap();
        $('#basemaps').on('change', function() {
            changeBasemap(myMap, this.value);
        })
    }

    function populateSite() {
        $.ajax({
            method: 'post',
            url: "/cocotemp/site/" + siteID + "/info.json",
            success: function (data) {
                if (data.length === 0) {
                    return;
                }

                var myMarker = L.marker([data.siteLatitude, data.siteLongitude]).addTo(myMap);
                myMap.setView([data.siteLatitude, data.siteLongitude], 6);
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
                    temperature.push(datum['temperature'].toFixed(1));
                });
                buildChart(dates, temperature);
            }
        });

        function buildChart(dates, temperature) {
            var d3 = Plotly.d3;

            var gd3 = d3.select('div[id=\'temperature-chart\']').append('div')
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
            };

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
                annotations: [],
                showlegend: true,
                legend: {
                    x: 1.2,
                    y: 1 },
                margin: {r: 200}
            };

            var indexTemp = [0, 42];
            var indexColors = ['rgb(0, 0, 255)', 'rgb(255, 0, 0)'];
            var indexName = ['Freezing','Ex Caution'];
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


            Plotly.plot(gd, data, layout);

            window.onresize = function() {
                Plotly.Plots.resize(gd);
            };
        }
    }

    _.defer(buildMap);
    _.defer(populateSite);
    _.defer(populateChart);
});