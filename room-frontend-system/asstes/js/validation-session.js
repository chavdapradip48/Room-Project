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
                "jwt": getJwtTokenFromLocalStrorage()
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

function verifyUserToken(){
    $('body').loader('show');
    var accessToken = getJwtTokenFromLocalStrorage(); // get token and store accessToken
    var sessionCheck = false;
    if (accessToken != null) { // accessToken find then this block execute
        if (validateJwtToken(accessToken)) {
            if(getJwtTokenFromLocalStrorage() != null){
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

function showOptions(element, operation) {
    $(element).siblings(".dropdown-menu").toggleClass(operation);
}

function getJwtTokenFromLocalStrorage(){
    return window.sessionStorage.getItem("token");
}

function validateForm() {
    // Reset error styles and messages
    $('.form-control').removeClass('error');
    $('.error-message').remove();
    
    // Flag to track form validity
    var isValid = true;
    
    // Validate each input field
    $('.form-control').each(function() {
      if ($(this).val() === '') {
        isValid = false;
        $(this).addClass('error');
        $(this).after('<p class="error-message">This field is required.</p>');
      }
    });
  return isValid;  
}

function SetUserSession(userId) {
    
      var settings = {
        "url": backendServerUrl + "/user/"+userId+"?projection=UserDTO",
        "method": "GET",
        "headers": {
          "Authorization": window.sessionStorage.getItem("token"),
        },
      };
      
      $.ajax(settings).done(function (response) {
        var sessionUser = response.data;
        sessionStorage.setItem('session_user', JSON.stringify(sessionUser));
        if(sessionUser.profilePhoto != null) {
            $("#user-profile").attr("src",sessionUser.profilePhoto);
        }
      }).fail(function(xhr, status, error) {
        showToast("Error setting user session: " + error, 'error');
      });
  }
  