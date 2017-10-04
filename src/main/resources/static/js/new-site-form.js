/**
 * Created by dough on 2017-02-09.
 */
$(function () {

    var latitudeInput = $('#siteLatitude');
    var longitudeInput = $('#siteLongitude');

    function initMap() {

        var address = document.getElementById('address');
        var autocomplete = new google.maps.places.Autocomplete(address);

        var myMap = createMap();
        myMap.on('click', function(e) {
            latitudeInput.val(e.latlng.lat);
            longitudeInput.val(e.latlng.lng);
            markLocation(myMap, latitudeInput.val(), longitudeInput.val());
        });

        /* User entered autocomplete results */
        autocomplete.addListener('place_changed', function() {
            var place = autocomplete.getPlace();
            if (!place.geometry) {
                /* User entered the name of a Place that was not suggested and pressed the Enter key, or the Place Details request failed. */
                window.alert("No details available for input: '" + place.name + "'");
                return;
            }
            latitudeInput.val(place.geometry.location.lat());
            longitudeInput.val(place.geometry.location.lng());
            markLocation(myMap, place.geometry.location.lat(), place.geometry.location.lng());
        });
    }

    _.defer(initMap)

});

/* Prevent enter key from submitting form */
$(document).ready(function() {
    $(window).keydown(function(event){
        if(event.keyCode == 13) {
            event.preventDefault();
            return false;
        }
    });
});