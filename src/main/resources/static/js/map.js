/**
 * Created by caden on 10/7/2016.
 */



/* Initialize Map */
var map = L.map('bgmap');

/* Style layer from MapBox */
L.tileLayer('https://api.mapbox.com/styles/v1/cjsumner/citzzhm4m002f2in5jfc102im/tiles/256/{z}/{x}/{y}?access_token=pk.eyJ1IjoiY2pzdW1uZXIiLCJhIjoiY2lmeDhkMDB3M3NpcHUxbTBlZnoycXdyYyJ9.NKtr-pvthf3saPDsRDGTmw',
    {
        maxZoom: 17,
        minZoom: 9
    }).addTo(map);

L.map('bgmap').setView([51.505, -30.9], 13);
L.map('bgmap').dragging(true);




