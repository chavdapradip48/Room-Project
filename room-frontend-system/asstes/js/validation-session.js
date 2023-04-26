// const apiUrl = 'https://roommates-782r.onrender.com';
$('#cover-spin').show();
// room-management-system
function validateJwtToken(accessToken) {
    try {
        console.log("verify function is called");
        $.ajax({
            url: apiUrl + "/verify-token",
            method: "POST",
            async: false,
            data: JSON.stringify({
                "jwt": window.sessionStorage.getItem("token")
            }),
            headers: {
                'Content-Type': 'application/json'
            },
            success: function (responce) {
                console.log("API responce", responce);
                // alert("user tiken is valid");    
                showToast(responce.message, 'success');
            },
            error: function (xhr, status, error) {
                var errorMessage = JSON.parse(xhr.responseText);
                console.log(errorMessage.message);
                // alert("user tiken is not valid");
                showToast(errorMessage.message, 'error');
            }
        });
        return true;
    }
    catch (error) {
        return false;
    }
}
var accessToken = window.sessionStorage.getItem("token"); // get token and store accessToken
var sessionCheck = false;
if (accessToken != null) { // accessToken find then this block execute
    if (validateJwtToken(accessToken)) {
        sessionCheck = true;
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
        console.log("token not valid");
    }
}

$('#cover-spin').hide();
