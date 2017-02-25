jQuery(document).ready(function () {

    //The upload form.
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

    //The infocards.
    function populateInfocards() {
        $.ajax({
            type: 'post',
            url: '/dashboard/data.json',
            success: function (data) {
                $('#device-count').text(data.deviceCount);
                $('#record-count').text(data.recordCount);
                $('#upload-count').text(data.uploadCount)
            },
            error: function (results) {

            }
        });
    }

    function populateChart() {
        var ctx = $('#temperature-chart');
        var myBarChart = new Chart(ctx, {
            type: 'line',
            data: {
                labels: [65, 59, 80, 81, 56, 55, 40, 98, 98, 94, 94, 69, 984, 65, 1, 213, 516, 984, 984, 351, 32, 135, 87, 78],
                datasets: [
                    {
                        label: "Random Data",
                        backgroundColor: 'rgba(5, 204, 255, 0.2)'
                        ,
                        borderColor: 'rgb(5, 204, 255)',
                        borderWidth: 1,
                        data: [65, 59, 80, 81, 56, 55, 40, 98, 98, 94, 94, 69, 984, 65, 1, 213, 516, 984, 984, 351, 32, 135, 87, 78]
                    }
                ]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                scales: {
                    xAxes: [{
                        stacked: true
                    }],
                    yAxes: [{
                        stacked: true
                    }]
                }
            }
        });
    }

    function populateMap() {
        var myMap = L.map('map').setView([51.505, -0.09], 13);
        L.tileLayer('https://api.mapbox.com/styles/v1/cjsumner/ciu0aibyr002p2iqd51spbo9p/tiles/256/{z}/{x}/{y}?access_token=pk.eyJ1IjoiY2pzdW1uZXIiLCJhIjoiY2lmeDhkMDB3M3NpcHUxbTBlZnoycXdyYyJ9.NKtr-pvthf3saPDsRDGTmw', {
            attribution: 'Map data &copy; <a href="http://openstreetmap.org">OpenStreetMap</a> contributors, <a href="http://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>, Imagery © <a href="http://mapbox.com">Mapbox</a>',
            maxZoom: 18,
            id: 'your.mapbox.project.id',
            accessToken: 'your.mapbox.public.access.token'
        }).addTo(myMap);
    }

    /*
     <li>
     <div class="row device">
     <div class="col-lg-12">
     <h1>Houghton Station</h1>
     <h2>Last updated 6 minutes ago</h2>
     </div>
     </div>
     </li>
     */
    function populateDevices() {
        $.ajax({
            type: 'post',
            url: '/dashboard/devices.json',
            success: function (data) {
                if (data.length == 0) {
                    return;
                }
                function appendDevice(id, deviceName) {
                    var $device = $('#device-template').clone();
                    $device.attr({'id': ''});
                    $device.find('.device-name').attr({'href': '/device/' + id}).text(deviceName);
                    $device.find('.device-update').text('Nothing here yet.');
                    $device.appendTo('.device-list');
                    $device.show();
                }

                for (var i = 0; i < data.length; i++) {
                    appendDevice(data[i].id, data[i].deviceName);
                }
            },
            error: function (results) {

            }
        });
    }

    _.defer(populateMap);
    _.defer(populateChart);
    // _.defer(populateInfocards);
    _.defer(populateDevices);
});