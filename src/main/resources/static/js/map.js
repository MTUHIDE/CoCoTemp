/* Limit map view to the USA */
var maxBounds = L.latLngBounds(
    L.latLng(5.090944175, -172.44140625), //Southwest
    L.latLng(71.8014103014, -32.16796875)  //Northeast
);

/* Initialize Map */
var map = L.map('map', {
    dragging: true,
    zoomControl: false,
    maxZoom: 18,
    minZoom: 4
});
map.setView([35, -100], 5);
map.setMaxBounds(maxBounds);
map.setZoom(0);

//Because Piper needed zoom buttons
L.control.zoom({
    position: 'topright'
}).addTo(map);

/* Style layer from MapBox */
L.tileLayer('https://api.mapbox.com/styles/v1/cjsumner/ciu0aibyr002p2iqd51spbo9p/tiles/256/{z}/{x}/{y}?access_token=pk.eyJ1IjoiY2pzdW1uZXIiLCJhIjoiY2lmeDhkMDB3M3NpcHUxbTBlZnoycXdyYyJ9.NKtr-pvthf3saPDsRDGTmw',
    {}).addTo(map);

var addPoints = function (data) {
    L.geoJSON(data, {
        onEachFeature: onEachFeature,
        pointToLayer: function (feature, latlng) {
            var temperature = feature.properties.temperature;
            var color;
            if (temperature < 30) {
                color = "#0f22ff"
            } else if (temperature >= 30 && temperature < 40) {
                color = "#2442ff"
            } else if (temperature >= 40 && temperature < 50) {
                color = "#4fbafb"
            } else if (temperature >= 50 && temperature < 60) {
                color = "#7affbf"
            } else if (temperature >= 60 && temperature < 70) {
                color = "#c0ff28"
            } else if (temperature >= 70 && temperature < 80) {
                color = "#f7ff0d"
            } else if (temperature >= 80 && temperature < 90) {
                color = "#ff6d00"
            } else if (temperature >= 90) {
                color = "#ff1200"
            }
            var geoJsonMarkerOptions = {
                radius: 8,
                fillColor: color,
                color: "#000",
                weight: 1,
                opacity: 1,
                fillOpacity: 0.8
            };
            return L.circleMarker(latlng, geoJsonMarkerOptions)
        }
    }).addTo(map);
};

var onEachFeature = function (feature, layer) {
    var stationName = feature.properties.name;
    var temperature = feature.properties.temperature;
    var popup = "<p>Station: " + stationName + "</p><h4>" + temperature + "</h4>";
    layer.bindPopup(popup);
};


$.ajax(
    {
        url: '/cocotemp/sitePoints.json',
        dataType: "json",
        method: "post",
        success: addPoints
    }
);