$(function () {
    var markersGroup =null;
    //Search form
    var $searchForm = $('#search-form');
    $searchForm.submit(function (event) {
        var searchQuery = $('#search-query').val();
        location.href = '/cocotemp/search?type=site&query=' + searchQuery;
        event.preventDefault();
    });

    var myMap = createMap();
    $('#basemaps').on('change', function() {
        changeBasemap(myMap, this.value);
        markersGroup.addTo(myMap);
    });
    markersGroup = new L.MarkerClusterGroup({
        maxClusterRadius: function(zoom) { return 50; }
    });
    markersGroup.addTo(myMap);

    populateSites(myMap,markersGroup);
});


