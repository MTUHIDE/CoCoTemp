/**
 * Created by dough on 10/27/2016.
 */
jQuery(document).ready(function () {

    $('#upload-form').submit(function (e) {
        e.preventDefault();

        var url = "/" + $('#device-select').val() + "/upload";
        var fileInput = $('#file');
        var file;
        fileInput.change(function () {
            file = this.files[0];
        });

        var form = new FormData($('#upload-form')[0]);

        var beforeHandler = function () {

            }, successHandler = function () {

            },
            errorHandler = function () {

            };
        $.ajax({
            data: form,
            type: 'post',
            xhr: function () {
                var myXhr = $.ajaxSettings.xhr();
                if (myXhr.upload) {
                    myXhr.upload.addEventListener('progress', progressHandlingFunction, false)
                }
                return myXhr;
            },
            url: url,
            beforeSend: beforeHandler,
            success: successHandler,
            error: errorHandler,
            cache: false,
            contentType: false,
            processData: false
        });

        function progressHandlingFunction(e) {
            if (e.lengthComputable) {
                $('#progress').attr({value: e.loaded, max: e.total})
            }
        }
    })


});