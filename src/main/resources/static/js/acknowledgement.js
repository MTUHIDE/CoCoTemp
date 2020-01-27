$(function () {
    // JavaScript to automate acknowledgements, so they don't need to be put in one at a time. To add someone, make a new element in the array, insert their name and their position.
    // Author: Trevor Murphy
    text = '';
    var people = [
        ['Dr. Ben Ruddell', 'Project Director'],
        ['Dr. Robert Pastel', 'Development Lead'],
        ['Paul Bahle', 'Former Human Factors Specialist'],
        ['Tom Berg','Web Developer (2019-present)'],
        ['Kyle Bray', 'Former Web/Sensor Developer (2016-2018)'],
        ['Payton Dunning', 'Former Web Developer (2017-2018)'],
        ['Piper Dougherty', 'Former Web Developer (2016-2017)'],
        ['Paul Esch-Laurent', 'Former Sensor/Web Developer (2016-2018)'],
        ['Connor Gessner', 'Former Circuits Developer'],
        ['Justin Havely', 'Former Web Developer (2017-2018)'],
        ['Alex Israels','Former Sensor Developer (2018-2019)'],
        ['Alejandro Jeronimo Ayala Perez','Former Web Developer (2018-2019)'],
        ['Zachary Johnson','Former Web Developer (2018-2019)'],
        ['Will Kirkconnell','Former Web Developer (2018-2019)'],
        ['Nick Lanter', 'Former Sensor Developer (2016-2017)'],
        ['Gary Lord', 'Former Sensor Developer'],
        ['Caleb Melnychenko','Web Developer (2018-present)'],
        ['Trevor Murphy', 'Former Web Developer (2017-2018)'],
        ['Abby Myers', 'Web Developer (2020-present)'],
        ['Collin Palmer','Web/Sensor Developer (2018-present)'],
        ['Cody Reightley', 'Web Developer (2019-present)'],
        ['Mason Sayles','Web Developer (2019-present)'],
       ['Jake Schmitz', 'Former Web Developer'],
        ['Benjamin Slade','Former Web Developer (2018-2019)'],
        ['Caden Sumner', 'Former Web Developer (2016-2017)'],
        ['Reo Tang','Former Web Developer (2018-2019)'],
        ['Brendan Zondlak','Former Sensor Developer (2018-2019)']
    ];
    for ( i = 0; i < people.length; i++ ) {
        if (i % 3 == 0) {
            text += '</div><div class="row">';
        }
        text += '<div class="col-lg-4"> <div class="panel-panel-default"> <div class="panel-body"> <div class="about"> <h2>' + people[i][0] + '<br/><small> ' + people[i][1] + '</small></h2> <hr /> </div> </div> </div> </div>';
    }
    document.getElementById('acknowledgeStart').innerHTML = text;
});