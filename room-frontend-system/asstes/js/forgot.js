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
        passwordChenge();
    }
});

function callSendOtp() {
    $('body').loader('show');
    $("#generate-otp , #fEmail").prop("disabled", true);
    $.ajax({
        url: backendServerUrl + "/user/send-otp/" + $('#fEmail').val(),
        method: "GET",
        headers: {
            'Content-Type': 'application/json'
        },
        success: function (responce) {
            $(".otp-section").css("display", "block");
            $("#verified-otp").css("display", "block");
            $("#forgot-email-field").css("display", "none");
            $("#generate-otp").css("display", "none");
            showToast(responce.message, 'success')
            $('body').loader('hide');
        },
        error: function (xhr, status, error) {
            showToast(xhr.responseJSON.message, 'error');
            $("#generate-otp , #fEmail").prop("disabled", false);
            $('body').loader('hide');
        }
    });
}

function verifyOtp() {
    $('body').loader('show');
    $("#verified-otp,#otp-field").prop("disabled", true);
    $.ajax({
        url: backendServerUrl + "/user/verify-otp/" + $('#fEmail').val() + "/" + $('#otp-field').val(),
        method: "GET",
        headers: {
            'Content-Type': 'application/json'
        },
        success: function (responce) {
            $(".new-password-section").css("display", "block");
            $("#change-password").css("display", "block");
            $(".otp-section").css("display", "none");
            $("#verified-otp").css("display", "none");
            showToast(responce.message, 'success');
            $('body').loader('hide');
        },
        error: function (xhr, status, error) {
            showToast(xhr.responseJSON.message, 'error');
            if(xhr.responseJSON.message === "Invalid OTP."){
                $("#verified-otp,#otp-field").prop("disabled", false);    
            }
            else{
                $(".otp-section").css("display", "none");
                $("#verified-otp").css("display", "none");
                $("#forgot-email-field").css("display", "block");
                $("#generate-otp").css("display", "block");
                $("#generate-otp , #fEmail").prop("disabled", false);
                $("#verified-otp,#otp-field").prop("disabled", false);
            }
            
            $('body').loader('hide');
        }
    });
}

function passwordChenge() {
    $('body').loader('show');
    $(".new-password-section,#change-password").prop("disabled", true);
    $.ajax({
        url: backendServerUrl + "/user/change-password",
        method: "POST",
        data: JSON.stringify({
            "email": $('#fEmail').val(),
            "password": $("#newPassword").val()
        }),
        headers: {
            'Content-Type': 'application/json'
        },
        success: function (responce) {
            showToast(responce.message, 'success');
            $('body').loader('hide');
            window.location.href = "login.html";
        },
        error: function (xhr, status, error) {
            $('body').loader('hide');
            errorMessageProcess(xhr);
            $(".new-password-section,#change-password").prop("disabled", false);
        }
    });
}
