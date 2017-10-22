$(function () {

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
    });
    populateSites(myMap);
});


