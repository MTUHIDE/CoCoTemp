/**
 * Created by dough on 2017-02-09.
 */
jQuery(document).ready(function () {

    var latitudeInput = $('#siteLatitude');
    var longitudeInput = $('#siteLongitude');

    function setupForPlaces(input, map) {
        var autocomplete = new google.maps.places.Autocomplete(input);

        // Bind the map's bounds (viewport) property to the autocomplete object,
        // so that the autocomplete requests use the current map bounds for the
        // bounds option in the request.
        autocomplete.bindTo('bounds', map);

        autocomplete.addListener('place_changed', function () {
            var place = autocomplete.getPlace();
            latitudeInput.val(place.geometry.location.lat());
            longitudeInput.val(place.geometry.location.lng());
        });

        var marker = new google.maps.Marker({
            map: map,
            anchorPoint: new google.maps.Point(0, -29),
            draggable:true
        });

        var $locateButton = $('#locateButton');
        $locateButton.click(function () {
            geocodeLocation(map, marker, latitudeInput.val(), longitudeInput.val());
        })

    }

    function geocodeLocation(map, marker, latitudeInput, longitudeInput) {

        if (latitudeInput === "" || longitudeInput === "") {
            return;
        }
        marker.setVisible(false);
        var latlng = {lat: parseFloat(latitudeInput), lng: parseFloat(longitudeInput)};
        marker.setPosition(latlng);
        map.setCenter(latlng);
        marker.setVisible(true);
    }

    function initMap() {
        var map = new google.maps.Map(document.getElementById('map'), {
            center: {lat: -33.8688, lng: 151.2195},
            zoom: 13,
            streetViewControl: false
        });
        var input = document.getElementById('locationInput');
        setupForPlaces(input, map);
    }

    _.defer(initMap)

});
