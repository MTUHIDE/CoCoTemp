$(function () {
    var $searchForm = $('#search-form');
    $searchForm.submit(function (event) {
        var searchQuery = $('#search-query').val();
        location.href = '/search?type=site&query=' + searchQuery;
        event.preventDefault();
    })
});