// <<<<<<< HEAD:room-frontend-system/asstes/js/comman.js
$(document).ready(function () {
    $("body").prepend("<div id='header'></div><div id='api-responce'></div>");
    verifyUserToken();
});
function showToast(message, type) {
    const $newDiv = $('<div>' + message + '</div>');   // create a div element
    $newDiv.addClass('toast-message');    // set class for the div element
    $('#api-responce').append($newDiv);    // append the div element to the body of the page
    $newDiv.css("background-color", type === 'success' ? '#4CAF50' : '#F44336'); // set new div background color
    setTimeout(() => {
        $newDiv.css("opacity", "0.5");
    }, 3000);
    setTimeout(() => {
        $newDiv.css("display", "none");

    }, 3000);
}

// =======
// // const apiUrl = 'https://roommates-782r.onrender.com';
// $('#cover-spin').show();
// // room-management-system
// >>>>>>> feature/render-deploy-frontend:room-frontend-system/asstes/js/validation-session.js
function validateJwtToken(accessToken) {
    try {
        $.ajax({
            url: backendServerUrl + "/verify-token",
            method: "POST",
            data: JSON.stringify({
                "jwt": window.sessionStorage.getItem("token")
            }),
            headers: {
                'Content-Type': 'application/json'
            },
            success: function (responce) {
                return true;
            },
            error: function (xhr, status, error) {
                var errorMessage = JSON.parse(xhr.responseText);
                window.sessionStorage.removeItem("token");
                window.sessionStorage.removeItem("session_user");
                showToast(errorMessage.message, 'error');
                window.location.reload();
            }

        });
        return true;
    }
    catch (error) {
        console.log("new "+error);
        return false;
    }
}
// <<<<<<< HEAD:room-frontend-system/asstes/js/comman.js
// =======
// var accessToken = window.sessionStorage.getItem("token"); // get token and store accessToken
// var sessionCheck = false;
// if (accessToken != null) { // accessToken find then this block execute
//     if (validateJwtToken(accessToken)) {
//         sessionCheck = true;
//     }
// }
// var page_url = window.location.href;
// var extractedPath = page_url.split("/").slice(-1);
// if (sessionCheck != true) {
//     window.localStorage.removeItem("token");
//     if (extractedPath != "login.html" && //false 
//         extractedPath != "register.html" && //true
//         extractedPath != "forgot.html") { //true
//         window.location.href = "login.html";
// >>>>>>> feature/render-deploy-frontend:room-frontend-system/asstes/js/validation-session.js

function verifyUserToken(){
    $('body').loader('show');
    var accessToken = window.sessionStorage.getItem("token"); // get token and store accessToken
    var sessionCheck = false;
    if (accessToken != null) { // accessToken find then this block execute
        if (validateJwtToken(accessToken)) {
            if(window.sessionStorage.getItem("token") != null){
                sessionCheck = true;
            }
        }
    }

    var page_url = window.location.href;
    var extractedPath = page_url.split("/").slice(-1);
    if (sessionCheck != true) {
        window.localStorage.removeItem("token");
        if (extractedPath != "login.html" && //false 
            extractedPath != "register.html" && //true
            extractedPath != "forgot.html") { //true
            window.location.href = "login.html";

        }
// =======
// }
// else {
//     if (extractedPath == "login.html" ||
//         extractedPath == "register.html" ||
//         extractedPath == "forgot.html") {
//         window.location.href = "home.html";
//         console.log("token not valid");
// >>>>>>> feature/render-deploy-frontend:room-frontend-system/asstes/js/validation-session.js
    }
    else {
        if (extractedPath == "login.html" ||
            extractedPath == "register.html" ||
            extractedPath == "forgot.html") {
            window.location.href = "home.html";
        }
    }
    $('body').loader('hide');
}

// <<<<<<< HEAD:room-frontend-system/asstes/js/comman.js
function logout(){
    window.sessionStorage.removeItem("token");
    window.sessionStorage.removeItem("session_user");
    showToast("Logout Successfully...", 'success');
    window.location.reload();
}

function decodeJwt(token) {
    var base64Payload = token.split(".")[1];
    var decodedPayload = atob(base64Payload);
    var uint8Array = new Uint8Array(decodedPayload.length);
    for (var i = 0; i < decodedPayload.length; i++) {
      uint8Array[i] = decodedPayload.charCodeAt(i);
    }
    var payload = new TextDecoder().decode(uint8Array);
    return JSON.parse(payload);
}
// =======
// $('#cover-spin').hide();
// >>>>>>> feature/render-deploy-frontend:room-frontend-system/asstes/js/validation-session.js
