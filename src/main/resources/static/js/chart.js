/**
 * Created by caden on 10/19/2016.
 */

$('document').ready(function () {
    $('#temperature-table').DataTable({
        "order": [[0, "desc"]]
    });
    var ctx = document.getElementById('temperature-chart');
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

})
;