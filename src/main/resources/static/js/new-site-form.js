/**
 * Created by dough on 2017-02-09.
 */
$(function () {

    var latitudeInput = $('#siteLatitude');
    var longitudeInput = $('#siteLongitude');
    var elevationInput = $('#siteElevation');
    function initMap() {

        var address = document.getElementById('address');
        var autocomplete = new google.maps.places.Autocomplete(address);

        var myMap = createMap();
        myMap.on('click', function(e) {
            latitudeInput.val(e.latlng.lat);
             longitudeInput.val(e.latlng.lng);

             markLocation(myMap, latitudeInput.val(), longitudeInput.val());

                var api_url = 'http://dev.virtualearth.net/REST/v1/Elevation/List?points';
                var accessToken = "Al7kNYJcdyzu9MBbi84YDdobFbm1mBFvMEYcHaxldkziSxR1_7C-RUh07pWYukCv ";
                $.ajax({
                    url: api_url+"="+latitudeInput.val()+","+longitudeInput.val() ,
                    method:"GET",
                    dataType: "json",
                    data:{
                        "key":accessToken
                    },
                    success:function (data) {
                        elevationInput.val(data.resourceSets[0].resources[0].elevations[0]);
                    },
                    error: function () {
                        console.log("Error");
                    }

                });

            }


        );

        window.logResults = function(response){
            console.log("success");

        }
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
            var api_url = 'http://dev.virtualearth.net/REST/v1/Elevation/List?points';
            var accessToken = "ArbP9oed248huqmuMHHBT9ITH08B-LCRylqHnMgEZMHCAJO-TnmS5HxCzEysLckD";
            $.ajax({
                url: api_url+"="+latitudeInput.val()+","+longitudeInput.val() ,
                method:"GET",
                dataType: "json",
                data:{
                    "key":accessToken
                },
                success:function (data) {
                    elevationInput.val(data.resourceSets[0].resources[0].elevations[0]);
                },
                error: function () {
                    console.log("Error");
                }

            });
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