$(function () {
    // JavaScript to automate acknowledgements, so they don't need to be put in one at a time. To add someone, make a new element in the array, insert their name and their position.
    // Author: Trevor Murphy
    text = '';
    var people = [
        ['Dr. Ben Ruddell', 'Project Director'],
        ['Dr. Robert Pastel', 'Development Lead'],
        ['Payton Dunning', 'Team Lead / Web Developer'],
        ['Trevor Murphy', 'Web Developer'],
        ['Piper Dougherty', 'Web Developer'],
        ['Justin Havely', 'Web Developer'],
        ['Caden Sumner', 'Web Developer'],
        ['Jake Schmitz', 'Web Developer'],
        ['Kyle Bray', 'Web Developer / Sensor Developer'],
        ['Nick Lanter', 'Sensor Developer'],
        ['Gary Lord', 'Sensor Developer'],
        ['Paul Esch-Laurent', 'Sensor Developer'],
        ['Connor Gessner', 'Circuits Developer'],
        ['Paul Bahle', 'Human Factors Specialist']
    ];
    for ( i = 0; i < people.length; i++ ) {
        if (i % 3 == 0) {
            text += '</div><div class="row">';
        }
        text += '<div class="col-lg-4"> <div class="panel-panel-default"> <div class="panel-body"> <div class="about"> <h2>' + people[i][0] + '<small> ' + people[i][1] + '</small></h2> <hr> </div> </div> </div> </div>';
    }
    document.getElementById('acknowledgeStart').innerHTML = text;
});