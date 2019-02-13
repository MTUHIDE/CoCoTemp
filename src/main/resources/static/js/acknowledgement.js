$(function () {
    // JavaScript to automate acknowledgements, so they don't need to be put in one at a time. To add someone, make a new element in the array, insert their name and their position.
    // Author: Trevor Murphy
    text = '';
    var people = [
        ['Dr. Ben Ruddell', 'Project Director'],
        ['Dr. Robert Pastel', 'Development Lead'],
        ['Paul Bahle', 'Former Human Factors Specialist'],
        ['Kyle Bray', 'Former Web/Sensor Developer'],
        ['Payton Dunning', 'Former Web Developer'],
        ['Piper Dougherty', 'Former Web Developer'],
        ['Paul Esch-Laurent', 'Former Sensor Developer'],
        ['Connor Gessner', 'Former Circuits Developer'],
        ['Justin Havely', 'Former Web Developer'],
        ['Alex Israels','Sensor Developer'],
        ['Alejandro Jeronimo Ayala Perez','Web Developer'],
        ['Zachary Johnson','Web Developer'],
        ['Will Kirkconnell','Web Developer'],
        ['Nick Lanter', 'Former Sensor Developer'],
        ['Gary Lord', 'Former Sensor Developer'],
        ['Trevor Murphy', 'Former Web Developer'],
        ['Collin Palmer','Web/Sensor Developer'],
        ['Jake Schmitz', 'Former Web Developer'],
        ['Benjamin Slade','Web Developer'],
        ['Caden Sumner', 'Former Web Developer'],
        ['Reo Tang','Web Developer'],
        ['Brendan Zondlak','Sensor Developer']
    ];
    for ( i = 0; i < people.length; i++ ) {
        if (i % 3 == 0) {
            text += '</div><div class="row">';
        }
        text += '<div class="col-lg-4"> <div class="panel-panel-default"> <div class="panel-body"> <div class="about"> <h2>' + people[i][0] + '<br/><small> ' + people[i][1] + '</small></h2> <hr /> </div> </div> </div> </div>';
    }
    document.getElementById('acknowledgeStart').innerHTML = text;
});