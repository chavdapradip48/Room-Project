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
        $("#verified-otp,#otp-field").prop("disabled", true);
    }
});

function callSendOtp() {
    // $('#cover-spin').show();

    $.ajax({
        url: apiUrl + "/user/send-otp/" + $('#fEmail').val(),
        method: "POST",
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
