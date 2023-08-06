// <<<<<<< HEAD:room-frontend-system/asstes/js/registration.js
// =======

// >>>>>>> feature/render-deploy-frontend:room-frontend-system/asstes/js/register.js
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
            "profilePhoto": localStorage.getItem("user_image"),
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
            localStorage.clear("user_image");
            window.location.href = "login.html"; // API response successfully then redirect login.html 
        },
        error: function (xhr, status, error) {
            // $('#cover-spin').hide(); // disable loader
            localStorage.clear("user_image");
            $('body').loader('hide');
            errorMessageProcess(xhr);
        }
    });
    
    reader.readAsDataURL(file.files[0]);
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
            // setProfileBase64LocalStorage().then(() => {
            //     callLRegistrationApi();
            // });

            resizeAndStoreImage("upload-profile-picture", 1)
            .then(() => {
                console.log("Image resized and stored successfully.");
                callLRegistrationApi();
            })
            .catch(error => {
                console.error("An error occurred:", error);
            });

            return true;
        } else {
            return false;
        }

    }
}

function resizeAndStoreImage(fileInputId, maxSizeMB) {
    return new Promise((resolve, reject) => {
        const fileInput = document.getElementById(fileInputId);
        const file = fileInput.files[0];

        if (!file) {
            reject(new Error("No file selected."));
            return;
        }

        const originalSizeMB = file.size / (1024 * 1024); // Size in MB

        if (originalSizeMB <= maxSizeMB) {
            console.log(`Original image size: ${originalSizeMB.toFixed(2)} MB`);
            const reader = new FileReader();
            reader.onload = function(event) {
                const base64String = event.target.result;
                localStorage.setItem("user_image", base64String);
                resolve();
            };
            reader.onerror = function(error) {
                reject(error);
            };
            reader.readAsDataURL(file);
        } else {
            const image = new Image();
            image.src = URL.createObjectURL(file);
            image.onload = function() {
                const maxWidth = 1024; // Adjust as needed
                const maxHeight = 1024; // Adjust as needed

                let newWidth = image.width;
                let newHeight = image.height;

                if (newWidth > maxWidth || newHeight > maxHeight) {
                    if (newWidth > maxWidth) {
                        const scaleFactor = maxWidth / newWidth;
                        newWidth = maxWidth;
                        newHeight *= scaleFactor;
                    }

                    if (newHeight > maxHeight) {
                        const scaleFactor = maxHeight / newHeight;
                        newHeight = maxHeight;
                        newWidth *= scaleFactor;
                    }
                }

                const canvas = document.createElement("canvas");
                canvas.width = newWidth;
                canvas.height = newHeight;

                const context = canvas.getContext("2d");
                context.drawImage(image, 0, 0, newWidth, newHeight);

                const resizedBase64 = canvas.toDataURL(file.type, 0.7); // Adjust quality if needed

                const resizedSizeMB = resizedBase64.length / (1024 * 1024); // Size in MB

                console.log(`Original image size: ${originalSizeMB.toFixed(2)} MB`);
                console.log(`Resized image size: ${resizedSizeMB.toFixed(2)} MB`);

                localStorage.setItem("user_image", resizedBase64);
                resolve();
            };
            image.onerror = function(error) {
                reject(error);
            };
        }
    });
}

function setProfileBase64LocalStorage() {
    return new Promise((resolve, reject) => {
      const file = $('#upload-profile-picture')[0];
      var reader = new FileReader();
      reader.onload = function(event) {
        // Get the base64 encoded string from the FileReader result
        var base64String = event.target.result;
        console.log(base64String);
        localStorage.setItem("user_image",base64String);
        resolve();
      };
      reader.onerror = function(error) {
        reject(error);
      };
      reader.readAsDataURL(file.files[0]);
    });
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


let auth_token;
$(document).ready(function () {
    $.ajax({
        type: 'get',
        url: 'https://www.universal-tutorial.com/api/getaccesstoken',
        success: function (data) {
            auth_token = data.auth_token;
            getCountry(data.auth_token)
        },
        error: function (error) {
            console.log(error);
        },
        headers: {
            "Accept": "application/json",
            "api-token": "6ipX_pWA4MT4iEk7S-1oRAZ7QvVq2PhmwQFpquICe6DLLHauIRUl_kCQNcrNxelcwSc",
            "user-email": "chavdasandipbhai@gmail.com"
        }

    });
});
function getCountry(auth_token) {
    $.ajax({
        type: 'get',
        url: 'https://www.universal-tutorial.com/api/countries/',
        success: function (data) {
            data.forEach(element => {
                $('#country').append('<option value="' + element.country_name + '">' + element.country_name + '</option>');
            });
        },
        error: function (error) {
            console.log(error);
        },
        headers: {
            "Authorization": "Bearer " + auth_token,
            "Accept": "application/json"
        }
    });
    $('#country').change(function () {
        getState();
    });
    $('#state').change(function () {
        getCity();
    });
}
function getState() {
    let country_name = $('#country').val();
    $.ajax({
        type: 'get',
        url: 'https://www.universal-tutorial.com/api/states/' + country_name,
        success: function (data) {
            $('#state').empty();
            $('#city').empty();
            $('#city').append('<option value="select">City</option>');
            data.forEach(element => {
                $('#state').append('<option value="' + element.state_name + '">' + element.state_name + '</option>');
            });
        },
        error: function (error) {
            console.log(error);
        },
        headers: {
            "Authorization": "Bearer " + auth_token,
            "Accept": "application/json"
        }
    });
}
function getCity() {
    let state_name = $('#state').val();
    console.log("Done");
    $.ajax({
        type: 'get',
        url: 'https://www.universal-tutorial.com/api/cities/' + state_name,
        success: function (data) {
            $('#city').empty();
            data.forEach(element => {
                $('#city').append('<option value="' + element.city_name + '">' + element.city_name + '</option>');
            });
        },
        error: function (error) {
            console.log(error);
        },
        headers: {
            "Authorization": "Bearer " + auth_token,
            "Accept": "application/json"
        }
    });
}