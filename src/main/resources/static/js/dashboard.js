jQuery(document).ready(function () {

    var myMap;

    //var query = getQueryParams(document.location.search);
    //alert(query.foo);
    function getQueryParams(qs) {
        qs = qs.split('+').join(' ');

        var params = {},
            tokens,
            re = /[?&]?([^=]+)=([^&]*)/g;

        while (tokens = re.exec(qs)) {
            params[decodeURIComponent(tokens[1])] = decodeURIComponent(tokens[2]);
        }

        return params;
    }




    //The infocards.
    function populateInfocards() {
        $.ajax({
            type: 'post',
            url: '/dashboard/data.json',
            success: function (data) {
                $('#device-count').text(data.deviceCount);
                $('#record-count').text(data.recordCount);
                $('#upload-count').text(data.uploadCount)
            },
            error: function (results) {

            }
        });
    }

    function populateChart() {

        var dates = [];
        var values = [];
        var query = getQueryParams(document.location.search);
        var range = query._range;
        console.log(range);

        $.ajax({
            type: 'post',
            url: '/history.json?_range=' + range,
            success: function (data) {
                for (var i = 0; i < data.length; i++) {
                    if (data[i].error === true) {
                        continue;
                    }
                    dates.push(data[i].dateTime);
                    values.push(data[i].records);
                }
                buildChart(dates, values)
            },
            error: function (results) {

            }
        });

        function buildChart(dates, values) {
            var ctx = $('#upload-history-chart');
            var myBarChart = new Chart(ctx, {
                type: 'line',
                data: {
                    labels: dates,
                    datasets: [
                        {
                            label: "Records Uploaded",
                            backgroundColor: 'rgba(5, 204, 255, 0.3)',
                            borderColor: 'rgb(5, 204, 255)',
                            borderWidth: 1,
                            data: values
                        }
                    ]
                },
                options: {
                    responsive: true,
                    maintainAspectRatio: false,
                    scales: {
                        xAxes: [{
                            type: 'time',
                            time: {
                                displayFormats: {
                                    quarter: 'MMM YYYY'
                                }
                            }
                        }],
                        yAxes: [{
                        }]
                    }
                }
            });
        }


    }

    function populateMap() {
        myMap = L.map('map').setView([51.505, -0.09], 13);
        L.tileLayer('https://api.mapbox.com/styles/v1/cjsumner/ciu0aibyr002p2iqd51spbo9p/tiles/256/{z}/{x}/{y}?access_token=pk.eyJ1IjoiY2pzdW1uZXIiLCJhIjoiY2lmeDhkMDB3M3NpcHUxbTBlZnoycXdyYyJ9.NKtr-pvthf3saPDsRDGTmw', {
            attribution: 'Map data &copy; <a href="http://openstreetmap.org">OpenStreetMap</a> contributors, <a href="http://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>, Imagery © <a href="http://mapbox.com">Mapbox</a>',
            maxZoom: 18,
            id: 'your.mapbox.project.id',
            accessToken: 'your.mapbox.public.access.token'
        }).addTo(myMap);
    }

    /*
     <li>
     <div class="row device">
     <div class="col-lg-12">
     <h1>Houghton Station</h1>
     <h2>Last updated 6 minutes ago</h2>
     </div>
     </div>
     </li>
     */
    function populateDevices() {
        var deviceMarkers = [];

        $.ajax({
            type: 'post',
            url: '/dashboard/devices.json',
            success: function (data) {
                if (data.length == 0) {
                    return;
                }
                function appendDevice(data) {
                    var $device = $('#device-template').clone();
                    $device.attr({'id': ''});
                    $device.find('.device-name').attr({'href': '/device/' + data.id}).text(data.deviceName);

                    var description = data.deviceDescription;
                    var length = description.length;
                    if (length > 50) {
                        description = description.substring(0, 50) + "...";
                    }
                    $device.find('.device-update').text(description);
                    $device.appendTo('.device-list');
                    $device.show();
                }

                for (var i = 0; i < data.length; i++) {
                    appendDevice(data[i]);

                    //Add the station locations to the map.
                    var myMarker = L.marker([data[i].deviceLatitude, data[i].deviceLongitude]).addTo(myMap);
                    myMarker.bindPopup("<p>" + data[i].deviceName + "</p>");
                    deviceMarkers.push(myMarker);
                }

                //Fit to show all markers on the map.
                var myGroup = new L.featureGroup(deviceMarkers);
                myMap.fitBounds(myGroup.getBounds())
            },
            error: function (results) {

            }
        });
    }

    _.defer(populateMap);
    _.defer(populateChart);
    _.defer(populateInfocards);
    _.defer(populateDevices);
});