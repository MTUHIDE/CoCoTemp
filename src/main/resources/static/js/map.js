/**
 * Created by caden on 10/7/2016.
 */

var maxBounds = L.latLngBounds(
    L.latLng(5.499550, -167.276413), //Southwest
    L.latLng(83.162102, -52.233040)  //Northeast
);

/* Initialize Map */
var map = L.map('bgmap', {
    dragging: true,
    zoomControl: false
});
map.setView([35, -100], 5);
map.setMaxBounds(maxBounds);

//Because Piper needed zoom buttons
L.control.zoom({
    position:'topright'
}).addTo(map);

/* Style layer from MapBox */
L.tileLayer('https://api.mapbox.com/styles/v1/cjsumner/ciu0aibyr002p2iqd51spbo9p/tiles/256/{z}/{x}/{y}?access_token=pk.eyJ1IjoiY2pzdW1uZXIiLCJhIjoiY2lmeDhkMDB3M3NpcHUxbTBlZnoycXdyYyJ9.NKtr-pvthf3saPDsRDGTmw',
    {
        maxZoom: 18
    }).addTo(map);

/* Add Weather Station Points */
L.marker([47.11, -88.54]).addTo(map).bindPopup("<b>Station 666</b><br>Michigan Technological University <br>Michigan, 49931");
L.marker([44.75, -85.60]).addTo(map).bindPopup("<b>Station 42</b><br>Traverse City<br>Michigan, 49684");
L.marker([39.69, -105.20]).addTo(map).bindPopup("<b>Station 69</b><br>Denver<br> Colorado, 80123");
