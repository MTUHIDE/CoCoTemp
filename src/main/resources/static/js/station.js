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
                    if(z.environment=="null"){
                        document.getElementById("Environment").textContent="Environment the site is in: Not filled out";

                    }
                    else{
                        document.getElementById("Environment").textContent="Environment the site is in: "+z.environment;
                    }
                    if(z.purpose=="null"){
                        document.getElementById("Purpose").textContent="Purpose of the area where the site is located: Not filled out";

                    }
                    else{
                        document.getElementById("Purpose").textContent="Purpose of the area where the site is located: "+z.purpose;
                    }
                    if(z.heightAboveGround==-1){
                        document.getElementById("HeightAboveGround").textContent="Height the sensor is above the ground: Not filled out";

                    }
                    else{
                        document.getElementById("HeightAboveGround").textContent="Height the sensor is above the ground: "+z.heightAboveGround+" m";

                    }
                    if(z.heightAboveFloor==-1){
                        document.getElementById("HeightAboveFloor").textContent="Height the sensor is above the floor: Not filled out";
                    }
                    else{
                        document.getElementById("HeightAboveFloor").textContent="Height the sensor is above the floor: "+z.heightAboveFloor+" m";
                    }
                    if(z.enclosurePercentage==-1){
                        document.getElementById("Enclosure").textContent="Enclosure Percentage: Not filled out";
                    }
                    else{
                        document.getElementById("Enclosure").textContent="Enclosure Percentage: "+z.enclosurePercentage+"%";
                    }
                    if(z.nearestAirflowObstacle==-1){
                        document.getElementById("AirObst").textContent="Nearest Airflow Obstacle: Not filled out";
                    }
                    else{
                        document.getElementById("AirObst").textContent="Nearest Airflow Obstacle: "+z.nearestAirflowObstacle+" m";
                    }
                    if(z.nearestObstacleDegrees==-1){
                        document.getElementById("ObstDeg").textContent="Nearest Obstacle Degrees: Not filled out";
                    }
                    else{
                        document.getElementById("ObstDeg").textContent="Nearest Obstacle Degrees: "+z.nearestObstacleDegrees+String.fromCharCode(176);
                    }
                    if(z.obstacleType=="null"){
                        document.getElementById("ObstType").textContent="Obstacle Type: Not filled out";
                    }
                    else{
                        document.getElementById("ObstType").textContent="Obstacle Type: "+z.obstacleType;
                    }
                    if(z.areaAroundSensor==-1){
                        document.getElementById("SenArea").textContent="Area Around Sensor: Not filled out";
                    }
                    else{
                        document.getElementById("SenArea").textContent="Area Around Sensor: "+z.areaAroundSensor+" m";
                    }
                    if(z.riparianArea=="null"){
                        document.getElementById("RipArea").textContent="Located in a depression or Riparian Area: Not filled out";
                    }
                    else{
                        document.getElementById("RipArea").textContent="Located in a depression or Riparian Area: "+z.riparianArea;
                    }
                    if(z.canopyType=="null"){
                        document.getElementById("Canopy").textContent="Canopy Type: Not filled out";
                    }
                    else{
                        document.getElementById("Canopy").textContent="Canopy Type: "+z.canopyType;
                    }
                    if(z.skyViewFactor==-1){
                        document.getElementById("skyView").textContent="SkyView Factor: Not filled out";
                    }
                    else{
                        document.getElementById("skyView").textContent="SkyView Factor: "+z.skyViewFactor+"%";
                    }
                    if(z.slope==-1){
                        document.getElementById("Slope").textContent="Slope of site location: Not filled out";
                    }
                    else{
                        document.getElementById("Slope").textContent="Slope of site location: "+z.slope+"%";
                    }
                    if(z.slopeDirection==-1){
                        document.getElementById("direction").textContent="Compass direction of slope: Not filled out";
                    }
                    else if(z.slopeDirection=-9999){
                        document.getElementById("direction").textContent="Compass direction of slope: No slope";

                    }
                    else{
                        document.getElementById("direction").textContent="Compass direction of slope: "+z.slopeDirection+String.fromCharCode(176);
                    }
                    if(z.nearestWater=="null"){
                        document.getElementById("WaterNear").textContent="Nearest Water: Not filled out";
                    }
                    else{
                        document.getElementById("WaterNear").textContent="Nearest Water: "+z.nearestWater;
                    }
                    if(z.waterDistance==-1){
                        document.getElementById("WaterDist").textContent="Distance water is from site: Not filled out";
                    }
                    else{
                        document.getElementById("WaterDist").textContent="Distance water is from site: "+z.waterDistance+" m";
                    }
                    if(z.waterDirection==-1){
                        document.getElementById("WaterDir").textContent="Compass direction of water: Not filled out";
                    }
                    else{
                        document.getElementById("WaterDir").textContent="Compass direction of water: "+z.waterDirection+String.fromCharCode(176);
                    }
                    if(z.maxNightTime==-1){
                        document.getElementById("maxNight").textContent="Hours unshaded and exposed to the sun on the longest day: Not filled out";
                    }
                    else{
                        document.getElementById("maxNight").textContent="Hours unshaded and exposed to the sun on the longest day: "+z.maxNightTime+ " hrs";
                    }
                    if(z.minNightTime==-1){
                        document.getElementById("minNight").textContent="Hours unshaded and exposed to the sun on the shortest day: Not filled out";
                    }
                    else{
                        document.getElementById("minNight").textContent="Hours unshaded and exposed to the sun on the shortest day: "+z.minNightTime+" hrs";
                    }
                }
            }
        });
    }
    _.defer(populateGlobeSiteData);
});