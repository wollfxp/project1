document.addEventListener("DOMContentLoaded", function () {
    refreshMissionTimers();
});

function refreshMissionTimers(){
    var missions = document.querySelectorAll(".mission-control-mission-item");
    if (missions.length > 0){
        missions.forEach(function(item){
            updateEtaTimer(item);
        });
        setTimeout(refreshMissionTimers, 1000);
    }
}

function updateEtaTimer(missionElement){
    var timerElement = missionElement.querySelector(".mission-estimate-timer");
    var endTime = new Date(missionElement.dataset.missionEndTime);
    var secondsRemaining = ((endTime - new Date()) / 1000);
    if (secondsRemaining < 1){
        if (timerElement.textContent !== "Completed"){
            timerElement.textContent = "Completed";
            missionElement.querySelector(".mission-show-results-hidden").classList.remove("mission-show-results-hidden");
        }
    } else {
        var diff = moment(endTime).diff(moment());
        if (diff > 0){
            var duration = moment.duration(diff);
            timerElement.textContent = moment.utc(duration.asMilliseconds()).format("mm:ss");
        }
    }

}