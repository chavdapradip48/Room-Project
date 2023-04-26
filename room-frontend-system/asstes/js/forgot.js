$("#generate-otp").click(function () {
    var femailren = emailVlid($('#fEmail').val(), $('#femail-err'));
    borderErrorColor(femailren, $('#fEmail'));
    if (femailren) {
        callSendOtp();
    }
});
$("#verified-otp").click(function () {
    var otpren = otpVlid($('#otp-field').val(), $('#otp-err'));
    borderErrorColor(otpren, $('#otp-field'));
    if (otpren) {
        // $("#verified-otp,#otp-field").prop("disabled", true);
        verifyOtp();
    }
});
$("#change-password").click(function () {
    var newPasswordRen = passVlid($('#newPassword').val(), $('#newPassword-err'));
    borderErrorColor(newPasswordRen, $('#newPassword'));
    var conPasswordRen = conpassVlid($('#rePassword').val(), $('#newPassword').val(), $('#rePassword-err'));
    borderErrorColor(conPasswordRen, $('#rePassword'))
    if (newPasswordRen && conPasswordRen) {
        $(".new-password-section,#change-password").prop("disabled", true);
        passwordChenge();
    }
});

function callSendOtp() {
    // $('#cover-spin').show();

    $.ajax({
        url: apiUrl + "/user/send-otp/" + $('#fEmail').val(),
        method: "GET",
        headers: {
            'Content-Type': 'application/json'
        },
        success: function (responce) {
            $(".otp-section").css("display", "block");
            $("#verified-otp").css("display", "block");
            $("#generate-otp , #fEmail").prop("disabled", true);
        },
        error: function (xhr, status, error) {
            // var errorMessage = JSON.parse(xhr.responseText);
            // console.log(errorMessage.message);
            $('#cover-spin').hide();
        }
    });
}

function verifyOtp() {
    // $('#cover-spin').show();

    $.ajax({
        url: apiUrl + "/user/verify-otp/" + $('#fEmail').val() + "/" + $('#otp-field').val(),
        method: "GET",
        headers: {
            'Content-Type': 'application/json'
        },
        success: function (responce) {
            $(".new-password-section").css("display", "block");
            $("#change-password").css("display", "block");
            $("#verified-otp,#otp-field").prop("disabled", true);
        },
        error: function (xhr, status, error) {
            // var errorMessage = JSON.parse(xhr.responseText);
            // console.log(errorMessage.message);
            $('#cover-spin').hide();
        }
    });
}

function passwordChenge() {
    // $('#cover-spin').show();

    $.ajax({
        url: apiUrl + "/user/change-password",
        method: "POST",
        data: JSON.stringify({
            "email": $('#fEmail').val(),
            "password": $("#newPassword").val()
        }),
        headers: {
            'Content-Type': 'application/json'
        },
        success: function (responce) {
            // $(".new-password-section").css("display", "block");
            // $("#change-password").css("display", "block");
            // $("#verified-otp,#otp-field").prop("disabled", true);
            window.location.href = "login.html";
            alert("password successfully change ");
        },
        error: function (xhr, status, error) {
            // var errorMessage = JSON.parse(xhr.responseText);
            // console.log(errorMessage.message);
            alert("password not change");
            $('#cover-spin').hide();
        }
    });
}
