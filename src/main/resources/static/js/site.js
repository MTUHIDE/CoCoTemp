$(function () {

    var myMap;
    var dates=[],temperature=[],tempF=[], spikes=[], spikesC=[],spikeDates = [];
    var previousTemp=tempStandard;
    var dataLength=0;




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
                    dataLength=0;
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
                dataLength=data.length;

                findSuspectSpike();
                findDiscontinuity();
                buildChart(dates, spikes, temperature, tempF);
            }
        });

        /*Finds temperature spikes to be used on the graph*/
        function findSuspectSpike(){
            var lastIndex = 0;
            var curIndex = 0;
            var spikeDateTemp = [];
            var spikeTemps = [];
            var spikeTempsC = [];
            //Finds temperature spikes
            for(var i = 0; i < dates.length; i++){
                curIndex = i;
                var x = 0;//Used for count

                if(Math.abs(dates[curIndex].getHours() - dates[lastIndex].getHours()) >= 1 ){
                    if(Math.abs(tempF[curIndex] - tempF[lastIndex]) > 40){
                        x  = lastIndex;
                        while( x <= curIndex){ //If there is a spike will add corresponding temperature points to the spike arrays
                            if(spikeTemps[x] != null){
                                x++
                            } else {
                                spikeDateTemp[x] = dates[x];
                                spikeTemps[x] = tempF[x];
                                spikeTempsC[x] = temperature[x];
                                x++;
                            }
                        }
                    } else {
                        x = lastIndex; //If there is no spike will fill in spots with null data
                        while (x <= curIndex){
                            if(spikeTemps[x] != null){
                                x++
                            } else {
                                spikeDateTemp[x] = dates[x];
                                spikeTemps[x] = null;
                                spikeTempsC[x] = null;
                                x++;
                            }

                        }
                    }
                lastIndex = curIndex;
                }
            }



            var finalSpikeF = [];
            var finalSpikeDates = [];
            var finalSpikeC = [];
            var z = 0;
            //Loops through the gathered spike data an inserts null values to make gaps on the graph where needed
            while(z < spikeTemps.length){
                if(spikeTemps[z] != null){//if not null check if next is null
                    if(spikeTemps[z+1] != null){//If both are not null verify they are apart by 40 degrees if not add null gap
                        if(Math.abs(spikeTemps[z]-spikeTemps[z+1]) < 40){
                            finalSpikeF.push(spikeTemps[z]);
                            finalSpikeC.push(spikeTempsC[z])
                            finalSpikeDates.push(spikeDateTemp[z]);
                            finalSpikeF.push(null);
                            finalSpikeC.push(null);
                            finalSpikeDates.push(null);
                            z++;
                        }
                    }
                }
                finalSpikeF.push(spikeTemps[z]);
                finalSpikeC.push(spikeTempsC[z]);
                finalSpikeDates.push(spikeDateTemp[z]);
                z++;
            }


            var count = 0;
            while(count < finalSpikeF.length){
                spikes.push(finalSpikeF[count]);
                spikesC.push(finalSpikeC[count]);
                spikeDates.push(finalSpikeDates[count]);
                count++;
            }


        }
        function findDiscontinuity() {
            for(var i=0; i <dates.length-2;i++){
                var diff= differenceHours(dates[i],dates[i+1]);
                if(diff>1){
                    temperature.splice(i+1,0,null);
                    temperature.join();
                    tempF.splice(i+1,0,null);
                    tempF.join();
                }

            }


        }

        function differenceHours(date1,date2) {
            var diff =(date2.getTime() - date1.getTime()) / 1000;
            diff /= (60 * 60);
            return Math.abs(Math.round(diff));
        }

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

           var select = $('#temperature-select');

            if(tempStandard=='C'){
                select.empty();
                select.append('<option value="C">Celsius</option>');
                select.append('<option value="F">Fahrenheit</option>');
            }

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
                if(dataLength==0||dataLength==undefined){
                    document.getElementById('max-temp').innerText='0 °F';
                    document.getElementById('min-temp').innerText='0 °F';
                    document.getElementById('avg-temp').innerText='0 °F';
                    document.getElementById('std-temp').innerText='0 °F';
                }
                else if(previousTemp=='C')
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

                var spikesLine = {
                    hoverinfo: "none",
                    visible: true,
                    x: spikeDates,
                    y: spikes,
                    name: 'site\'s temperature spikes',
                    mode: 'lines+markers',
                    type:'scattergl',
                    connectgaps: false
                }

                var collectedTempF = {
                    hoverinfo: "y+x",
                    visible: true,
                    x: dates,
                    y: tempF,
                    name: 'site\'s temperature F',
                    mode: 'lines+markers',
                    type:'scattergl',
                    connectgaps: false
                }

                var collectedTempsC = {
                    hoverinfo: "y+x",
                    visible: false,
                    x: dates,
                    y: temperature,
                    name: 'site\'s temperature C',
                    mode: 'lines+markers',
                    type:'scattergl',
                    connectgaps: false
                };


                var data = [collectedTempsC, collectedTempF, spikesLine];
                var layout = {
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
                            family: 'Open Sans',
                            size: 14,
                            color: '#7f7f7f'
                        }
                    };
                    layout.annotations.push(annotations);
                });
                Plotly.newPlot('temperature-chart', data, layout,{responsive:true});
            }
            else if(tempStandard =='C') {


                if(dataLength==0||dataLength==undefined){
                    document.getElementById('max-temp').innerText='0 °C';
                    document.getElementById('min-temp').innerText='0 °C';
                    document.getElementById('avg-temp').innerText='0 °C';
                    document.getElementById('std-temp').innerText='0 °C';
                }
                else if(previousTemp=='F'){


                    var stdC = ((stdTemp - 32) * 5 / 9).toFixed(1);
                    var maxC = ((maxTemp - 32) * 5 / 9).toFixed(1);
                    var minC = ((minTemp - 32) * 5 / 9).toFixed(1);
                    var avgC = ((avgTemp - 32) * 5 / 9).toFixed(1);

                    stdTemp = stdC;
                    maxTemp = maxC;
                    minTemp = minC;
                    avgTemp = avgC;
                    if (stdC == 0) {
                        stdTemp = 0;
                        stdC = 0;
                    }
                    if (maxC == 0) {
                        maxTemp = 0;
                        maxC = 0;
                    }
                    if (minC == 0) {
                        minTemp = 0;
                        minC = 0;
                    }
                    if (avgC == 0) {
                        avgTemp = 0;
                        avgC = 0;
                    }
                    document.getElementById('max-temp').innerText=maxC+' °C'
                    document.getElementById('min-temp').innerText=minC+' °C'
                    document.getElementById('avg-temp').innerText=avgC+' °C'
                    document.getElementById('std-temp').innerText=stdC+' °C'
                }

                var spikeLineC = {
                    hoverinfo: "none",
                    visible: true,
                    x: spikeDates,
                    y: spikesC,
                    name: 'site\'s temperature spikes',
                    mode: 'lines+markers',
                    type:'scattergl',
                    connectgaps: false
                }

                var collectedTempF = {
                    hoverinfo: "y+x",
                    visible: false,
                    x: dates,
                    y: tempF,
                    name: 'site\'s temperature F',
                    mode: 'lines+markers',
                    type:'scattergl',
                    connectgaps: false
                }

                var collectedTempsC = {
                    hoverinfo: "y+x",
                    visible: true,
                    x: dates,
                    y: temperature,
                    name: 'site\'s temperature C',
                    mode: 'lines+markers',
                    type:'scattergl',
                    connectgaps: false
                };


                var data = [collectedTempsC, collectedTempF, spikeLineC];

                var layout = {
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
                            family: 'Open Sans',
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
