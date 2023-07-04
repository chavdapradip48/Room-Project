// var scheduleTaskCounter = 0;
$(document).ready(function () {
    $("body").prepend("<div id='header'></div><div id='api-responce'></div>");
    verifyUserToken();  
    if(sessionStorage.getItem("session_user") == null){
        SetUserSession(decodeJwt(getJwtTokenFromLocalStrorage()).id);
    }
    // if(scheduleTaskCounter++ === 0){
    //     scheduleTask();
    // }
    setTimeout(function() {
        scheduleTask();
    },notificationDurationInHour * 3600000);
});