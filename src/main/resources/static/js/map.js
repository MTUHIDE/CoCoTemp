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
map.setView([40, -85], 6);
map.setMaxBounds(maxBounds);

/* Style layer from MapBox */
L.tileLayer('https://api.mapbox.com/styles/v1/cjsumner/citzzhm4m002f2in5jfc102im/tiles/256/{z}/{x}/{y}?access_token=pk.eyJ1IjoiY2pzdW1uZXIiLCJhIjoiY2lmeDhkMDB3M3NpcHUxbTBlZnoycXdyYyJ9.NKtr-pvthf3saPDsRDGTmw',
    {
        maxZoom: 18
    }).addTo(map);
