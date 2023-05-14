$(document).ready(function () {
    $('body').loader('show');
    var queryParams = new URLSearchParams(window.location.search);
    var userId = queryParams.get('userId');
    if (userId != null && userId != "") {
        $(".delete-user").attr("disabled","disabled");
        viewUserProfile(userId);
    }else{
        var sessionUser=sessionStorage.getItem("session_user");
        if(sessionUser != null){
            sessionUserJson=JSON.parse(sessionUser);
            setUserProfileDOM(sessionUserJson);
        }
    }
    $('body').loader('hide');

    $(".delete-user").click(function(){
        // Display confirmation popup
        var confirmed = confirm("Are you sure you want to delete this account permanently?");

        // Check user's response
        if (confirmed) {
            $('body').loader('show');
            deleteUser(JSON.parse(window.sessionStorage.getItem("session_user")).id)
            .then(function(success) {
                if (success) {
                    showToast('User deleted successfully!', 'success');
                    logout();
                } else {
                    showToast('Failed to delete user.', 'error');
                }
                $('body').loader('hide');
            })
            .catch(function(error) {
                $('body').loader('hide');
                showToast('Error deleting user:', 'error');
            });
        }
    });
});

function setUserProfileDOM(sessionUserJson){
    var sessionUserphoto=sessionUserJson.profilePhoto;
    if(sessionUserphoto != null && sessionUserphoto != ""){
        $('#profile-image').attr('src',sessionUserphoto);
    }
    $('#name').text(sessionUserJson.firstName +" "+ sessionUserJson.lastName);
    $('#full-name').text(sessionUserJson.firstName +" "+ sessionUserJson.lastName);
    $('#gender').text(sessionUserJson.gender);
    $('#email').text(sessionUserJson.email);
    $('#mobile').text(sessionUserJson.mobile);
    var address=sessionUserJson.address;
    if(address != null){
        $('#general-value').text(address.generalAddress);
        $('#pincode-value').text(address.pincode);
        $('#state-value').text(sessionUserJson.state);
        $('#country-value').text(sessionUserJson.country);
    }
}

function viewUserProfile(userId) {
    $('body').loader('show');
    getUserById(userId)
    .then(function(response) {
        setUserProfileDOM(response.data);
        $('body').loader('hide');
    })
    .catch(function(error) {
      $('body').loader('hide');
      showToast('Failed to set user.', 'error');
    });
}
