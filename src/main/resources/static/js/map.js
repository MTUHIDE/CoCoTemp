/**
 * Created by caden on 10/7/2016.
 */

var mymap = L.map('mapid').setView([51.505, -0.09], 13);
L.tileLayer('https://api.mapbox.com/styles/v1/cjsumner/citzeefn400012hqh2y2eo2gv/tiles/256/{z}/{x}/{y}?access_token=pk.eyJ1IjoiY2pzdW1uZXIiLCJhIjoiY2lmeDhkMDB3M3NpcHUxbTBlZnoycXdyYyJ9.NKtr-pvthf3saPDsRDGTmw}', {
    attribution: 'Map data &copy; <a href="http://openstreetmap.org">OpenStreetMap</a> contributors, <a href="http://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>, Imagery © <a href="http://mapbox.com">Mapbox</a>',
    maxZoom: 18,
    accessToken: 'pk.eyJ1IjoiY2pzdW1uZXIiLCJhIjoiY2lmeDhkMDB3M3NpcHUxbTBlZnoycXdyYyJ9.NKtr-pvthf3saPDsRDGTmw}'
}).addTo(mymap);