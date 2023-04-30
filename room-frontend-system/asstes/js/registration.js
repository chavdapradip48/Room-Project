function callLRegistrationApi() {
    // $('#cover-spin').show(); // It enable spinner 
    // Below ajax use to call create user API by passing set of user fields
    $('body').loader('show');
    $.ajax({
        url: backendServerUrl + "/user",
        method: "POST",
        data: JSON.stringify({
            "firstName": $("#fname").val(),
            "lastName": $("#lname").val(),
            "profilePhoto": $('#upload-profile-picture').val(),
            "password": $("#password-fild").val(),
            "gender": $("#gender").val(),
            "enabled": "true",
            "locked": "false",
            "mobile": $("#mobile-fild").val(),
            "email": $("#email-fild").val(),
            "address": {
                "generalAddress": $("#general-address").val(),
                "country": $("#country").val(),
                "state": $("#state").val(),
                "city": $("#city").val(),
                "pincode": $("#pin-code-fild").val()
            }
        }),
        headers: {
            'Content-Type': 'application/json'
        },
        success: function (responce) {
            // $('#cover-spin').hide(); // disable loader
            showToast("User created successsfully", 'success');
            $('body').loader('hide');
            window.location.href = "login.html"; // API response successfully then redirect login.html 
        },
        error: function (xhr, status, error) {
            // $('#cover-spin').hide(); // disable loader
            $('body').loader('hide');
            var errorMessage = JSON.parse(xhr.responseText); // convert json to plantext
            showToast(errorMessage.status+errorMessage.message, 'error');
            console.log(errorMessage.message); // console error
        }
    });
}


var currentTab = 0; // Current tab is set to be the first tab (0)
showTab(currentTab); // Display the current tab
$("#submit").css("display", "none");
// Below function use for hide and show all fields like personal details, address details, login details
function showTab(n) {//0
    var allTabs = $(".tab");
    allTabs.eq(n).css("display", "block");
    if (n == (allTabs.length - 1)) {
        $('#nextBtn').html("Submit");
    } else {
        $('#nextBtn').html("Next");
    }
    if (n == 0) {
        HideShow("#prevBtn", "none");
    } else {
        HideShow("#prevBtn", "inline");
    }
}
// Below function use for hide and show eny elements
function HideShow(element, value) {
    $(element).css("display", value);
}
// Below function use for click next botton then currentTab hide and nextTab show
function nextPrev(n) {
    var allTabs = $(".tab");
    // If n == 1 then user clicked next button otherwise previous
    if (!validateFields(currentTab + 1)) {
        return;
    }

    allTabs.eq(currentTab).css("display", "none");
    if (n == 1) {
        showTab(++currentTab);
    }
    else {
        showTab(--currentTab);
    }
}
// Below function use for check validation
function validateFields(stepNum) {
    var currentStep = $("#step" + stepNum).children(".form-group");
    if (stepNum == 1) {
        var fname = $('#fname').val();
        var lname = $('#lname').val();
        var gender = $('#gender').val();
        var profile_picture = $('#upload-profile-picture').val();
        var fname_error = $('#fname-err');
        var lname_error = $('#lname-err');
        var gender_error = $('#gender-err');
        var profile_picture_err = $('#profile-picture-err');

        var fname_retu = userVlid(fname, fname_error);
        var lname_retu = userVlid(lname, lname_error);
        var profile_retu = profileVlid(profile_picture, profile_picture_err);
        var gender_retu = genderVlid(gender, gender_error);

        borderErrorColor(fname_retu, $('#fname'));
        borderErrorColor(lname_retu, $('#lname'));
        borderErrorColor(gender_retu, $('#gender'));
        borderErrorColor(profile_retu, $('#upload-profile-picture'));

        if (fname_retu && lname_retu && profile_retu && gender_retu) {
            return true;
        } else {
            return false;
        }
    }
    if (stepNum == 2) {
        var general_address = $('#general-address').val();
        var address_error = $('#address-err');
        var country = $('#country').val();
        var country_err = $('#country-err');
        var state = $('#state').val();
        var state_err = $('#state-err');
        var city = $('#city').val();
        var city_err = $('#city-err');
        var pincode = $('#pin-code-fild').val();
        var pincode_err = $('#pincode-err');


        var address_retu = addressVlid(general_address, address_error);
        var country_retu = genderVlid(country, country_err);
        var state_retu = genderVlid(state, state_err);
        var city_retu = genderVlid(city, city_err);
        var pincode_retu = pincodeVlid(pincode, pincode_err);

        borderErrorColor(address_retu, $('#general-address'));
        borderErrorColor(country_retu, $('#country'));
        borderErrorColor(state_retu, $('#state'));
        borderErrorColor(city_retu, $('#city'));
        borderErrorColor(pincode_retu, $('#pin-code-fild'));

        if (address_retu && country_retu && state_retu && city_retu && pincode_retu) {
            return true;
        } else {
            return false;
        }
    }
    if (stepNum == 3) {
        var mobile = $('#mobile-fild').val();
        var mobile_err = $('#mobile-err');
        var email = $('#email-fild').val();
        var email_err = $('#email-err');
        var pass = $('#password-fild').val();
        var pass_err = $('#password-err');
        var cpass = $('#cpassword-fild').val();
        var cpass_err = $('#cpassword-err');

        var mobile_retu = numberVlid(mobile, mobile_err);
        var email_retu = emailVlid(email, email_err);
        var pass_retu = passVlid(pass, pass_err);
        var cpass_retu = conpassVlid(cpass, pass, cpass_err);

        borderErrorColor(mobile_retu, $('#mobile-fild'));
        borderErrorColor(email_retu, $('#email-fild'));
        borderErrorColor(pass_retu, $('#password-fild'));
        borderErrorColor(cpass_retu, $('#cpassword-fild'));
        if (mobile_retu && email_retu && pass_retu && cpass_retu) {
            console.log("form is valid");
            callLRegistrationApi();
            console.log("api Called successfully");
            return true;
        } else {
            return false;
        }

    }
}
// Below function use for read base64String
function encodeImageFileAsURL(element) {
    var input = $('#upload-profile-picture')[0];
    var reader = new FileReader();
    reader.onload = function (event) {
        var base64String = event.target.result;
    };
    reader.readAsDataURL(input.files[0]);
    $("#preview").attr("src ", base64String);
}

// Set Preview Image function
var preview = $('#preview');
var profile_picture = $('#upload-profile-picture');
profile_picture.change(function (event) {
    if (event.target.files.length == 0) {
        return 0;
    }
    var tempURL = URL.createObjectURL(event.target.files[0]);
    preview.attr('src', tempURL);
});


