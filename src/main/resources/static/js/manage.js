/**
 * Created by dough on 10/28/2016.
 */
jQuery(document).ready(function () {

    //Initialize location search box.
    var input = document.getElementById('device-location');
    var autocomplete = new google.maps.places.Autocomplete(input);
    google.maps.event.addDomListener(autocomplete, 'load', function () {

    });

    $('#new-device').click(function () {

    })
});