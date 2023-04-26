
$(document).ready(function () {
    // console.log(apiUrl);
    function callLoginApi(email, password) {
        $('#cover-spin').show();
        $.ajax({
            url: apiUrl + "/authenticate",
            method: "POST",
            data: JSON.stringify({
                "email": email,
                "password": password
            }),
            headers: {
                'Content-Type': 'application/json'
            },
            success: function (responce) {
                console.log("API responce", responce);
                $('#cover-spin').hide();
                window.sessionStorage.setItem("token", "Bearer " + responce.jwt);
                showToast("User logged-in successsfully", 'success');
                window.location.href = "home.html";
            },
            error: function (xhr, status, error) {
                var errorMessage = JSON.parse(xhr.responseText);
                console.log(errorMessage.message);
                showToast(errorMessage.message, 'error');
                $('#cover-spin').hide();
            }
        });
    }

    $('#sub').click(function () {
        // get value
        var email = $('#email').val();
        var pass = $('#password').val();
        //error element stor 
        var email_error = $('#email-err');
        var pass_error = $('#password-err');
        // call function
        var email_ret = emailVlid(email, email_error);
        var pass_ret = passVlid(pass, pass_error);
        // give error border 
        borderErrorColor(email_ret, $('#email'));
        borderErrorColor(pass_ret, $('#password'));
        // form valide then call "callLoginApi()"
        if (email_ret && pass_ret) {
            console.log("form is valid");
            callLoginApi($("#email").val(), $("#password").val());
        }
    });
});
