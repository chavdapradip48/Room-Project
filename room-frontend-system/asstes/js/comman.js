$(document).ready(function () {
    
    $("body").prepend("<div id='header'></div><div id='api-responce'></div><div id='cover-spin'></div>");
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
                showToast(errorMessage.message, 'error');
                throw new Error("hello");
            }

        });
        return true;
    }
    catch (error) {
        console.log("new "+error);
        return false;
    }
}

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
            extractedPath != "registration.html" && //true
            extractedPath != "forgote-pass.html") { //true
            window.location.href = "login.html";

        }
    }
    else {
        if (extractedPath == "login.html" ||
            extractedPath == "registration.html" ||
            extractedPath == "forgote-pass.html") {
            window.location.href = "homePage.html";
        }
    }
    $('body').loader('hide');
}

function logout(){
    window.sessionStorage.removeItem("token");
    showToast("Logout Successfully...", 'success');
    window.location.reload();
}