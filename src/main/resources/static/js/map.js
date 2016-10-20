/**
 * Created by caden on 10/7/2016.
 */

/* Limit map view to the USA */
var maxBounds = L.latLngBounds(
    L.latLng(5.090944175, -172.44140625), //Southwest
    L.latLng(71.8014103014, -32.16796875)  //Northeast
);

/* Initialize Map */
var map = L.map('bgmap', {
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

/* Add Weather Station Points */
$.getJSON("https://gist.githubusercontent.com/Ghosts/9cee42dbb5275c5ae15d5cc5e733eab2/raw/8563d2ca75d36edbe52b8d1dec309164fc0ac29b/geoTest.geojson", function (data) {
    L.geoJson(data, {
        onEachFeature: function (feature, layer) {
            layer.bindPopup("test");
        }
    }).addTo(map);
});