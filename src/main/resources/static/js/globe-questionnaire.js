$(function () {
    Survey.StylesManager.applyTheme("bootstrap");

    Survey
        .FunctionFactory
        .Instance
        .register("checkSkyViewFactor", checkSkyViewFactor);
    Survey
        .FunctionFactory
        .Instance
        .register("checkTime", checkTime);


    function checkSkyViewFactor(Skyparams){

            if(Skyparams[1]=='No Canopy'&&Skyparams[0]==0){
                return 1;
            }

            return 0;
    }
    function checkTime(timeParams) {
            if(timeParams[0]!=undefined && timeParams[1]!=undefined && timeParams[0]<timeParams[1]){
                return 0;
            }
            if(timeParams[0]==undefined&&timeParams[1]==undefined){
                return 0;
            }
            return 1;
    }

    var surveyJSON = {
        pages:
            [
                {
                    name:"page20",
                    title:"Welcome to the GLOBE Site metadata Survey",
                    description:"Click Start to start the survey"},
                {
                    name:"page1",
                    elements:
                        [
                            {
                                type:"radiogroup",
                                name:"question1",
                                title:"Is your site in a natural or urban context?",
                                choices:
                                    [
                                        {
                                            value: "Natural",
                                            text:"Natural"
                                        },
                                {
                                    value:"Urban",
                                    text:"Urban"
                                }
                                ]
                            }
                            ]
                },
                {
                    name:"page2",
                    elements:
                        [
                            {
                                type:"checkbox",
                                name:"question2",
                                title:"What is the primary purpose of this site? (check all that apply).",
                                choices:
                                    [
                                        {
                                            value:"Commercial ",
                                            text:"Commercial Offices"
                                        },
                                        {
                                            value:"Retail",
                                            text:"Retail"
                                        },
                                        {
                                            value:"Restaurant",
                                            text:"Restaurant"
                                        },
                                        {
                                            value:"Industrial",
                                            text:"Industrial"
                                        },
                                        {
                                            value:"Construction ",
                                            text:"Construction Site"
                                        },
                                        {
                                            value:"Single Family Residential",
                                            text:"Single Family Residential"
                                        },
                                        {
                                            value:"Multi Family Residential",
                                            text:"Multi Family Residential"
                                        },
                                        {
                                            value:"Park or Greenbelt",
                                            text:"Park or Greenbelt"
                                        },
                                        {
                                            value:"Sports Facility",
                                            text:"Sports Facility"
                                        },
                                        {
                                            value:"Recreational Pool",
                                            text:"Recreational Pool"
                                        },
                                        {
                                            value:"Promenade or Plaza",
                                            text:"Promenade or Plaza"
                                        },
                                        {
                                            value:"Bike or Walking Path",
                                            text:"Bike or Walking Path"
                                        },
                                        {
                                            value:"Roadway or Parking Lot",
                                            text:"Roadway or Parking Lot"
                                        }
                                        ]
                            }
                            ]
                    ,
                    visibleIf:"{question1} = 'Urban'"},
                {
                    name:"page3",
                    elements:
                        [
                            {
                                type:"text",
                                name:"question3",
                                title:"What is the height of the sensor above the ground surface in meters (NOT the floor)?",
                                validators:
                                    [
                                        {
                                            type:"numeric",
                                            text:"Must be a numeric value",
                                            minValue:0,
                                            maxValue:100
                                        }
                                        ],
                                placeHolder:"0"
                            }
                            ]
                },
                {
                    name:"page4",
                    elements:
                        [
                            {
                                type:"text",
                                name:"question4",
                                title:"If the sensor is on a porch or in a building what is the height of the sensor above the floor surface in meters?",
                                validators:
                                    [
                                        {
                                            type:"numeric",
                                            text:"Must be a numeric value",
                                            minValue:0,
                                            maxValue:100
                                        }
                                        ],
                                placeHolder:"0"
                            }
                            ]
                },
                {
                    name:"page5",
                    elements:
                        [
                            {
                                type:"text",
                                name:"question5",
                                title:"What percent of the site is enclosed? (Do not include a percent sign)",
                                validators:
                                    [
                                        {
                                            type:"numeric",
                                            text:"Must be a numeric value. No percent sign",
                                            minValue:0,
                                            maxValue:100
                                        }
                                        ],
                                placeHolder:"0"
                            }
                            ]
                },
                {
                    name:"page6",
                    elements:
                        [
                            {
                                type:"text",
                                name:"question6",
                                title:" If the sensor is outdoors, how far is the sensor from the nearest airflow obstacle in meters (for example a wall, hedgerow,or building that is taller than the sensor's height)?",
                                validators:
                                    [
                                        {
                                            type:"numeric",
                                            text:"Must be a numeric value",
                                            minValue:0,
                                            maxValue:100
                                        }
                                        ],
                                placeHolder:"0"
                            }
                            ]
                },
                {
                    name:"page7",
                    elements:
                        [
                            {
                                type:"text",
                                name:"question7",
                                title:"In what direction is the nearest obstacle located from the sensor (degrees east of north, 0 = north, 90 = east, 180 = south, 270 = west)?",
                                validators:
                                    [
                                        {
                                            type:"numeric",
                                            text:"Must be a numeric value",
                                            minValue:0,
                                            maxValue:360
                                        }
                                        ],
                                placeHolder:"0"
                            }
                            ]
                },
                {
                    name:"page8",elements:
                        [
                            {
                                type:"radiogroup",
                                name:"question8",
                                title:"What is the obstacle?",
                                choices:
                                    [
                                        {
                                            value:"Building",
                                            text:"Building"
                                        },
                                        {
                                            value:"Wall",
                                            text:"Wall"
                                        },
                                        {
                                            value:"Hedgerow",
                                            text:"Hedgerow"
                                        },
                                        {
                                            value:"Other",
                                            text:"Other"
                                        }
                                        ]
                            }
                            ]
                }
                ,
                {
                    name:"page9",
                    elements:
                        [
                            {
                                type:"text",
                                name:"question9",
                                title:"Roughly how large/wide is the area (in meters) in which the sensor is located, before walls or obstacles are encountered?",
                                validators:
                                    [
                                        {
                                            type:"numeric",
                                            text:"Must be a numeric value",
                                            minValue:0,
                                            maxValue:100
                                        }
                                        ],
                                placeHolder:"0"
                            }
                            ]
                },
                {
                    name:"page10",
                    elements:
                        [
                            {
                                type:"radiogroup",
                                name:"question12",
                                title:"Is the sensor located in a depression or riparian area where water can collect?",
                                choices:
                                    [
                                        {
                                            value:"Yes",
                                            text:"Yes"
                                        },
                                        {
                                            value:"No",
                                            text:"No"
                                        }
                                        ]
                            },
                            {
                                type:"html",
                                name:"question20",
                                html:"<img src='../images/riparianArea.png' width='500' height='365'>"
                            }
                            ]
                },
                {
                    name:"page11",
                    elements:
                        [
                            {
                                type:"matrixdropdown",
                                name:"question10",
                                title:"On the longest day of the year (late June in N. Hemisphere), during what hours is the sensor unshaded and exposed to the sun?",
                                validators:
                                    [
                                        {
                                            type:"expression",
                                            text:"Your beginning time should be earlier than your ending time",
                                            expression:"checkTime({question10.Time.From},{question10.Time.To})<>1"
                                        }
                                    ]
                                ,
                                columns:
                                    [
                                        {
                                            name:"From",
                                            cellType:"dropdown"
                                        },
                                        {
                                            name:"To",
                                            cellType:"dropdown"
                                        }
                                        ],
                                choices:
                                    [
                                        {
                                            value:"0",
                                            text:"00:00"
                                        },
                                        {
                                            value:"1",
                                            text:"01:00"
                                        },
                                        {
                                            value:"2",
                                            text:"02:00"
                                        },
                                        {
                                            value:"3",
                                            text:"03:00"
                                        },
                                        {
                                            value:"4",
                                            text:"04:00"
                                        },
                                        {
                                            value:"5",
                                            text:"05:00"
                                        },
                                        {
                                            value:"6",
                                            text:"06:00"
                                        },
                                        {
                                            value:"7",
                                            text:"07:00"
                                        },
                                        {
                                            value:"8",
                                            text:"08:00"
                                        },
                                        {
                                            value:"9",
                                            text:"09:00"
                                        },
                                        {
                                            value:"10",
                                            text:"10:00"
                                        },
                                        {
                                            value:"11",
                                            text:"11:00"
                                        },
                                        {
                                            value:"12",
                                            text:"12:00"
                                        },
                                        {
                                            value:"13",
                                            text:"13:00"
                                        },
                                        {
                                            value:"14",
                                            text:"14:00"
                                        },
                                        {
                                            value:"15",
                                            text:"15:00"
                                        },
                                        {
                                            value:"16",
                                            text:"16:00"
                                        },
                                        {
                                            value:"17",
                                            text:"17:00"
                                        },
                                        {
                                            value:"18",
                                            text:"18:00"
                                        },
                                        {
                                            value:"19",
                                            text:"19:00"
                                        },
                                        {
                                            value:"20",
                                            text:"20:00"
                                        },
                                        {
                                            value:"21",
                                            text:"21:00"
                                        },
                                        {
                                            value:"22",
                                            text:"22:00"
                                        },
                                        {
                                            value:"23",
                                            text:"23:00"
                                        }
                                        ],
                                rows:["Time"]
                            }
                            ]
                },
                {
                    name:"page12",
                    elements:
                        [
                            {
                                type:"matrixdropdown",
                                name:"question11",
                                title:"On the shortest day of the year (late December in N. Hemisphere), during what hours is the sensor unshaded and exposed to the sun?",
                                validators:
                                    [
                                        {
                                            type:"expression",
                                            text:"Your beginning time should be earlier than your ending time",
                                            expression:"checkTime({question11.Time.From},{question11.Time.To})<>1"
                                        }
                                    ],
                                columns:
                                    [
                                        {
                                            name:"From",
                                            cellType:"dropdown"},
                                        {
                                            name:"To",
                                            cellType:"dropdown"
                                        }
                                        ],
                                choices:
                                    [
                                        {
                                            value:"0",
                                            text:"00:00"
                                        },
                                        {
                                            value:"1",
                                            text:"01:00"
                                        },
                                        {
                                            value:"2",
                                            text:"02:00"
                                        },
                                        {
                                            value:"3",
                                            text:"03:00"
                                        },
                                        {
                                            value:"4",
                                            text:"04:00"
                                        },
                                        {
                                            value:"5",
                                            text:"05:00"
                                        },
                                        {
                                            value:"6",
                                            text:"06:00"
                                        },
                                        {
                                            value:"7",
                                            text:"07:00"
                                        },
                                        {
                                            value:"8",
                                            text:"08:00"
                                        },
                                        {
                                            value:"9",
                                            text:"09:00"
                                        },
                                        {
                                            value:"10",
                                            text:"10:00"
                                        },
                                        {
                                            value:"11",
                                            text:"11:00"
                                        },
                                        {
                                            value:"12",
                                            text:"12:00"
                                        },
                                        {
                                            value:"13",
                                            text:"13:00"
                                        },
                                        {
                                            value:"14",
                                            text:"14:00"
                                        },
                                        {
                                            value:"15",
                                            text:"15:00"
                                        },
                                        {
                                            value:"16",
                                            text:"16:00"
                                        },
                                        {
                                            value:"17",
                                            text:"17:00"
                                        },
                                        {
                                            value:"18",
                                            text:"18:00"
                                        },
                                        {
                                            value:"19",
                                            text:"19:00"
                                        },
                                        {
                                            value:"20",
                                            text:"20:00"
                                        },
                                        {
                                            value:"21",
                                            text:"21:00"
                                        },
                                        {
                                            value:"22",
                                            text:"22:00"
                                        },
                                        {
                                            value:"23",
                                            text:"23:00"
                                        }
                                        ],
                                rows:["Time"]
                            }
                            ]
                },
                {
                    name:"page13",
                    elements:
                        [
                            {
                                type:"radiogroup",
                                name:"question13",
                                title:"Canopy type above the sensor (near enough to be captured in an upward photo)",
                                choices:
                                    [
                                        {
                                            value:"No Canopy",
                                            text:"No Canopy"
                                        },
                                        {
                                            value:"Tree/Vegetation",
                                            text:"Tree/Vegetation"
                                        },
                                        {
                                            value:"Shade Sail",
                                            text:"Shade Sail"
                                        },
                                        {
                                            value:"Pergola/Ramada",
                                            text:"Pergola/Ramada"},
                                        {
                                            value:"Other Solid Roof",
                                            text:"Other Solid Roof"
                                        }
                                        ]
                            }
                            ]

                },
                {
                    name:"page14",
                    elements:
                        [
                            {
                                type:"text",
                                name:"question14",
                                title:"If you stand in the location of the sensor and look straight up, roughly what percentage of the sky is visible (i.e. the Sky View Factor, do not use a percent sign. For example, 75 means 75% of sky is visible):",
                                validators:
                                    [
                                        {
                                            type:"numeric",
                                            text:"Must be a numeric value, do not use a percent sign",
                                            minValue:0,
                                            maxValue:100
                                        },
                                        {
                                            type:"expression",
                                            text:"You selected No Canopy so the Sky View factor must be greater than 0%",
                                            expression:"checkSkyViewFactor({question14},{question13})<>1"
                                        }
                                        ],
                                placeHolder:"0"
                            }
                            ]
                },
                {
                    name:"page15",
                    elements:
                        [
                            {
                                type:"text",
                                name:"question15",
                                title:"What is the slope on this site, measured as the ratio of rise over run (for example one meter of rise in fifteen meters of run is 1/15 or 6.7%.",
                                validators:
                                    [
                                        {
                                            type:"numeric",
                                            text:"Must be a numeric value",
                                            minValue:0,
                                            maxValue:100
                                        }
                                        ]
                                ,
                                placeHolder:"0"
                            }
                            ]
                },
                {
                    name:"page16",elements:
                        [
                            {
                                type:"text",
                                name:"question16",
                                visibleIf:"{question15} <> 0",
                                title:"What is the compass direction of the slope, uphill? (degrees east of north, 0 = north, 90 = east, 180 = south, 270 = west)",
                                validators:
                                    [
                                        {
                                            type:"numeric",
                                            text:"Must be a numeric value",
                                            minValue:0,
                                            maxValue:360
                                        }
                                        ],
                                placeHolder:"0"
                            }
                            ]
                },
                {
                    name:"page17",
                    elements:
                        [
                            {
                                type:"radiogroup",
                                name:"question17",
                                title:"What is the nearest body of open water?",
                                hasOther:true,
                                choices:
                                    [

                                        {
                                            value:"Swimming Pool",
                                            text:"Swimming Pool"
                                        },
                                        {
                                            value:"Large river",
                                            text:"Large river"
                                        },
                                        {
                                            value:"Small stream",
                                            text:"Small stream"
                                        },
                                        {
                                            value:"Lake/Pond",text:"Lake/Pond"
                                        }
                                        ]
                            }
                            ]
                },
                {
                    name:"page18",
                    elements:
                        [
                            {
                                type:"text",
                                name:"question18",
                                title:"How far away is this water from the sensor? (meters)",
                                validators:
                                    [
                                        {
                                            type:"numeric",
                                            text:"Must be a numeric value",
                                            minValue:0
                                        }
                                        ],
                                placeHolder:"0"
                            }
                            ]
                },
                {
                    name:"page19",
                    elements:
                        [
                            {
                                type:"text",
                                name:"question19",
                                title:" What is the compass direction of the water from this sensor? (degrees east of north, 0 = north, 90 = east, 180 = south, 270 = west)",
                                validators:
                                    [
                                        {
                                            type:"numeric",
                                            text:"Must be a numeric value",
                                            minValue:0,
                                            maxValue:360
                                        }
                                        ],
                                placeHolder:"0"
                            }
                            ]
                }
                ],
        clearInvisibleValues:"none",
        showTitle:false,
        showProgressBar:"bottom",
        progressBarType:"questions",
        firstPageIsStarted:true}

    function sendDataToServer(survey) {
        document.getElementById("skipForm").hidden=true;
        var json = JSON.parse(JSON.stringify(survey.data));

        var context= json["question1"];
        var purpose = json["question2"];
        var heightGround = json["question3"];
        var heightFloor = json["question4"];
        var enclosed = json["question5"];
        var airflow = json["question6"];
        var airflowDirection = json["question7"];
        var obstacle = json["question8"];
        var areaAround = json["question9"];
        var riparian = json["question12"];
        var longestTime= null;
        var shortestTime=null;
        if(json["question10"] !=undefined){
            var longTime = json.question10["Time"];
            var longFrom = longTime["From"];
            var longTo = longTime["To"];

             longestTime=longTo-longFrom;
        }
        if(json["question11"] != undefined){
            var shortTime = json.question11["Time"];
            var shortFrom = shortTime["From"];
            var shortTo = shortTime["To"];
            shortestTime=shortTo-shortFrom;

        }


        var canopy = json["question13"];
        var skyview = json["question14"];
        var slope = json["question15"];
        var slopeDirection = json["question16"];
        var water = json["question17"];
        var waterDistance = json["question18"];
        var waterDirection = json["question19"];

        if(context==undefined){
            context=null;
        }
        if(purpose==undefined){
            purpose=null;
        }
        if(heightGround==undefined){
            heightGround=-1;
        }
        if(longestTime==null){
            longestTime=-1;
        }
        if(shortestTime==null){
            shortestTime=-1;
        }
        if(heightFloor==undefined){
            heightFloor=-1;
        }
        if(enclosed==undefined){
            enclosed=-1;
        }if(airflow==undefined){
            airflow=-1;
        }if(airflowDirection==undefined){
            airflowDirection=-1;
        }
        if(obstacle==undefined){
            obstacle=null;
        }if(areaAround==undefined){
            areaAround=-1;
        }
        if(riparian==undefined){
            riparian=null;
        }
        if(canopy==undefined){
            canopy=null;
        }
        if(skyview==undefined){
            skyview=-1;
        }if(slope==undefined){
            slope=-1;
        }if(slopeDirection==undefined){
            slopeDirection=-1;
        }
        if(slope==0 && slopeDirection==undefined){
            slopeDirection=-9999;
        }
        if(water==undefined){
            water=null;
        }if(waterDirection==undefined){
            waterDirection=-1;
        }if(waterDistance==undefined){
            waterDistance=-1;
        }




        $.ajax({
            type: "POST",
            url: "/cocotemp/settings/new?_finish_globe",
            data: "environment=" + context + "&purpose=" + purpose+"&heightAboveGround="+heightGround+"&heightAboveFloor="+heightFloor+"&enclosurePercentage="+enclosed+"&nearestAirflowObstacle="+airflow+"&nearestObstacleDegrees="+airflowDirection+"&obstacleType="+obstacle+"&areaAroundSensor="+areaAround+"&riparianArea="+riparian+"&maxNightTime="+longestTime+"&minNightTime="+shortestTime+"&canopyType="+canopy+"&skyViewFactor="+skyview+"&slope="+slope+"&slopeDirection="+slopeDirection+"&nearestWater="+water+"&waterDistance="+waterDistance+"&waterDirection="+waterDirection+"&elevation="+elevation,
            success: function(response,data){

            },
            error: function(e){
                alert('Error: ' + e);
            }
        });
        document.getElementById("saveForm").hidden=false;

    }

    var survey = new Survey.Model(surveyJSON);
    $("#surveyContainer").Survey({
        model: survey,
        onComplete: sendDataToServer
    });


});
