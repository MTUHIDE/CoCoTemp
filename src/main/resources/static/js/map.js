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
// L.marker([47.11, -88.54]).addTo(map).bindPopup("<b>Station 666</b><br>Michigan Technological University<br>Michigan, 49931");
// L.marker([44.75, -85.60]).addTo(map).bindPopup("<b>Station 42</b><br>Traverse City<br>Michigan, 49684");
// L.marker([39.69, -105.20]).addTo(map).bindPopup("<b>Station 69</b><br>Denver<br> Colorado, 80123");

function onEachFeature(feature, layer) {
    // does this feature have a property named popupContent?
        layer.bindPopup(feature.properties.popupContent);
}

/* GeoJSON Data */
$.getJSON('https://gist.githubusercontent.com/Ghosts/9cee42dbb5275c5ae15d5cc5e733eab2/raw/02c0df960d62ac698c2df0fd334155f0df56a27d/geoTest.json', function (geojson) {
    L.geoJson(geojson, {
        onEachFeature: onEachFeature
    }).addTo(map);
});