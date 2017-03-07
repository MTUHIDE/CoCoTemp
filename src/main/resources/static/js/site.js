jQuery(document).ready(function () {

    function populateChart() {
        var dates = [], temperature = [];

        $.ajax("/site/" + siteID + "/temperature.json", {
            method: 'post',
            success: function (data) {
                var i = 0;
                data.forEach(function (datum) {
                    dates.push(datum['dateTime']);
                    temperature.push(datum['temperature']);
                });
                buildChart(dates, temperature);
            }
        });

        function buildChart(dates, temperature) {
            var ctx = $('#temperature-chart');
            var myBarChart = new Chart(ctx, {
                type: 'line',
                data: {
                    labels: dates,
                    datasets: [
                        {
                            label: "Temperature",
                            backgroundColor: 'rgba(5, 204, 255, 0.3)'
                            ,
                            borderColor: 'rgb(5, 204, 255)',
                            // borderWidth: 1,
                            data: temperature
                        }
                    ]
                },
                options: {
                    bezierCurve: true,
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
                        }]
                    }
                }
            });
        }
    }

    function populateMap() {

        $.ajax("/site/" + siteID + "/info.json", {
            method: 'post',
            success: function (data) {
                createMap(data)
            }

        });

        function createMap(data) {
            var myMap = L.map('map').setView([51.505, -0.09], 13);
            L.tileLayer('https://api.mapbox.com/styles/v1/cjsumner/ciu0aibyr002p2iqd51spbo9p/tiles/256/{z}/{x}/{y}?access_token=pk.eyJ1IjoiY2pzdW1uZXIiLCJhIjoiY2lmeDhkMDB3M3NpcHUxbTBlZnoycXdyYyJ9.NKtr-pvthf3saPDsRDGTmw', {
                attribution: 'Map data &copy; <a href="http://openstreetmap.org">OpenStreetMap</a> contributors, <a href="http://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>, Imagery © <a href="http://mapbox.com">Mapbox</a>',
                maxZoom: 18,
                id: 'your.mapbox.project.id',
                accessToken: 'your.mapbox.public.access.token'
            }).addTo(myMap);


            var marker = L.marker([data.siteLatitude, data.siteLongitude]).addTo(myMap);
            var group = L.featureGroup([marker]);
            myMap.fitBounds(group.getBounds());
        }
    }


    _.defer(populateMap);
    _.defer(populateChart);
});