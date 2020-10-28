$(function () {

    var myMap;
    var dates=[],temperature=[],tempF=[], anomalyDates=[], anomaliesF=[], anomaliesC=[]
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

    function getDateRange() {
        var today = new Date();
        var dd = String(today.getDate()).padStart(2, '0');
        var mm = String(today.getMonth() + 1).padStart(2, '0');
        var yyyy = today.getFullYear();
        var year_ago = new Date();
        year_ago.setFullYear(today.getFullYear() - 1)
        var olddd = String(year_ago.getDate()).padStart(2, '0');
        var oldmm = String(year_ago.getMonth() + 1).padStart(2, '0');
        var oldyyyy = year_ago.getFullYear();


        today = yyyy + '-' + mm + '-' + dd;
        year_ago = oldyyyy + '-' + oldmm + '-' + olddd;
        return {today, year_ago};
    }



    function populateChart() {

        getSiteData();

        //NOTE NOAA DOES NOT CONTAIN DATA UP TO CURRENT DAY
        function compareNOAA(NOAAdates,NOAAtemp, NOAAtempF) {
            let siteDates = dates;
            let siteTemp = temperature;
            let siteTempF = tempF;

            let anomalyDate = [];
            let anomalyTemp = [];
            let anomalyTempF = [];

            //let NOAALength = NOAAdates.length;
            ///hour in ms = 3600000
            let NOAATestTemps = [];
            for(let i = 0; i < siteDates.length; i++){
                //Find a NOAA temperature value within an hour of this value
                //Binary search through NOAA temp to find index value for comparison
                let x = findCloseVal(NOAAdates,0, NOAAdates.length, siteDates[i].getTime())
                NOAATestTemps.push(NOAAtempF[x]);

                if(Math.abs(NOAAtempF[x] - siteTempF[i]) > 30 ){ //if more than 30 degree difference count as anamolies
                    anomalyDate.push(siteDates[i]);
                    anomalyTemp.push(siteTemp[i]);
                    anomalyTempF.push(siteTempF[i]);
                } else {
                    anomalyDate.push(siteDates[i]);
                    anomalyTemp.push(null);
                    anomalyTempF.push(null);
                }
            }

            function findCloseVal(NOAAdates, l, r, time){
                let start = l;
                let end = r;
                while (start < end) {
                    let mid = Math.floor(((start + (end - 1)) / 2));
                    if(Math.abs(NOAAdates[mid].getTime() - time) < 3600000)//If within an hour use this value
                        return mid;
                    if(NOAAdates[mid].getTime() < time){
                        start = mid + 1;
                    } else {
                        end = mid - 1;
                    }
                }
            }

            anomalyDates = anomalyDate;
            anomaliesF = anomalyTempF;
            anomaliesC = anomalyTemp;
            buildChart()
        }



        function getSiteData(NOAAdata) {
            $.ajax({
                method: 'get',
                url: "/cocotemp/site/" + siteID + "/temperature.json",
                success: function (data) {

                    data.forEach(function (datum) {
                        dates.push(new Date(datum['dateTime']));
                        temperature.push(datum['temperature'].toFixed(1));
                        tempF.push((datum['temperature']*(9/5)+32).toFixed(1));
                    });
                 getNOAATemp();
                }
            });
        };




        function getNOAATemp(){
            /*Check to see if site has any data to begin with. If site has no data don't
             begin comparison*/
            if(dates.length == 0){
                buildChart();
            }
            $.ajax({
                 method: 'post',
                  url: "/cocotemp/site/"+siteID+"/info.json",
                success: function (data) {
                    siteData = data;
                    return getClosestNOAA(data);
                }
            });
        }

        function getNOAATemperatureHelper(site) {

            let station = site.stations[0].id;

            let startDateTemp = dates[dates.length-1].toString().split(' ');
            let endDateTemp = dates[0].toString().split( ' ');


            function monthToNum(month){
                switch (month) {
                    case "Jan":
                        return "01";
                    case "Feb":
                        return "02";
                    case "Mar":
                        return "03";
                    case "Apr":
                        return "04";
                    case "May":
                        return "05";
                    case "Jun":
                        return "06";
                    case "Jul":
                        return "07";
                    case "Aug":
                        return "08";
                    case "Sep":
                        return "09";
                    case "Oct":
                        return "10";
                    case "Nov":
                        return "11";
                    case "Dec":
                        return "12";
                }
            }

            let startDate = startDateTemp[3]+"-"
                +monthToNum(startDateTemp[1])+"-"
                +startDateTemp[2].padStart(2,"0")
                // +"T"+startDateTemp[4]
                // +encodeURIComponent("-")
                // +"04:00";


            let endDate = endDateTemp[3]+"-"
                +monthToNum(endDateTemp[1])+"-"
                +endDateTemp[2].padStart(2,"0");
                // +"T"+endDateTemp[4]
                // + encodeURIComponent("-")
                // +"04:00";

            //Check if there is only data for one day if start on previous date if so add 1
            if(endDate === startDate){
                let monthNum = parseInt(monthToNum(startDateTemp[1])) - 1;
                let month = monthNum.toString().padStart(2,"0");
                startDate = startDateTemp[3]+"-"
                    +month+"-"
                    +startDateTemp[2];
            }


            $.ajax({
                method: 'get',
                datatype: 'json',
                headers: {"Token": NOAAToken},

                url: 'https://cocotemp-proxy.herokuapp.com/https://www.ncei.noaa.gov/access/services/data/v1?startDate='+startDate+'&endDate='+endDate+'&dataset=global-hourly&dataTypes=TMP&stations='+station+'&format=json&units=metric&includeStationName=1&includeStationLocation=1&includeAttributes=1',
                success: function (data) {
                    if (data.length === 0) {
                        length = 0;
                        return;
                    }
                    var numberOfDataPoints = Object.keys(data).length;
                    length = Object.keys(data).length;
                    let temperature = [];
                    let dates = [];
                    let tempF = [];
                    for (var i = 0; i < numberOfDataPoints; i++) {
                        var tmp = data[i].TMP;
                        var tempSplit = tmp.split(',');
                        var nonconvertedtemp = tempSplit[0];
                        var convertedTemp = nonconvertedtemp / 10;
                        if (convertedTemp != 999.9) {
                            dates.push(new Date(data[i].DATE));
                            temperature.push(parseFloat(convertedTemp.toFixed(1)));
                            tempF.push(parseFloat((convertedTemp * (9 / 5) + 32).toFixed(1)));
                        }
                    }

                    compareNOAA(dates,temperature,tempF);
                }
            });

        }

        /*Uses the nearest NOAA site as comparison to check for anomolous data*/
        function getClosestNOAA(site){
            let siteLong = site.siteLongitude;
            let siteLat = site.siteLatitude

            let {today, year_ago} = getDateRange();

            let north = siteLat + 2;
            let south = siteLat - 2;
            let east = siteLong + 2;
            let west = siteLong - 2;
            let offset = 0;

            $.ajax({
                method: 'get',
                datatype: 'json',
                headers: {"Token": NOAAToken},
                async: true,
                url: 'https://cocotemp-proxy.herokuapp.com/https://www.ncei.noaa.gov/access/services/search/v1/data?dataset=global-hourly&startDate='+year_ago+'&endDate='+today+'&dataTypes=TMP&limit=1000&offset='+offset+'&bbox='+north+','+west+','+south+','+east,
                success: function (data) {
                    let closest = {
                        index: 0,
                        distance: 99999,
                    }
                    for( let i = 0; i < data.count;i++){
                        let lat = data.results[i].location.coordinates[0];
                        let long = data.results[i].location.coordinates[1];

                        let distance = Math.sqrt(Math.pow((siteLong-long),2)- Math.pow((siteLat-lat),2) );

                        if (distance < closest.distance){
                            closest.index = i;
                            closest.distance = distance;
                        }
                    }
                    //return data.results[closest.index];
                    return getNOAATemperatureHelper(data.results[closest.index])
                }
            });
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

            if(anomalyDates.length != 0 && document.getElementById("anomalyMessage") == null){//Check if anamolies were detected and if so display a message explaining it
                let p = document.createElement("P");
                p.id = "anomalyMessage";
                let text = document.createTextNode("The anomalies shown on the graph were found by comparing" +
                    " the data from this site to that of the nearest NOAA site and found a discrepancy of at least 30 degrees Fahrenheit.");
                p.appendChild(text);
                let x = document.getElementById("plot-area")
                x.appendChild(p);
                console.log("appended");
             }


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

                var anomaliesFLine = {
                        hoverinfo: "y+x",
                        visible: true,
                        x: anomalyDates,
                        y: anomaliesF,
                        name: 'site\'s anomalies F',
                        mode: 'lines+markers',
                        type:'scattergl',
                        connectgaps: false
                }

                var anomaliesCLine = {
                    hoverinfo: "none",
                    visible: false,
                    x: anomalyDates,
                    y: anomaliesC,
                    name: 'site\'s anomalies C',
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


                var data = [collectedTempsC, collectedTempF, anomaliesCLine, anomaliesFLine];
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
                Plotly.newPlot('temperature-chart', data, layout,{responsive:true,scrollZoom:true});
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


                var anomaliesFLine = {
                    hoverinfo: "y+x",
                    visible: false,
                    x: anomalyDates,
                    y: anomaliesF,
                    name: 'site\'s anomalies F',
                    mode: 'lines+markers',
                    type:'scattergl',
                    connectgaps: false
                }

                var anomaliesCLine = {
                    hoverinfo: "none",
                    visible: true,
                    x: anomalyDates,
                    y: anomaliesC,
                    name: 'site\'s anomalies C',
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


                var data = [collectedTempsC, collectedTempF, anomaliesCLine];

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
                Plotly.newPlot('temperature-chart', data, layout,{responsive:true,scrollZoom:true});

            }

        }
    }

    _.defer(buildMap);
    _.defer(populateSite);
    _.defer(populateChart);
});
