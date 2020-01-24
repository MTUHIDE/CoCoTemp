$(function () {

    var myMap;
    function buildMap() {
        myMap = createMap();
        $('#basemaps').on('change', function() {
            changeBasemap(myMap, this.value);
        })
    }



    function populateSiteandPopulateChart() {
        var today = new Date();
        var dd = String(today.getDate()).padStart(2, '0');
        var mm = String(today.getMonth() + 1).padStart(2, '0');
        var yyyy = today.getFullYear();
        var year_ago = new Date();
        year_ago.setFullYear(today.getFullYear()-1)
        var olddd = String(year_ago.getDate()).padStart(2, '0');
        var oldmm = String(year_ago.getMonth() + 1).padStart(2, '0');
        var oldyyyy = year_ago.getFullYear();

        var station = siteID;

        today = yyyy+'-'+mm+'-'+dd;
        year_ago = oldyyyy+'-'+oldmm+'-'+olddd;


        var dates = [], temperature = [], tempF = [];
        var help;

        $.ajax({
            method: 'get',
            datatype: 'json',
            headers: {"Token": "uZqEMqAJLHUBrZwgzdJvIdLodhoGWMKJ"},
            url: 'https://www.ncei.noaa.gov/access/services/data/v1?startDate='+year_ago+'&endDate='+today+'&dataset=global-hourly&dataTypes=TMP&stations='+station+'&format=json&units=metric&includeStationName=1&includeStationLocation=1&includeAttributes=1',
            success: function (data) {
                if (data.length === 0) {
                    return;
                }
                var NOAAIcon = L.icon({
                    iconUrl: '/cocotemp/images/NOAA-map-marker.png',

                    iconSize: [25, 41], // size of the icon
                    iconAnchor: [25, 41], // point of the icon which will correspond to marker's location
                    popupAnchor: [-25, -41] // point from which the popup should open relative to the iconAnchor
                });
                document.getElementById("siteName").innerText=data[0].NAME;
                document.getElementById("LatLong").innerText='Location: '+data[0].LATITUDE+','+data[0].LONGITUDE;
                document.getElementById("elevation").innerText='Elevation: '+data[0].ELEVATION+' m';

                var myMarker = L.marker([data[0].LATITUDE, data[0].LONGITUDE], {icon: NOAAIcon}).addTo(myMap);
                myMap.setView([data[0].LATITUDE,data[0].LONGITUDE], 6);

                var numberOfDataPoints=Object.keys(data).length;

                for(i=0;i<numberOfDataPoints;i++){
                    var tmp = data[i].TMP;
                    var tempSplit = tmp.split(',');
                    nonconvertedtemp = tempSplit[0];
                    var convertedTemp = nonconvertedtemp/10;
                    help=convertedTemp;
                    if(convertedTemp!=999.9) {
                        dates.push(new Date(data[i].DATE));
                        temperature.push(convertedTemp.toFixed(1));
                        tempF.push((convertedTemp * (9 / 5) + 32).toFixed(1));
                    }
                }
                buildChart(dates, temperature, tempF);
            }
        });
    }


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


    _.defer(buildMap);
    _.defer(populateSiteandPopulateChart);
});