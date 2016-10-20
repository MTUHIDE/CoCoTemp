/**
 * Created by caden on 10/19/2016.
 */

$('document').ready(function () {
//        $('#temperature-table').DataTable();
})

var ctx = document.getElementById('temperature-chart');
var chart = new Chart(ctx, {
    type: 'line',
    data: {
        labels: ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"],
        datasets: [{
            label: "Temperature",
            data: [12.6, 34.6, 34.5, 87.9, 34.6, 24.6, 44.5, 76.5, 34.2, 56.4, 54, 23.8],
            backgroundColor: [
                "rgba(255, 99, 132, 0.2"
            ]
        }]
    },
    options: {
        legend: {
            display: false
        },
        scales: {
            xAxes: [{
                scaleLabel: {
                    display: true,
                    labelString: "Month"
                },
                ticks: {
                    beginAtZero: true
                }
            }],
            yAxes: [{
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
})