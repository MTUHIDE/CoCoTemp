/**
 * Created by dough on 2017-03-05.
 */
jQuery(document).ready(function () {
    var getUrlParameter = function getUrlParameter(sParam) {
        var sPageURL = decodeURIComponent(window.location.search.substring(1)),
            sURLVariables = sPageURL.split('&'),
            sParameterName,
            i;

        for (i = 0; i < sURLVariables.length; i++) {
            sParameterName = sURLVariables[i].split('=');

            if (sParameterName[0] === sParam) {
                return sParameterName[1] === undefined ? true : sParameterName[1];
            }
        }
    };

    var queryParameterValue = getUrlParameter('query');
    if (queryParameterValue != null) {
        $("#search-query").val(queryParameterValue);
    }

    var $searchForm = $("#search-form");
    $searchForm.submit(function (event) {
        var searchQuery = $('#search-query').val();
        location.href = ('/search?query=' + searchQuery);
        event.preventDefault();
    })
});