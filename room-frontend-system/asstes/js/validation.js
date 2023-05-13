function borderErrorColor(ret_fild_valid, elementTag) {
    if (ret_fild_valid != true) {
        elementTag.css("border-color", "red");
        elementTag.css("background-color", "#ff8b8b26");
    } else {
        elementTag.css("border-color", "green");
        elementTag.css("background-color", "#86f7862b");
    }
}

// <<<<<<< HEAD:room-frontend-system/asstes/js/validationFunction.js
// =======
// function showToast(message, type) {
//     const $newDiv = $('<div>' + message + '</div>');   // create a div element
//     $newDiv.addClass('toast-message');    // set class for the div element
//     $('#api-responce').append($newDiv);    // append the div element to the body of the page
//     $newDiv.css("background-color", type === 'success' ? '#4CAF50' : '#F44336'); // set new div background color 
//     setTimeout(() => {
//         $newDiv.css("opacity", "0.5");
//     }, 4000);
//     setTimeout(() => {
//         $newDiv.css("display", "none");

//     }, 4000);
// }

// >>>>>>> feature/render-deploy-frontend:room-frontend-system/asstes/js/validation.js
function emailVlid(emails, errEle) {
    var nameRetn = true;
    if (emails == "") {
        errEle.css("display", "block");
        errEle.text(" Please fill the email id field");
        nameRetn = false;
    }
    else if (emails.indexOf("@") <= 0) {
        errEle.css("display", "block");
        errEle.text(" Invalid Email");
        nameRetn = false;
    }

    else if (emails.charAt(emails.length - 4) != "." && emails.charAt(emails.length - 3) != ".") {
        errEle.css("display", "block");
        errEle.text(" Invalid Email");
        nameRetn = false;
    }
    else {
        errEle.css("display", "none");
        errEle.text("");
        nameRetn = true;
    }
    if (nameRetn != true) {

        return false;
    } else {
        return true;
    }
}


function userVlid(user, errEle) {
    var nameRetn = true;
    if (user == "") {
        errEle.css("display", "block");
        errEle.text(" Please fill this field");
        nameRetn = false;
    }
    else if (user.length <= 3 || user.length > 20) {
        errEle.css("display", "block");
        errEle.text("lenght must be between 3 and 20");
        nameRetn = false;
    }
    else if (!isNaN(user)) {
        errEle.css("display", "block");
        errEle.text(" only characters are allowed");
        nameRetn = false;
    } else {
        errEle.css("display", "none");
        errEle.text("");
        nameRetn = true;
    }
    if (nameRetn != true) {
        return false;
    } else {
        return true;
    }
}

function addressVlid(user, errEle) {
    var nameRetn = true;
    if (user == "") {
        errEle.css("display", "block");
        errEle.text("Please fill this field");
        nameRetn = false;
    }
    else if (user.length <= 10 || user.length > 150) {
        errEle.css("display", "block");
        errEle.text("lenght must be between 10 and 150");
        nameRetn = false;
    }
    else {
        errEle.css("display", "none");
        errEle.text("");
        nameRetn = true;
    }
    if (nameRetn != true) {
        return false;
    } else {
        return true;
    }
}

function passVlid(pass, errEle) {
    var nameRetn = true;
    var pass_number = /^(?=.*[0-9])[a-zA-Z0-9!@#$%^&*]{8,16}$/;
    var pass_U_character = /^(?=.*[A-Z])[a-zA-Z0-9!@#$%^&*]{8,16}$/;
    var pass_L_character = /^(?=.*[a-z])[a-zA-Z0-9!@#$%^&*]{8,16}$/;
    var special_charavter = /^(?=.*[!@#$%^&*])[a-zA-Z0-9!@#$%^&*]{8,16}$/;

    if (pass == "" || pass == null) {
        errEle.css("display", "block");
        errEle.text(" Please fill the password field");
        nameRetn = false;
    }
    else if (pass.length <= 6 || pass.length > 16) {
        errEle.css("display", "block");
        errEle.text("Passwords lenght must be between  6 and 16");
        nameRetn = false;
    }
    else if (!(pass_number.test(pass))) {
        errEle.css("display", "block");
        errEle.text("enter min one number ");
        nameRetn = false;
    }
    else if (!(pass_U_character.test(pass))) {
        errEle.css("display", "block");
        errEle.text("enter min one uppercase character ");
        nameRetn = false;

    } else if (!(pass_L_character.test(pass))) {
        errEle.css("display", "block");
        errEle.text("enter min one lowercase charater ");
        nameRetn = false;
    } else if (!(special_charavter.test(pass))) {
        errEle.css("display", "block");
        errEle.text("enter min one special character ");
        nameRetn = false;
    } else {
        errEle.css("display", "none");
        errEle.text("");
        nameRetn = true;

    }
    if (nameRetn != true) {
        return false;
    } else {
        return true;
    }
}

function conpassVlid(confirmpass, pass, errEle) {
    var nameRetn = true;
    if (pass != confirmpass) {
        errEle.css("display", "block");
        errEle.text("Password Mismatch");
        return false;
    }

    else if (confirmpass == "") {
        errEle.css("display", "block");
        errEle.text("Please fill the confirmpassword field");
        return false;
    } else {
        errEle.css("display", "none");
        errEle.text("");
    }
    if (nameRetn != true) {
        return false;
    } else {
        return true;
    }
}

function numberVlid(mobileNumber, errEle) {
    var nameRetn = true;
    if (mobileNumber == "") {
        errEle.css("display", "block");
        errEle.text(" Please fill the mobile this field");
        nameRetn = false;
    }
    else if (isNaN(mobileNumber)) {
        errEle.css("display", "block");
        errEle.text(" user must write digits only not characters");
        nameRetn = false;
    }
    else if (mobileNumber.length != 10) {
        errEle.css("display", "block");
        errEle.text(" Mobile Number must be 10 digits only");
        nameRetn = false;
    } else {
        errEle.css("display", "none");
        errEle.text("");
        nameRetn = true;
    }
    if (nameRetn != true) {
        return false;
    } else {
        return true;
    }
}
function pincodeVlid(pincode, errEle) {
    var nameRetn = true;
    if (pincode == "") {
        errEle.css("display", "block");
        errEle.text(" Please fill the mobile this field");
        nameRetn = false;
    }
    else if (isNaN(pincode)) {
        errEle.css("display", "block");
        errEle.text(" user must write digits only not characters");
        nameRetn = false;
    }
    else if (pincode.length != 6) {
        errEle.css("display", "block");
        errEle.text(" Mobile Number must be 6 digits only");
        nameRetn = false;
    } else {
        errEle.css("display", "none");
        errEle.text("");
        nameRetn = true;
    }
    if (nameRetn != true) {
        return false;
    } else {
        return true;
    }
}
function otpVlid(otp, errEle) {
    var nameRetn = true;
    if (otp == "" || otp == null) {
        errEle.css("display", "block");
        errEle.text(" Please fill the mobile this field");
        nameRetn = false;
    }
    else if (otp.length != 6) {
        errEle.css("display", "block");
        errEle.text(" otp must be 6 digits only");
        nameRetn = false;
    }
    else if (isNaN(otp)) {
        errEle.css("display", "block");
        errEle.text(" user must write digits only not characters");
        nameRetn = false;
    }
    else {
        errEle.css("display", "none");
        errEle.text("");
        nameRetn = true;
    }
    if (nameRetn != true) {
        return false;
    } else {
        return true;
    }
}

function genderVlid(gender, errEle) {
    var nameRetn = true;
    if (gender == "selected" || gender == "") {
        errEle.css("display", "block");
        errEle.text("Please select this field ");
        nameRetn = false;
    }
    else {
        errEle.css("display", "none");
        errEle.text("");
        nameRetn = true;
    }
    if (nameRetn != true) {
        return false;
    } else {
        return true;
    }
}

function profileVlid(picture, errEle) {
    var nameRetn = true;
    if (picture == null || picture == "") {
        errEle.css("display", "block");
        errEle.text("Please select profile picture");
        nameRetn = false;
    }
    else {
        errEle.css("display", "none");
        errEle.text("");
        nameRetn = true;
    }
    if (nameRetn != true) {
        return false;
    } else {
        return true;
    }
}