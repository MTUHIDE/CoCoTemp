(function ($) {
    $.fn.spin = function (opts, color) {
        var presets = {
            "tiny": {
                lines: 8,
                length: 2,
                width: 2,
                radius: 3
            },
            "small": {
                lines: 8,
                length: 4,
                width: 3,
                radius: 5
            },
            "large": {
                lines: 10,
                length: 8,
                width: 4,
                radius: 8
            }
        };
        if (Spinner) {
            return this.each(function () {
                var $this = $(this),
                    data = $this.data();

                if (data.spinner) {
                    data.spinner.stop();
                    delete data.spinner;
                }
                if (opts !== false) {
                    if (typeof opts === "string") {
                        if (opts in presets) {
                            opts = presets[opts];
                        } else {
                            opts = {};
                        }
                        if (color) {
                            opts.color = color;
                        }
                    }
                    data.spinner = new Spinner($.extend({
                        color: $this.css('color')
                    }, opts)).spin(this);
                }
            });
        } else {
            throw "Spinner class not available.";
        }
    };
})(jQuery);

jQuery(document).ready(function () {

    $('#device-list li').each(function (i, e) {

        var BeforeSend = function () {

            $(e).spin();
        };
        var OnComplete = function (jqXHR, textStatus) {

        };
        var OnSuccess = function (data, textStatus, jqXHR) {

            if (data.length > 0) {
                var error = false;
                var newRecord = false;
                for (var i = 0; i < data.length; i++) {
                    var record = data[i];
                    if (!record.viewed) {
                        newRecord = true;
                    }
                    if (record.error) {
                        error = true;
                    }
                }

                $(e).spin(false);

                if (error && newRecord) {
                    $(e).find('.data-container').append(
                        '<span class="label label-danger">Error Uploading</span>'
                    )
                } else if (!error && newRecord) {
                    $(e).find('.data-container').append(
                        '<span class="label label-success">Upload Successful</span>'
                    )
                }
                var date = moment(data[0].dateTime);
                $(e).find('#last-updated').text("Last uploaded " + date.fromNow())
            } else {
                $(e).spin(false);
                $(e).find('#last-updated').text("No records")
            }

        };
        var OnError = function (qXHR, textStatus, errorThrown) {

        };

        $.ajax({
            url: "/device/" + e.id + "/history.json",
            method: 'POST',
            dataType: 'json',
            beforeSend: BeforeSend,
            complete: OnComplete,
            success: OnSuccess,
            error: OnError
        });
    });

    $('#upload-form').submit(function (e) {
        e.preventDefault();
        var url = "/upload/" + $('#device-select').val();
        var fileInput = $('#file');
        var file;
        fileInput.change(function () {
            file = this.files[0];
        });

        var form = new FormData($('#upload-form')[0]);

        var beforeHandler = function () {

            }, successHandler = function () {
                $('#close').trigger('click');
                var toasterSuccessOptions = {
                    "closeButton": true,
                    "debug": false,
                    "newestOnTop": false,
                    "progressBar": true,
                    "positionClass": "toast-top-right",
                    "preventDuplicates": true,
                    "onclick": null,
                    "showDuration": "300",
                    "hideDuration": "1000",
                    "timeOut": "5000",
                    "extendedTimeOut": "1000",
                    "showEasing": "swing",
                    "hideEasing": "linear",
                    "showMethod": "fadeIn",
                    "hideMethod": "fadeOut"
                };
                toastr.options = toasterSuccessOptions;
                toastr["success"]("Your data is being uploaded and will be available soon.", "Upload")
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

        }
    });
    $('#temperature-table').DataTable({
        'ajax': '/data',
        'serverSide': true,
        responsive: true,
        columns: [{
            data: 'dateTime'
        }, {
            data: 'temperature'
        }]
    });
    var ctx = $('#temperature-chart');
    var chart = new Chart(ctx, {
        type: 'line',
        data: {
            labels: ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"],
            datasets: [{
                label: "Temperature",
                fill: true,
                data: [12.6, 34.6, 34.5, 87.9, 34.6, 24.6, 44.5, 76.5, 34.2, 56.4, 54, 23.8],
                borderColor: "#79D0B3",
                backgroundColor: "rgba(121, 208, 179, 0.2)"
            }]
        },
        options: {
            tooltips: {
                enabled: true,
                mode: 'label'

            }, hover: {
                mode: 'label'
            },
            legend: {
                display: false
            },
            scales: {
                xAxes: [{
                    gridLines: {
                        display: false
                    },
                    scaleLabel: {
                        display: true,
                        labelString: "Month"
                    },
                    ticks: {
                        beginAtZero: true
                    }
                }],
                yAxes: [{
                    gridLines: {
                        display: true
                    },
                    scaleLabel: {
                        display: true,
                        labelString: "Temperature"
                    },
                    ticks: {
                        beginAtZero: true
                    }
                }]
            }
        }
    });


});