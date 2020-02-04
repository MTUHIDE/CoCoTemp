$(function () {

    var myMap;

    function buildMap() {
        myMap = createMap();
        $('#basemaps').on('change', function () {
            changeBasemap(myMap, this.value);
        })
    }

    var dates = [], temperature = [], tempF = [];
    var maxC = 0, minC = 0, avgC = 0, stdC = 0;
    var maxF = 0, minF = 0, avgF = 0, stdF = 0;
    var select = $('#temperature-select');

    if (tempStandard == 'C') {
        select.empty();
        select.append('<option value="C">Celsius</option>');
        select.append('<option value="F">Fahrenheit</option>');
    }
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

    var innerContainer = document.querySelector('#plot-area');
    var tempeSelect = innerContainer.querySelector('#temperature-select');
    var tempPlot = innerContainer.querySelector('#temperature-chart');
    var dataF=[], dataC=[];
    tempeSelect.addEventListener('change', changeTemperaturePreference, false);


    function findMax(tempArr) {
        var newmax = (tempArr[0]);
        for (var i = 0; i < tempArr.length; i++) {
            if ((tempArr[i]) > newmax) {
                newmax = (tempArr[i]);
            }
        }
        return newmax;
    }

    function findMin(tempArr) {
        var min = (tempArr[0]);
        for (var index = 0; index < tempArr.length; index++) {
            if ((tempArr[index]) < min) {
                min = (tempArr[index]);
            }
        }
        return min;
    }

    function findAvg(tempArr) {
        var result= tempArr.reduce((a, b) => (a + b)) / tempArr.length;

        return result.toFixed(1);
    }

    function findStd(tempArr,averageC) {
        var squareDiffs = tempArr.map(function(value){
            var diff = value - averageC;
            var sqrDiff = diff * diff;
            return sqrDiff;
        });
        var newAvg = findAvg(squareDiffs);
        var sqrtVal = Math.sqrt(newAvg);
        return sqrtVal.toFixed(1);

    }

    function populateSiteandPopulateChart() {
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
        var today = new Date();
        var dd = String(today.getDate()).padStart(2, '0');
        var mm = String(today.getMonth() + 1).padStart(2, '0');
        var yyyy = today.getFullYear();
        var year_ago = new Date();
        year_ago.setFullYear(today.getFullYear() - 1);
        var olddd = String(year_ago.getDate()).padStart(2, '0');
        var oldmm = String(year_ago.getMonth() + 1).padStart(2, '0');
        var oldyyyy = year_ago.getFullYear();

        var station = siteID;

        today = yyyy + '-' + mm + '-' + dd;
        year_ago = oldyyyy + '-' + oldmm + '-' + olddd;


        var help;

        $.ajax({
            method: 'get',
            datatype: 'json',
            async: true,
            headers: {"Token": NOAAToken},
            url: 'https://cors-anywhere.herokuapp.com/https://www.ncei.noaa.gov/access/services/data/v1?startDate=' + year_ago + '&endDate=' + today + '&dataset=global-hourly&dataTypes=TMP&stations=' + station + '&format=json&units=metric&includeStationName=1&includeStationLocation=1&includeAttributes=1',
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
                document.getElementById("siteName").innerText = data[0].NAME;
                document.getElementById("LatLong").innerText = 'Location: ' + data[0].LATITUDE + ',' + data[0].LONGITUDE;
                document.getElementById("elevation").innerText = 'Elevation: ' + data[0].ELEVATION + ' m';

                var myMarker = L.marker([data[0].LATITUDE, data[0].LONGITUDE], {icon: NOAAIcon}).addTo(myMap);
                myMap.setView([data[0].LATITUDE, data[0].LONGITUDE], 6);

                var numberOfDataPoints = Object.keys(data).length;

                for (var i = 0; i < numberOfDataPoints; i++) {
                    var tmp = data[i].TMP;
                    var tempSplit = tmp.split(',');
                    var nonconvertedtemp = tempSplit[0];
                    var convertedTemp = nonconvertedtemp / 10;
                    help = convertedTemp;
                    if (convertedTemp != 999.9) {
                        dates.push(new Date(data[i].DATE));
                        temperature.push(parseFloat(convertedTemp.toFixed(1)));
                        tempF.push(parseFloat((convertedTemp * (9 / 5) + 32).toFixed(1)));
                    }
                }

                maxC = findMax(temperature).toFixed(1);
                maxF = (maxC * (9 / 5) + 32).toFixed(1);

                minC = findMin(temperature).toFixed(1);
                minF = (minC * (9 / 5) + 32).toFixed(1);

                avgC = findAvg(temperature);
                avgF = (avgC * (9 / 5) + 32).toFixed(1);

                stdC = findStd(temperature,avgC);
                stdF = (stdC * (9 / 5) + 32).toFixed(1);

                var collectedTemps =  {
                    hoverinfo: "y+x",
                    x: dates,
                    y: tempF,
                    name: 'site\'s temperature F',
                    mode: 'lines+markers',
                    type:'scattergl',
                    connectgaps: false
                }
                var otherTemps= {
                    hoverinfo: "y+x",
                    x: dates,
                    y: temperature,
                    name: 'site\'s temperature C',
                    mode: 'lines+markers',
                    type:'scattergl',
                    connectgaps: false
                }

                addTemperature(collectedTemps,otherTemps);
                findDiscontinuity();
                buildChart();
                spinner.stop();
            }
        });
    }

    function findDiscontinuity() {
        for (var i = 0; i < dates.length - 2; i++) {
            var diff = differenceHours(dates[i], dates[i + 1]);
            if (diff > 1) {
                temperature.splice(i + 1, 0, null);
                temperature.join();
                tempF.splice(i + 1, 0, null);
                tempF.join();
            }

        }


    }

    function differenceHours(date1, date2) {
        var diff = (date2.getTime() - date1.getTime()) / 1000;
        diff /= (60 * 60);
        return Math.abs(Math.round(diff));
    }

    function getLayout() {
        var layout = null;
        if (tempStandard == "F") {
            layout = {
                xaxis: {
                    fixedrange: false,
                    title: 'Time (24hrs)',
                    titlefont: {
                        family: 'Open Sans',
                        size: 12,
                        color: '#7f7f7f'
                    }
                },
                yaxis: {
                    title: 'F°',
                    titlefont: {
                        family: 'Open Sans',
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
        } else {
            layout = {
                xaxis: {
                    fixedrange: false,
                    title: 'Time (24hrs)',
                    titlefont: {
                        family: 'Open Sans',
                        size: 12,
                        color: '#7f7f7f'
                    }
                },
                yaxis: {
                    title: 'C°',
                    titlefont: {
                        family: 'Open Sans',
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
        }
        return layout;

    }

    function createChart() {
        var dateTimes = [];
        var tempFahrenheit = [];
        var collected = {
            hoverinfo: "y+x",
            visible: true,
            x: dateTimes,
            y: tempFahrenheit,
            name: 'site\'s temperature ' + tempStandard,
            mode: 'lines+markers',
            type:'scattergl',
            connectgaps: false
        }
        var data = [collected];
        var layout = getLayout();
        var thresholds = null;

        document.getElementById('Max').innerText = '' + 0 + ' °' + tempStandard;
        document.getElementById('Min').innerText = '' + 0 + ' °' + tempStandard;
        document.getElementById('Avg').innerText = '' + 0 + ' °' + tempStandard;
        document.getElementById('Std').innerText = '' + 0 + ' °' + tempStandard;

        if (tempStandard == 'F') {
            thresholds = FahrenheitThresholds;
        } else{
            thresholds = CelsiusThresholds;
        }
        thresholds.forEach(function (threshold) {
            var lines = null;
            if (threshold.thresholdValue == '0' || threshold.thresholdValue == '32') {
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
            } else if (threshold.thresholdValue == '35' || threshold.thresholdValue == '95') {
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
            } else if (threshold.thresholdValue == '56' || threshold.thresholdValue == '132.8') {
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
                        color: 'rgb(0, 0, 255)',
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
                    family: 'Open Sans',
                    size: 14,
                    color: '#7f7f7f'
                }
            };
            layout.annotations.push(annotations);
        });
        Plotly.newPlot('temperature-chart', data, layout, {responsive: true});
    }

    function addTemperature(collectedTemp,otherTemp) {
            dataF=[collectedTemp];
            dataC=[otherTemp];
    }

    function buildChart() {

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
        var data = null;
        var layout = getLayout();
        var thresholds=null;


        if (tempStandard == 'F') {
            document.getElementById('Max').innerText = '' + maxF + ' °' + tempStandard;
            document.getElementById('Min').innerText = '' + minF + ' °' + tempStandard;
            document.getElementById('Avg').innerText = '' + avgF + ' °' + tempStandard;
            document.getElementById('Std').innerText = '' + stdF + ' °' + tempStandard;
            thresholds=FahrenheitThresholds;
            data=dataF;
        } else{
            document.getElementById('Max').innerText = '' + maxC + ' °' + tempStandard;
            document.getElementById('Min').innerText = '' + minC + ' °' + tempStandard;
            document.getElementById('Avg').innerText = '' + avgC + ' °' + tempStandard;
            document.getElementById('Std').innerText = '' + stdC + ' °' + tempStandard;
            thresholds=CelsiusThresholds;
            data=dataC;
        }
        thresholds.forEach(function (threshold) {
            var lines = null;
            if (threshold.thresholdValue == '0' || threshold.thresholdValue == '32') {
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
            } else if (threshold.thresholdValue == '35' || threshold.thresholdValue == '95') {
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
            } else if (threshold.thresholdValue == '56' || threshold.thresholdValue == '132.8') {
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
                        color: 'rgb(0, 0, 255)',
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
                    family: 'Open Sans',
                    size: 14,
                    color: '#7f7f7f'
                }
            };
            layout.annotations.push(annotations);
        });
        Plotly.newPlot('temperature-chart', data, layout, {responsive: true});
        spinner.stop();
    }


    function changeTemperaturePreference() {


        if (tempStandard == 'F') {
            tempStandard = 'C';
            buildChart();
        } else {
            tempStandard = 'F';
            buildChart();
        }
    }

    _.defer(createChart());
    _.defer(buildMap);
    _.defer(populateSiteandPopulateChart);
});


