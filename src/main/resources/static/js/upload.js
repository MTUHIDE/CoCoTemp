/**
 * Created by dough on 10/27/2016.
 */
jQuery(document).ready(function () {

    $('#upload-form').submit(function (e) {
        e.preventDefault();
        var selectedValue = $('#device-select').val();
        console.log(selectedValue);
    })

});