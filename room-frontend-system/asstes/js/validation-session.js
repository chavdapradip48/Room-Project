// var scheduleTaskCounter = 0;
$(document).ready(function () {
    $("body").prepend("<div id='header'></div><div id='api-responce'></div>");
    verifyUserToken();  
    if(localStorage.getItem("session_user") == null){
        SetUserSession(decodeJwt(getJwtTokenFromLocalStrorage()).id);
    }
    // if(localStorage.getItem("visit_user") == "0"){
    //     scheduleTask();
    //     localStorage.setItem("visit_user", "1");
    // }
    // setTimeout(function() {
    //     scheduleTask();
    // },notificationDurationInHour * 3600000);
});