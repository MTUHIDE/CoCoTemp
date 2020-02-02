$(function () {

    var myMap;
    var dates=[],temperature=[],tempF=[];
    var previousTemp=tempStandard;
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

        function changeTemperaturePreference() {


            if(tempStandard=='F')
            {
                tempStandard='C';
                previousTemp='F';
                buildChart();
            }
            else
            {
                tempStandard='F';
                previousTemp='C';
                buildChart();
            }
        }

        function buildChart() {

            var innerContainer = document.querySelector('#plot-area');
            var tempeSelect = innerContainer.querySelector('#temperature-select');
            var tempPlot = innerContainer.querySelector('#temperature-chart');

            tempeSelect.addEventListener('change', changeTemperaturePreference, false);

            var CelsiusThresholds=[
                {thresholdValue:"0", thresholdName:"Freezing"},
                {thresholdValue:"35", thresholdName:"<a href='https://www.weather.gov/ama/heatindex'>NWS Heat Caution</a>"},
                {thresholdValue:"56", thresholdName:"<a href='https://www.weather.gov/ama/heatindex'>NWS Heat Danger</a>"}
            ];
            var FahrenheitThresholds=[
                {thresholdValue:"32", thresholdName:"Freezing"},
                {thresholdValue:"95", thresholdName:"<a href='https://www.weather.gov/ama/heatindex'>NWS Heat Caution</a>"},
                {thresholdValue:"132.8", thresholdName:"<a href='https://www.weather.gov/ama/heatindex'>NWS Heat Danger</a>"}
            ];

            if(tempStandard =='F') {
                if(previousTemp=='C')
                {

                    var stdF=(stdTemp*(9/5)+32).toFixed(1);
                    var maxF=(maxTemp*(9/5)+32).toFixed(1);
                    var minF=(minTemp*(9/5)+32).toFixed(1);
                    var avgF=(avgTemp*(9/5)+32).toFixed(1);

                    stdTemp=stdF;
                    maxTemp=maxF;
                    minTemp=minF;
                    avgTemp=avgF;

                    if(stdF==0)
                    {
                        stdTemp=0;
                        stdF=0;
                    }
                    if(maxF==0){
                        maxTemp=0;
                        maxF=0;
                    }
                    if(minF==0)
                    {
                        minTemp=0;
                        minF=0;
                    }
                    if(avgF==0){
                        avgTemp=0;
                        avgF=0;
                    }

                    document.getElementById('max-temp').innerText=maxF+' °F'
                    document.getElementById('min-temp').innerText=minF+' °F'
                    document.getElementById('avg-temp').innerText=avgF+' °F'
                    document.getElementById('std-temp').innerText=stdF+' °F'
                }

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


                var data = [collectedTempsC, collectedTempF];
                var layout = {
                    xaxis: {
                        fixedrange: false,
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
                    annotations: [],
                    showlegend: true,
                    legend: {
                        x: 1.2,
                        y: 1
                    },
                    margin: {r: 200}
                };
                FahrenheitThresholds.forEach(function (threshold) {
                    var lines;
                    if(threshold.thresholdValue==32)
                    {
                        lines = {
                            type: 'line',
                            xref: 'paper',
                            yref: 'y',
                            x0: 0,
                            y0: threshold.thresholdValue,
                            x1: 1,
                            y1: threshold.thresholdValue,
                            line: {
                                color: 'rgb(51, 175, 255)',
                                width: 1
                            }
                        };
                    }
                    else if(threshold.thresholdValue==95)
                    {
                        lines = {
                            type: 'line',
                            xref: 'paper',
                            yref: 'y',
                            x0: 0,
                            y0: threshold.thresholdValue,
                            x1: 1,
                            y1: threshold.thresholdValue,
                            line: {
                                color: 'rgb(255,102,0)',
                                width: 1
                            }
                        };
                    }
                    else{
                        lines = {
                            type: 'line',
                            xref: 'paper',
                            yref: 'y',
                            x0: 0,
                            y0: threshold.thresholdValue,
                            x1: 1,
                            y1: threshold.thresholdValue,
                            line: {
                                color: 'rgb(206,0,1)',
                                width: 1
                            }
                        };
                    }


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
                Plotly.newPlot('temperature-chart', data, layout,{responsive:true});
            }
            else if(tempStandard =='C') {
                var stdC=((stdTemp-32)*5/9).toFixed(1);
                var maxC=((maxTemp-32)*5/9).toFixed(1);
                var minC=((minTemp-32)*5/9).toFixed(1);
                var avgC=((avgTemp-32)*5/9).toFixed(1);

                stdTemp=stdC;
                maxTemp=maxC;
                minTemp=minC;
                avgTemp=avgC;
                if(stdC==0)
                {
                    stdTemp=0;
                    stdC=0;
                }
                if(maxC==0){
                    maxTemp=0;
                    maxC=0;
                }
                if(minC==0)
                {
                    minTemp=0;
                    minC=0;
                }
                if(avgC==0){
                    avgTemp=0;
                    avgC=0;
                }


                document.getElementById('max-temp').innerText=maxC+' °C'
                document.getElementById('min-temp').innerText=minC+' °C'
                document.getElementById('avg-temp').innerText=avgC+' °C'
                document.getElementById('std-temp').innerText=stdC+' °C'
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


                var data = [collectedTempsC, collectedTempF];

                var layout = {
                    xaxis: {
                        fixedrange: false,
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
                        y: 1
                    },
                    margin: {r: 200}
                };

                CelsiusThresholds.forEach(function (threshold) {
                    var lines;
                    if(threshold.thresholdValue==0)
                    {
                        lines = {
                            type: 'line',
                            xref: 'paper',
                            yref: 'y',
                            x0: 0,
                            y0: threshold.thresholdValue,
                            x1: 1,
                            y1: threshold.thresholdValue,
                            line: {
                                color: 'rgb(51, 175, 255)',
                                width: 1
                            }
                        };
                    }
                    else if(threshold.thresholdValue==35)
                    {
                        lines = {
                            type: 'line',
                            xref: 'paper',
                            yref: 'y',
                            x0: 0,
                            y0: threshold.thresholdValue,
                            x1: 1,
                            y1: threshold.thresholdValue,
                            line: {
                                color: 'rgb(255,102,0)',
                                width: 1
                            }
                        };
                    }
                    else{
                        lines = {
                            type: 'line',
                            xref: 'paper',
                            yref: 'y',
                            x0: 0,
                            y0: threshold.thresholdValue,
                            x1: 1,
                            y1: threshold.thresholdValue,
                            line: {
                                color: 'rgb(206,0,1)',
                                width: 1
                            }
                        };
                    }
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
                Plotly.newPlot('temperature-chart', data, layout,{responsive:true});

            }

        }
    }

    _.defer(buildMap);
    _.defer(populateSite);
    _.defer(populateChart);
});