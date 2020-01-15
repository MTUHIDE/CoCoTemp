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
            //Adds buttons to change from C to F and vice versa
            if(tempStandard=='F') {
                var updatemenus = [{
                    y: 1,
                    yanchor: 'top',
                    buttons: [
                        {
                            method: 'update',
                            args: [{'visible': [false, true, true, true, false, false]}, {
                                'yaxis': {
                                    title: 'F°',
                                    titlefont: {
                                        family: 'Segoe UI',
                                        size: 16,
                                        color: '#7f7f7f'
                                    }
                                }, 'annotations': annotationsF
                            }],
                            label: 'Fahrenheit'
                        }, {
                            method: 'update',
                            args: [{'visible': [true, false, false, false, true, true]}, {
                                'yaxis': {
                                    title: 'C°',
                                    titlefont: {
                                        family: 'Segoe UI',
                                        size: 16,
                                        color: '#7f7f7f'
                                    }
                                }, 'annotations': annotationsC
                            }],
                            label: 'Celsius'
                        }]
                }];
            }
            else if(tempStandard=='C'){
                var updatemenus = [{
                    y: 1,
                    yanchor: 'top',
                    buttons: [
                        {
                            method: 'update',
                            args: [{'visible': [true, false, false, false, true, true]}, {
                                'yaxis': {
                                    title: 'C°',
                                    titlefont: {
                                        family: 'Segoe UI',
                                        size: 16,
                                        color: '#7f7f7f'
                                    }
                                }, 'annotations': annotationsC
                            }],
                            label: 'Celsius'
                        },
                        {
                            method: 'update',
                            args: [{'visible': [false, true, true, true, false, false]}, {
                                'yaxis': {
                                    title: 'F°',
                                    titlefont: {
                                        family: 'Segoe UI',
                                        size: 16,
                                        color: '#7f7f7f'
                                    }
                                }, 'annotations': annotationsF
                            }],
                            label: 'Fahrenheit'
                        }]
                }];
            }


            var gd3 = d3.select('div[id=\'temperature-chart\']').append('div')
                .style({
                    width: '100%',
                    height: '100%'
                });

            var gd = gd3.node();

            //data arrays for freezing and ex caution lines
            var yFreezeF = [] ;
            var yCautionF = [];
            var yFreezeC = [];
            var yCautionC = [];
            var i; //counter var for loops
            for(i = 0; i < dates.length; i++) {
                yFreezeF.push(32);
            }
            for (i = 0; i < dates.length; i++) {
                    yCautionF.push(107);
                }
            for (i = 0; i < dates.length; i++) {
                    yFreezeC.push(0);
                }
            for (i = 0; i < dates.length; i++) {
                    yCautionC.push(42);
                }

            if(tempStandard =='F') {
                var collectedTempF = {
                    hoverinfo: "y+x",
                    visible: true,
                    type: 'temp',
                    x: dates,
                    y: tempF,
                    name: 'site\'s temperature F',
                    mode: 'lines+markers',
                    type: 'scatter'
                }

                var collectedTempsC = {
                    hoverinfo: "y+x",
                    visible: false,
                    type: 'temp',
                    x: dates,
                    y: temperature,
                    name: 'site\'s temperature C',
                    mode: 'lines+markers',
                    type: 'scatter'
                };

                var freezeF;
                //If no data sets freezeF default if data goes by data points
                if (dates.length == 0) {
                    freezeF = {
                        showlegend: false,
                        hoverinfo: "none",
                        visible: true,
                        x: [0, 1, 2, 3, 4, 5],
                        y: [32, 32, 32, 32, 32, 32],
                        mode: 'lines',
                        marker: {color: '#0000FF'}
                    }
                } else {
                    freezeF = {
                        showlegend: false,
                        hoverinfo: "none",
                        visible: true,
                        x: dates,
                        y: yFreezeF,
                        mode: 'lines',
                        marker: {color: '#0000FF'}
                    }
                }

                var cautionF;
                if (dates.length == 0) {
                    cautionF = {
                        showlegend: false,
                        hoverinfo: "none",
                        dx: 0,
                        visible: true,
                        x: [0, 1, 2, 3, 4, 5],
                        y: [107, 107, 107, 107, 107, 107],
                        mode: 'lines',
                        marker: {color: '#FF0000'}
                    }
                } else {
                    cautionF = {
                        showlegend: false,
                        hoverinfo: "none",
                        dx: 0,
                        visible: true,
                        x: dates,
                        y: yCautionF,
                        mode: 'lines',
                        marker: {color: '#FF0000'}
                    }
                }

                var freezeC;
                if (dates.length == 0) {
                    freezeC = {
                        showlegend: false,
                        hoverinfo: "none",
                        visible: false,
                        x: [0, 1, 2, 3, 4, 5],
                        y: [0, 0, 0, 0, 0, 0],
                        mode: 'lines',
                        marker: {color: '#0000FF'}
                    }
                } else {
                    freezeC = {
                        showlegend: false,
                        hoverinfo: "none",
                        visible: false,
                        x: dates,
                        y: yFreezeC,
                        mode: 'lines',
                        marker: {color: '#0000FF'}
                    }
                }

                var cautionC;
                if (dates.length == 0) {
                    cautionC = {
                        showlegend: false,
                        hoverinfo: "none",
                        visible: false,
                        x: [0, 1, 2, 3, 4, 5],
                        y: [42, 42, 42, 42, 42, 42],
                        mode: 'lines',
                        marker: {color: '#FF0000'}
                    }
                } else {
                    cautionC = {
                        showlegend: false,
                        hoverinfo: "none",
                        visible: false,
                        x: dates,
                        y: yCautionC,
                        mode: 'lines',
                        marker: {color: '#FF0000'}
                    }
                }

                var data = [collectedTempsC, collectedTempF, freezeF, cautionF, freezeC, cautionC];

                var layout = {
                    updatemenus: updatemenus,
                    xaxis: {
                        fixedrange: true,
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
                        y: 1
                    },
                    margin: {r: 200}
                };
                Plotly.plot(gd, data, layout)

                window.onresize = function() {
                    Plotly.Plots.resize(gd);
                };
            }
            else if(tempStandard =='C')
            {
                var collectedTempF = {
                    hoverinfo: "y+x",
                    visible: false,
                    type: 'temp',
                    x: dates,
                    y: tempF,
                    name: 'site\'s temperature F',
                    mode: 'lines+markers',
                    type: 'scatter'
                }

                var collectedTempsC = {
                    hoverinfo: "y+x",
                    visible: true,
                    type: 'temp',
                    x: dates,
                    y: temperature,
                    name: 'site\'s temperature C',
                    mode: 'lines+markers',
                    type: 'scatter'
                };

                var freezeF;
                //If no data sets freezeF default if data goes by data points
                if (dates.length == 0) {
                    freezeF = {
                        showlegend: false,
                        hoverinfo: "none",
                        visible: false,
                        x: [0, 1, 2, 3, 4, 5],
                        y: [32, 32, 32, 32, 32, 32],
                        mode: 'lines',
                        marker: {color: '#0000FF'}
                    }
                } else {
                    freezeF = {
                        showlegend: false,
                        hoverinfo: "none",
                        visible: false,
                        x: dates,
                        y: yFreezeF,
                        mode: 'lines',
                        marker: {color: '#0000FF'}
                    }
                }

                var cautionF;
                if (dates.length == 0) {
                    cautionF = {
                        showlegend: false,
                        hoverinfo: "none",
                        dx: 0,
                        visible: false,
                        x: [0, 1, 2, 3, 4, 5],
                        y: [107, 107, 107, 107, 107, 107],
                        mode: 'lines',
                        marker: {color: '#FF0000'}
                    }
                } else {
                    cautionF = {
                        showlegend: false,
                        hoverinfo: "none",
                        dx: 0,
                        visible: false,
                        x: dates,
                        y: yCautionF,
                        mode: 'lines',
                        marker: {color: '#FF0000'}
                    }
                }

                var freezeC;
                if (dates.length == 0) {
                    freezeC = {
                        showlegend: false,
                        hoverinfo: "none",
                        visible: true,
                        x: [0, 1, 2, 3, 4, 5],
                        y: [0, 0, 0, 0, 0, 0],
                        mode: 'lines',
                        marker: {color: '#0000FF'}
                    }
                } else {
                    freezeC = {
                        showlegend: false,
                        hoverinfo: "none",
                        visible: true,
                        x: dates,
                        y: yFreezeC,
                        mode: 'lines',
                        marker: {color: '#0000FF'}
                    }
                }

                var cautionC;
                if (dates.length == 0) {
                    cautionC = {
                        showlegend: false,
                        hoverinfo: "none",
                        visible: true,
                        x: [0, 1, 2, 3, 4, 5],
                        y: [42, 42, 42, 42, 42, 42],
                        mode: 'lines',
                        marker: {color: '#FF0000'}
                    }
                } else {
                    cautionC = {
                        showlegend: false,
                        hoverinfo: "none",
                        visible: true,
                        x: dates,
                        y: yCautionC,
                        mode: 'lines',
                        marker: {color: '#FF0000'}
                    }
                }

                var data = [collectedTempsC, collectedTempF, freezeF, cautionF, freezeC, cautionC];

                var layout = {
                    updatemenus: updatemenus,
                    xaxis: {
                        fixedrange: true,
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
                    annotations: [{
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
                    }, {
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
                    }],
                    showlegend: true,
                    legend: {
                        x: 1.2,
                        y: 1
                    },
                    margin: {r: 200}
                };
                Plotly.plot(gd, data, layout)

                window.onresize = function() {
                    Plotly.Plots.resize(gd);
                };
            }

        }
    }

    _.defer(buildMap);
    _.defer(populateSite);
    _.defer(populateChart);
});