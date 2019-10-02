$(function () {

    function populateGlobeSiteData(){
        $.ajax({
            method: 'post',
            url: "/cocotemp/sitemetadata/"+siteID,
            success: function (z) {
                if(z=="")
                {
                    document.getElementById("globeMeta").innerHTML="<p>Globe Survey Not Filled Out</p>";
                }
                else{
                    document.getElementById("Environment").textContent="Environment: "+z.environment;
                    document.getElementById("Purpose").textContent="Purpose: "+z.purpose;
                    document.getElementById("HeightAboveGround").textContent="Height Above Ground: "+z.heightAboveGround+" m";
                    document.getElementById("HeightAboveFloor").textContent="Height Above Floor: "+z.heightAboveFloor+" m";
                    document.getElementById("Enclosure").textContent="Enclosure Percentage: "+z.enclosurePercentage+"%";
                    document.getElementById("AirObst").textContent="Nearest Airflow Obstacle: "+z.nearestAirflowObstacle+" m";
                    document.getElementById("ObstDeg").textContent="Nearest Obstacle Degrees: "+z.nearestObstacleDegrees+String.fromCharCode(176);
                    document.getElementById("ObstType").textContent="Obstacle Type: "+z.obstacleType;
                    document.getElementById("SenArea").textContent="Area Around Sensor: "+z.areaAroundSensor+" m";
                    document.getElementById("RipArea").textContent="Located in a depression or Riparian Area: "+z.riparianArea;
                    document.getElementById("maxNight").textContent="Hours shaded on longest day: "+z.maxNightTime+ " hrs";
                    document.getElementById("minNight").textContent="Hours shaded on shortest day: "+z.minNightTime+" hrs";
                    document.getElementById("Canopy").textContent="Canopy Type: "+z.canopyType;
                    document.getElementById("skyView").textContent="SkyView Factor: "+z.skyViewFactor+"%";
                    document.getElementById("Slope").textContent="Slope of site location: "+z.slope+"%";
                    document.getElementById("direction").textContent="Compass direction of slope: "+z.slopeDirection+String.fromCharCode(176);
                    document.getElementById("WaterNear").textContent="Nearest Water: "+z.nearestWater;
                    document.getElementById("WaterDist").textContent="Distance water is from site: "+z.waterDistance+" m";
                    document.getElementById("WaterDir").textContent="Compass direction of water: "+z.waterDirection+String.fromCharCode(176);


                }
            }
        });

    }
    _.defer(populateGlobeSiteData);
});