$(document).ready(function () {
    function callLoginApi() {
        authenticateAndSetUserSession($("#email").val(), $("#password").val());
    }


    function authenticateAndSetUserSession(email, password) {
        $('body').loader('show');
        $.ajax({
          url: backendServerUrl + "/authenticate",
          method: "POST",
          data: JSON.stringify({
            "email": email,
            "password": password
          }),
          headers: {
            'Content-Type': 'application/json'
          },
          success: function (response) {
            // window.localStorage.setItem("visit_user", 0);
            window.localStorage.setItem("token", "Bearer " + response.jwt);
            showToast("User logged-in successfully", 'success');
            window.location.href = "home.html";
            // scheduleTask();
            $('body').loader('hide');
          },
          error: function (xhr, status, error) {
            $('body').loader('hide');
            errorMessageProcess(xhr);
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
