// function SetUserSession(userId) {

//     var settings = {
//       "url": backendServerUrl + "/user/"+userId+"?projection=UserDTO",
//       "method": "GET",
//       "headers": {
//         "Authorization": getJwtTokenFromLocalStrorage(),
//       },
//     };
    
//     $.ajax(settings).done(function (response) {
//       var sessionUser = response.data;
//     //   if (response.status == 200 && response.data != '') {
//         sessionStorage.setItem('session_user', JSON.stringify(sessionUser));
//     //   }
//     });
// }


$(document).ready(function() {
  $('#header').load('header.html');    
});
