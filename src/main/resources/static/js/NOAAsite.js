$(function () {

    var myMap;
    function buildMap() {
        myMap = createMap();
        $('#basemaps').on('change', function() {
            changeBasemap(myMap, this.value);
        })
    }
    var dates = [], temperature = [], tempF = [];
    var maxC=0,minC=0,avgC=0,stdC=0;
    var maxF=0,minF=0,avgF=0,stdF=0;



    function populateSiteandPopulateChart() {
        var target= document.getElementById("temperature-chart");
        var opts={
            lines: 13, // The number of lines to draw
            length: 38, // The length of each line
            width: 17, // The line thickness
            radius: 45, // The radius of the inner circle
            scale: 1, // Scales overall size of the spinner
            corners: 1, // Corner roundness (0..1)
            color: '#000000', // CSS color or array of colors
            fadeColor: 'transparent', // CSS color or array of colors
            speed: 1, // Rounds per second
            rotate: 0, // The rotation offset
            animation: 'spinner-line-fade-quick', // The CSS animation name for the lines
            direction: 1, // 1: clockwise, -1: counterclockwise
            zIndex: 2e9, // The z-index (defaults to 2000000000)
            className: 'spinner', // The CSS class to assign to the spinner
            top: '50%', // Top position relative to parent
            left: '50%', // Left position relative to parent
            shadow: '0 0 1px transparent', // Box-shadow for the lines
            position: 'absolute' // Element positioning
        };
        var spinner = new Spinner(opts).spin(target);
        var today = new Date();
        var dd = String(today.getDate()).padStart(2, '0');
        var mm = String(today.getMonth() + 1).padStart(2, '0');
        var yyyy = today.getFullYear();
        var year_ago = new Date();
        year_ago.setFullYear(today.getFullYear()-1);
        var olddd = String(year_ago.getDate()).padStart(2, '0');
        var oldmm = String(year_ago.getMonth() + 1).padStart(2, '0');
        var oldyyyy = year_ago.getFullYear();

        var station = siteID;

        today = yyyy+'-'+mm+'-'+dd;
        year_ago = oldyyyy+'-'+oldmm+'-'+olddd;


        var help;

        $.ajax({
            method: 'get',
            datatype: 'json',
            async:true,
            headers: {"Token": "uZqEMqAJLHUBrZwgzdJvIdLodhoGWMKJ"},
            url: 'https://cors-anywhere.herokuapp.com/https://www.ncei.noaa.gov/access/services/data/v1?startDate='+year_ago+'&endDate='+today+'&dataset=global-hourly&dataTypes=TMP&stations='+station+'&format=json&units=metric&includeStationName=1&includeStationLocation=1&includeAttributes=1',
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

                for(var i=0;i<numberOfDataPoints;i++){
                    var tmp = data[i].TMP;
                    var tempSplit = tmp.split(',');
                    var nonconvertedtemp = tempSplit[0];
                    var convertedTemp = nonconvertedtemp/10;
                    help=convertedTemp;
                    if(convertedTemp!=999.9) {
                        dates.push(new Date(data[i].DATE));
                        temperature.push(parseFloat(convertedTemp.toFixed(1)));
                        tempF.push(parseFloat((convertedTemp * (9 / 5) + 32).toFixed(1)));
                    }
                }
                spinner.stop();
                maxC=findMax(temperature);
                maxF=findMax(tempF);
                minC=findMin(temperature);
                minF=findMin(tempF);
                avgC=findAvg(temperature);
                avgF=findAvg(tempF);
                stdC=findStd(temperature);
                stdF=findStd(tempF);

                buildChart();
            }
        });
        buildChart();
    }
    function findAvg(tempArr) {
        var sum =0;
        var avg;
        for(var i=0;i<tempArr.length;i++)
        {
            sum = sum+tempArr[i];
        }
        avg=sum/tempArr.length;
        return avg.toFixed(1);
    }

    function findStd(tempArr) {
        var avg = findAvg(tempArr);
        var newArr = [];
        for(var i=0;i<tempArr.length;i++)
        {
            var value=Math.pow((tempArr[i]-avg),2);
            newArr.push(value);
        }
        var newAvg = findAvg(newArr);
        var sqrtVal= Math.sqrt(newAvg);
        return sqrtVal.toFixed(1);

    }

    function findMax(tempArr) {
        var newmax = (tempArr[0]);
        for(var i=0;i<tempArr.length;i++)
            {
                if((tempArr[i])>newmax)
                {
                    newmax=(tempArr[i]);
                }
            }
        return newmax;
    }
    function findMin(tempArr) {
        var min = (tempArr[0]);
        for(var index=0;index<tempArr.length;index++)
        {
            if((tempArr[index])<min)
            {
                min=(tempArr[index]);
            }
        }
        return min;
    }

        function buildChart() {


            var innerContainer = document.querySelector('#plot-area');
            var tempeSelect = innerContainer.querySelector('#temperature-select');
            var tempPlot = innerContainer.querySelector('#temperature-chart');

            var CelsiusThresholds = [
                {thresholdValue: "0", thresholdName: "Freezing"},
                {
                    thresholdValue: "35",
                    thresholdName: "<a href='https://www.weather.gov/ama/heatindex'>NWS Heat Caution</a>"
                },
                {
                    thresholdValue: "56",
                    thresholdName: "<a href='https://www.weather.gov/ama/heatindex'>NWS Heat Danger</a>"
                }
            ];
            var FahrenheitThresholds = [
                {thresholdValue: "32", thresholdName: "Freezing"},
                {
                    thresholdValue: "95",
                    thresholdName: "<a href='https://www.weather.gov/ama/heatindex'>NWS Heat Caution</a>"
                },
                {
                    thresholdValue: "132.8",
                    thresholdName: "<a href='https://www.weather.gov/ama/heatindex'>NWS Heat Danger</a>"
                }
            ];

            tempeSelect.addEventListener('change', changeTemperaturePreference, false);
            var target = document.getElementById("temperature-chart");
            var opts = {
                lines: 13, // The number of lines to draw
                length: 38, // The length of each line
                width: 17, // The line thickness
                radius: 45, // The radius of the inner circle
                scale: 1, // Scales overall size of the spinner
                corners: 1, // Corner roundness (0..1)
                color: '#000000', // CSS color or array of colors
                fadeColor: 'transparent', // CSS color or array of colors
                speed: 1, // Rounds per second
                rotate: 0, // The rotation offset
                animation: 'spinner-line-fade-quick', // The CSS animation name for the lines
                direction: 1, // 1: clockwise, -1: counterclockwise
                zIndex: 2e9, // The z-index (defaults to 2000000000)
                className: 'spinner', // The CSS class to assign to the spinner
                top: '50%', // Top position relative to parent
                left: '50%', // Left position relative to parent
                shadow: '0 0 1px transparent', // Box-shadow for the lines
                position: 'absolute' // Element positioning
            };
            var spinner = new Spinner(opts).spin(target);
            //
            // var layout = {
            //     xaxis: {
            //         title: 'Time (24hrs)',
            //         titlefont: {
            //             family: 'Segoe UI',
            //             size: 12,
            //             color: '#7f7f7f'
            //         }
            //     },
            //     yaxis: {
            //         title: 'F°',
            //         titlefont: {
            //             family: 'Segoe UI',
            //             size: 16,
            //             color: '#7f7f7f'
            //         }
            //     },
            //     shapes: [],
            //     annotations: [{
            //         visible: true,
            //         xref: 'paper',
            //         x: 1,
            //         y: 107,
            //         xanchor: 'left',
            //         yanchor: 'middle',
            //         text: "Ex Caution",
            //         showarrow: false,
            //         font: {
            //             family: 'Segoe UI',
            //             size: 14,
            //             color: '#7f7f7f'
            //         }
            //     }, {
            //         visible: true,
            //         xref: 'paper',
            //         x: 1,
            //         y: 32,
            //         xanchor: 'left',
            //         yanchor: 'middle',
            //         text: "Freezing",
            //         showarrow: false,
            //         font: {
            //             family: 'Segoe UI',
            //             size: 14,
            //             color: '#7f7f7f'
            //         }
            //     }],
            //     showlegend: false,
            //     legend: {
            //         x: 1.2,
            //         y: 1
            //     },
            //     margin: {r: 200}
            // };


            if (tempStandard == 'F') {
                document.getElementById('Max').innerText = '' + maxF + ' °' + tempStandard;
                document.getElementById('Min').innerText = '' + minF + ' °' + tempStandard;
                document.getElementById('Avg').innerText = '' + avgF + ' °' + tempStandard;
                document.getElementById('Std').innerText = '' + stdF + ' °' + tempStandard;

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
                    if (threshold.thresholdValue == 32) {
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
                    } else if (threshold.thresholdValue == 95) {
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
                    } else {
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
                Plotly.newPlot('temperature-chart', data, layout, {responsive: true});
                spinner.stop();
            } else if (tempStandard == 'C') {
                document.getElementById('Max').innerText = '' + maxC + ' °' + tempStandard;
                document.getElementById('Min').innerText = '' + minC + ' °' + tempStandard;
                document.getElementById('Avg').innerText = '' + avgC + ' °' + tempStandard;
                document.getElementById('Std').innerText = '' + stdC + ' °' + tempStandard;
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
                    if (threshold.thresholdValue == 0) {
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
                    } else if (threshold.thresholdValue == 35) {
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
                    } else {
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
                Plotly.newPlot('temperature-chart', data, layout, {responsive: true});

                spinner.stop();

            }
        }


            function changeTemperaturePreference() {


                if(tempStandard=='F')
                {
                    tempStandard='C';
                    buildChart();
                }
                else
                    {
                        tempStandard='F';
                        buildChart();
                    }
        }

    _.defer(buildMap);
    _.defer(populateSiteandPopulateChart);
});