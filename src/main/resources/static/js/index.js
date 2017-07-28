$(function () {

    var myMap;

    //Search
    var $searchForm = $('#search-form');
    $searchForm.submit(function (event) {
        var searchQuery = $('#search-query').val();
        location.href = '/cocotemp/search?type=site&query=' + searchQuery;
        event.preventDefault();
    })

    //Creates map object
    function createMap() {
        myMap = L.map('map').setView([37.0902, -95.7129], 4);
        L.tileLayer('https://api.mapbox.com/styles/v1/cjsumner/ciu0aibyr002p2iqd51spbo9p/tiles/256/{z}/{x}/{y}?access_token=pk.eyJ1IjoiY2pzdW1uZXIiLCJhIjoiY2lmeDhkMDB3M3NpcHUxbTBlZnoycXdyYyJ9.NKtr-pvthf3saPDsRDGTmw', {
            attribution: 'Map data &copy; <a href="http://openstreetmap.org">OpenStreetMap</a> contributors, <a href="http://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>, Imagery © <a href="http://mapbox.com">Mapbox</a>',
            maxZoom: 18,
            id: 'your.mapbox.project.id',
            accessToken: 'your.mapbox.public.access.token'
        }).addTo(myMap);
    }

    //Add all site pins to map
    function populateSites() {
        var siteMarkers = [];
        $.ajax({
            method: 'post',
            url: '/cocotemp/sites.json',
            success: function (data) {
                if (data.length == 0) {
                    return;
                }

                for (var i = 0; i < data.length; i++) {
                    //Add the station locations to the map.
                    var myMarker = L.marker([data[i].siteLatitude, data[i].siteLongitude]).addTo(myMap);
                    myMarker.bindPopup("<p>" + data[i].siteName + "</p>");
                    siteMarkers.push(myMarker);
                }
            },
        });
    }

    createMap();
    populateSites();
});


