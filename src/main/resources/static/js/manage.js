/**
 * Created by dough on 10/28/2016.
 */
jQuery(document).ready(function ($) {

    //Initialize location search box.
    var input = document.getElementById('device-location');
    var autocomplete = new google.maps.places.Autocomplete(input);
    google.maps.event.addDomListener(autocomplete, 'load', function () {

    });

    $("#form").submit(function (e) {
        e.preventDefault();

        var $deviceName = $("#device-name").val();
        var $deviceLocation = $("#device-location").val();
        var data = "deviceName=" + $deviceName + "&deviceLocation=" + $deviceLocation;
        $.ajax({
            data: data,
            dataType: 'json',
            timeout: 1000,
            type: 'post',
            url: '/manage/devices/add'
        }).done(function (data) {
            console.log(JSON.stringify(data));
            if (data['error'] == false) {
                location.href = "/manage"
            }
        });
    })
});