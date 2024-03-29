$(function () {
    // JavaScript to automate acknowledgements, so they don't need to be put in one at a time. To add someone, make a new element in the array, insert their name and their position.
    // Author: Trevor Murphy
    text = '';
    var people = [
        ['Dr. Ben Ruddell', 'Project Director','Northern Arizona University','NAU'],
        ['Dr. Robert Pastel', 'Development Lead','Michigan Technological University','MTU'],
        ['Paul Bahle', 'Human Factors Specialist (2017-2018)','Michigan Technological University','MTU'],
        ['Tom Berg','Web Developer (2019-present)','Michigan Technological University','MTU'],
        ['Kyle Bray', 'Web/Sensor Developer (2016-2018)','Michigan Technological University','MTU'],
        ['Payton Dunning', 'Web Developer (2017-2018)','Michigan Technological University','MTU'],
        ['Piper Dougherty', 'Web Developer (2016-2017)','Michigan Technological University','MTU'],
        ['Paul Esch-Laurent', 'Sensor/Web Developer (2016-2018)','Michigan Technological University','MTU'],
        ['Connor Gessner', 'Former Circuits Developer','Michigan Technological University','MTU'],
        ['Justin Havely', 'Web Developer (2017-2018)','Northern Arizona University','NAU'],
        ['Alex Israels','Sensor Developer (2018-2019)','Michigan Technological University','MTU'],
        ['Alejandro Jeronimo Ayala Perez','Web Developer (2018-2019)','Michigan Technological University','MTU'],
        ['Zachary Johnson','Web Developer (2018-2019)','Michigan Technological University','MTU'],
        ['Will Kirkconnell','Web Developer (2018-2019)','Michigan Technological University','MTU'],
        ['Nick Lanter', 'Sensor Developer (2016-2017)','Michigan Technological University','MTU'],
        ['Gary Lord', 'Former Sensor Developer','Michigan Technological University','MTU'],
        ['Caleb Melnychenko','Web Developer (2018-2020)','Michigan Technological University','MTU'],
        ['Trevor Murphy', 'Web Developer (2017-2018)','Michigan Technological University','MTU'],
        ['Abby Myers', 'Web Developer (2020-present)','Michigan Technological University','MTU'],
        ['Collin Palmer','Web/Sensor Developer (2018-2020)','Michigan Technological University','MTU'],
        ['Cody Reightley', 'Web Developer (2019-2020)','Michigan Technological University','MTU'],
        ['Lela Root', 'Web Developer (2020-present)','Michigan Technological University','MTU'],
        ['Mason Sayles','Web Developer (2019-present)','Michigan Technological University','MTU'],
       ['Jake Schmitz', 'Former Web Developer','Northern Arizona University','NAU'],
        ['Benjamin Slade','Web Developer (2018-2019)','Michigan Technological University','MTU'],
        ['Caden Sumner', 'Web Developer (2016-2017)','Michigan Technological University','MTU'],
        ['Reo Tang','Web Developer (2018-2019)','Michigan Technological University','MTU'],
        ['Brendan Zondlak','Sensor Developer (2018-2019)','Michigan Technological University','MTU']
    ];
    for ( i = 0; i < people.length; i++ ) {
        if (i % 3 == 0) {
            text += '</div><div class="row">';
        }
        if(people[i][3]=='NAU')
        {
            text += '<div class="col-lg-4"> <div class="panel-panel-default"> <div class="panel-body"> <div class="about"> <h2>' + people[i][0] + '<br/><small> ' + people[i][1] + '</small><br/> <small> ' + 'Institution: '+ people[i][2] + '</small>  </h2> <img style = "height: 100px; width:150px" src="/cocotemp/images/NAU.png"> <hr /> </div> </div> </div> </div>';
        }
        else if(people[i][3]=='MTU')
        {
            text += '<div class="col-lg-4"> <div class="panel-panel-default"> <div class="panel-body"> <div class="about"> <h2>' + people[i][0] + '<br/><small> ' + people[i][1] + '</small><br/> <small> ' + 'Institution: ' + people[i][2] + '</small>  </h2> <img style = "height: auto; width:200px" src="/cocotemp/images/MTU.png"> <hr /> </div> </div> </div> </div>';
        }
    }
    document.getElementById('acknowledgeStart').innerHTML = text;
});