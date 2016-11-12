jQuery(document).ready(function () {

    var maxBounds = L.latLngBounds(
        L.latLng(5.090944175, -172.44140625), //Southwest
        L.latLng(71.8014103014, -32.16796875)  //Northeast
    );

    /* Initialize Map */
    var map = L.map('map', {
        dragging: true,
        maxZoom: 18,
        minZoom: 4
    });

    map.setView([35, -100], 5);
    map.setMaxBounds(maxBounds);
    map.setZoom(0);

    /* Style layer from MapBox */
    L.tileLayer('https://api.mapbox.com/styles/v1/cjsumner/ciu0aibyr002p2iqd51spbo9p/tiles/256/{z}/{x}/{y}?access_token=pk.eyJ1IjoiY2pzdW1uZXIiLCJhIjoiY2lmeDhkMDB3M3NpcHUxbTBlZnoycXdyYyJ9.NKtr-pvthf3saPDsRDGTmw',
        {}).addTo(map);

    var addPoints = function (data) {
    };

    $('#temperature-table').DataTable({
        'ajax': '/dataPoints.json',
        'serverSide': true,
        responsive: true,
        columns: [{
            data: 'dateTime'
        }, {
            data: 'temperature'
        }]
    });
});