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
        var dates = [], temperature = [], tempF = [];

        $.ajax({
            method: 'get',
            url: "/cocotemp/site/" + siteID + "/temperature.json",
            success: function (data) {

                data.forEach(function (datum) {
                    dates.push(new Date(datum['dateTime']));
                    temperature.push(datum['temperature'].toFixed(1));
                    tempF.push((datum['temperature']*(9/5)+32).toFixed(1));
                });
                buildChart(dates, temperature, tempF);
            }
        });

        function buildChart(dates, temperature, tempF) {
            var d3 = Plotly.d3;
            //Adds buttons to change from C to F and vice versa
            var annotationsC = [{
                visible: true,
                xref: 'paper',
                x: 1,
                y: 42,
                xanchor: 'left',
                yanchor: 'middle',
                text: "Ex Caution",
                showarrow: false,
                font: {
                    family: 'Segoe UI',
                    size: 14,
                    color: '#7f7f7f'
                }
            },{
                visible: true,
                xref: 'paper',
                x: 1,
                y: 0,
                xanchor: 'left',
                yanchor: 'middle',
                text: "Freezing",
                showarrow: false,
                font: {
                    family: 'Segoe UI',
                    size: 14,
                    color: '#7f7f7f'
                }
            }];

            var annotationsF = [{
                visible: true,
                xref: 'paper',
                x: 1,
                y: 107,
                xanchor: 'left',
                yanchor: 'middle',
                text: "Ex Caution",
                showarrow: false,
                font: {
                    family: 'Segoe UI',
                    size: 14,
                    color: '#7f7f7f'
                }
            }, {
                visible: true,
                xref: 'paper',
                x: 1,
                y: 32,
                xanchor: 'left',
                yanchor: 'middle',
                text: "Freezing",
                showarrow: false,
                font: {
                    family: 'Segoe UI',
                    size: 14,
                    color: '#7f7f7f'
                }
            }];

            var updatemenus = [{
                    y: 1,
                    yanchor: 'top',
                    buttons: [
                        {
                        method: 'update',
                        args: [{'visible': [false, true, true, true, false, false]},{ 'yaxis' : {
                        title : 'F°',
                            titlefont: {
                            family: 'Segoe UI',
                            size: 16,
                            color: '#7f7f7f'
                        }
                    }, 'annotations': annotationsF }],
                        label: 'Fahrenheit'
                    }, {
                        method: 'update',
                        args: [{'visible': [true, false,false,false, true, true]} ,{'yaxis' : {
                            title : 'C°',
                                titlefont: {
                                    family: 'Segoe UI',
                                    size: 16,
                                    color: '#7f7f7f'
                                }
                            },'annotations': annotationsC}],
                            label: 'Celsius'
                        }]
                    }];

            var gd3 = d3.select('div[id=\'temperature-chart\']').append('div')
                .style({
                    width: '100%',
                    height: '100%'
                });

            var gd = gd3.node();
            var yFreezeF = [] ;
            var yCautionF = [];
            var yFreezeC = [];
            var yCautionC = [];
            var i;
            var l = dates.length;
            for(i = 0; i < l; i++ ){
                yFreezeF.push(32);
            }
            for(i = 0; i < l; i++){
                yCautionF.push(107);
            }
            for(i = 0; i < l; i++){
                yFreezeC.push(0);
            }
            for(i = 0; i < l; i++ ){
                yCautionC.push(42);
            }

            var collectedTempF = {
                hoverinfo: "y",
                visible: true,
                type: 'temp',
                x: dates,
                y: tempF,
                name: 'site\'s temperature F',
                type: 'scatter'
            }

            var collectedTempsC = {
                hoverinfo: "y",
                visible: false,
                type: 'temp',
                x: dates,
                y: temperature,
                name: 'site\'s temperature C',
                type: 'scatter'
            };

            var freezeF = {
                showlegend: false,
                hoverinfo: "none",
                visible: true,
                dx: 0,
                x: dates,
                y: yFreezeF,
                dy: 0,
                mode : 'lines',
                marker: {color: '#0000FF'}
            }

            var cautionF ={
                showlegend: false,
                hoverinfo: "none",
                dx: 0,
                visible: true,
                x: dates,
                y: yCautionF,
                mode : 'lines',
                marker: {color: '#FF0000'}
            }

            var freezeC = {
                showlegend: false,
                hoverinfo: "none",
                visible: false,
                x: dates,
                y: yFreezeC,
                mode : 'lines',
                marker: {color: '#0000FF'}
            }
            var cautionC ={
                showlegend: false,
                hoverinfo: "none",
                visible: false,
                x: dates,
                y: yCautionC,
                mode : 'lines',
                marker: {color: '#FF0000'}
            }


            var data = [collectedTempsC, collectedTempF, freezeF, cautionF, freezeC, cautionC ];

            var layout = {
                updatemenus: updatemenus,
                xaxis: {
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
                annotations: [{
                    visible: true,
                    xref: 'paper',
                    x: 1,
                    y: 107,
                    xanchor: 'left',
                    yanchor: 'middle',
                    text: "Ex Caution",
                    showarrow: false,
                    font: {
                        family: 'Segoe UI',
                        size: 14,
                        color: '#7f7f7f'
                    }
                }, {
                    visible: true,
                    xref: 'paper',
                    x: 1,
                    y: 32,
                    xanchor: 'left',
                    yanchor: 'middle',
                    text: "Freezing",
                    showarrow: false,
                    font: {
                        family: 'Segoe UI',
                        size: 14,
                        color: '#7f7f7f'
                    }
                }],
                showlegend: true,
                legend: {
                    x: 1.2,
                    y: 1 },
                margin: {r: 200}
            };
            Plotly.plot(gd, data, layout)

            window.onresize = function() {
                Plotly.Plots.resize(gd);
            };
        }
    }

    _.defer(buildMap);
    _.defer(populateSite);
    _.defer(populateChart);
});