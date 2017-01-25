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
        var ctx = $('#upload-history-chart');
        var myBarChart = new Chart(ctx, {
            type: 'bar',
            data: {
                labels: ["January", "February", "March", "April", "May", "June", "July"],
                datasets: [
                    {
                        label: "My First dataset",
                        backgroundColor: [
                            'rgba(255, 99, 132, 0.2)',
                            'rgba(54, 162, 235, 0.2)',
                            'rgba(255, 206, 86, 0.2)',
                            'rgba(75, 192, 192, 0.2)',
                            'rgba(153, 102, 255, 0.2)',
                            'rgba(255, 159, 64, 0.2)'
                        ],
                        borderColor: [
                            'rgba(255,99,132,1)',
                            'rgba(54, 162, 235, 1)',
                            'rgba(255, 206, 86, 1)',
                            'rgba(75, 192, 192, 1)',
                            'rgba(153, 102, 255, 1)',
                            'rgba(255, 159, 64, 1)'
                        ],
                        borderWidth: 1,
                        data: [65, 59, 80, 81, 56, 55, 40]
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
        var mymap = L.map('map').setView([51.505, -0.09], 13);
        L.tileLayer('https://api.mapbox.com/styles/v1/cjsumner/ciu0aibyr002p2iqd51spbo9p/tiles/256/{z}/{x}/{y}?access_token=pk.eyJ1IjoiY2pzdW1uZXIiLCJhIjoiY2lmeDhkMDB3M3NpcHUxbTBlZnoycXdyYyJ9.NKtr-pvthf3saPDsRDGTmw', {
            attribution: 'Map data &copy; <a href="http://openstreetmap.org">OpenStreetMap</a> contributors, <a href="http://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>, Imagery © <a href="http://mapbox.com">Mapbox</a>',
            maxZoom: 18,
            id: 'your.mapbox.project.id',
            accessToken: 'your.mapbox.public.access.token'
        }).addTo(mymap);
    }

    _.defer(populateMap);;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
    _.defer(populateChart);;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
    _.defer(populateInfocards)
});