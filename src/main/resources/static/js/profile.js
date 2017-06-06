jQuery(document).ready(function ($) {

    //Initialize location search box.
    var input = document.getElementById('site-location');
    var autocomplete = new google.maps.places.Autocomplete(input);
    google.maps.event.addDomListener(autocomplete, 'load', function () {
    });

    $("#form").submit(function (e) {
        e.preventDefault();
        var $siteName = $("#site-name").val();

        var place = autocomplete.getPlace();

        var data = "siteName=" + $siteName +
            "&siteLatitude=" + place.geometry.location.lat()
            + "&siteLongitude=" + place.geometry.location.lng();

        $('#form-spinner').css("display", "inline-block");

        $.ajax({
            data: data,
            dataType: 'json',
            type: 'post',
            url: '/cocotemp/manage/sites/add'
        }).done(function (data) {
            console.log(JSON.stringify(data));
            if (data['error'] == false) {
                window.location.href = "/cocotemp/manage"
            }
        });
    })
});