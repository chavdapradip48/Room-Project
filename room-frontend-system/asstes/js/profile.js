$(document).ready(function () {
    var sessionUser=sessionStorage.getItem("session_user");
    if(sessionUser != null){
        sessionUserJson=JSON.parse(sessionUser);
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
            $('#state-value').text(sessionUserJson.state    );
            $('#country-value').text(sessionUserJson.country);
        }
    }

});