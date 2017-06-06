/**
 * Created by dough on 2017-03-05.
 */
jQuery(document).ready(function () {

    var $location = document.getElementById('location-query');
    var autocomplete = new google.maps.places.Autocomplete($location);
    autocomplete.addListener("place_changed", function () {
        var location = autocomplete.getPlace().geometry.location;
        $("#latitude-query").val(location.lat());
        $("#longitude-query").val(location.lng());
        $("#human-readable").val(autocomplete.getPlace().formatted_address);
    });

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

    var locationParameterValue = getUrlParameter("location");
    if (locationParameterValue != null) {
        var splitQuery = locationParameterValue.split(",");
        $("#latitude-query").val(splitQuery[0]);
        $("#longitude-query").val(splitQuery[1]);
    }


    var $searchForm = $("#search-form");

    $('#location-query').bind('keypress keydown keyup', function (e) {
        if (e.keyCode == 13) {
            e.preventDefault();
        }
    });

    $searchForm.submit(function (event) {

        event.preventDefault();

        var $search = $("#search-query");
        var $latitudeField = $("#latitude-query");
        var $longitudeField = $("#longitude-query");

        if ($latitudeField.val().length === 0 && $longitudeField.val().length != 0) {
            $(".location-group").addClass('has-error');
            return;
        }
        if ($longitudeField.val().length === 0 && $latitudeField.val().length != 0) {
            $(".location-group").addClass('has-error');
            return;
        }

        var startURL = "/cocotemp/search?";


        var $latitude = $latitudeField.val();
        var $longitude = $longitudeField.val();

        var queryObject = {};
        if ($search.val().length > 0) {
            queryObject.query = $search.val().trim();
        }
        if ($latitude.length > 0 || $longitude.length > 0) {
            queryObject.location = $latitude + "," + $longitude;
            queryObject.range = $("#range-selector").val();
        }

        queryObject.type = 'site';
        startURL += $.param(queryObject);
        window.location = startURL;
    })
});